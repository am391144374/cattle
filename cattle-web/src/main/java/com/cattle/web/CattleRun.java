package com.cattle.web;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cattle.common.ItemsHelper;
import com.cattle.common.JobContextHelper;
import com.cattle.common.context.ProcessContext;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.plugin.ExecuteScriptInterface;
import com.cattle.common.plugin.ExtensionComponentLoader;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.entity.CattleJob;
import com.cattle.service.api.ConfigurableSpiderService;
import com.cattle.service.api.RunLogService;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lsj
 * 脚本最小执行类
 */
@Slf4j
public class CattleRun implements Closeable {

    /** 执行线程池 */
    private ExecutorService cattlePool = Executors.newFixedThreadPool(15, ThreadUtil.newNamedThreadFactory("cattle run -",true));
    /** 任务等待队列 */
    private LinkedBlockingQueue<CattleJob> queue = new LinkedBlockingQueue<>(3000);
    private boolean runFlag = true;
    /** 监控context线程 */
    private MonitorContextThread monitorContext;
    /** 执行脚本线程 */
    private RunJob scriptRunJob;
    // 日志保存
    private RunLogService runLogService;
    private ConfigurableSpiderService spiderService;

    /** 执行脚本 */
    private ExtensionComponentLoader componentLoader;

    public CattleRun(RunLogService runLogService,ConfigurableSpiderService spiderService){
        this.runLogService = runLogService;
        this.spiderService = spiderService;
    }

    public void init(){
        //启动执行线程
        this.scriptRunJob = new RunJob();
        scriptRunJob.start();

        //初始化组件扫描
        initComponent();

        //启动监控context线程
        monitorContext = new MonitorContextThread();
        monitorContext.start();
    }

    public void initComponent(){
        componentLoader = new ExtensionComponentLoader();
        componentLoader.findComponentByPackage();
    }

    class RunJob extends Thread{
        /**
         * 执行脚本
         */
        @Override
        public void run() {
            log.info("脚本执行线程启动成功！");
            while (runFlag){
                //雪花算法
                long batchId = IdUtil.getSnowflake(1,1).nextId();
                try {
                    CattleJob cattleJob = queue.take();
                    runLogService.createLog(batchId);
                    cattleJob.setBatchId(batchId);
                    Class c = componentLoader.getComponentClass(cattleJob.getScriptType());
                    if(c == null){
                        log.error("错误的执行脚本类别 cattleJob:{}",cattleJob.toString());
                        runLogService.updateErrorInfo(batchId,"错误的执行脚本类别");
                        continue;
                    }
                    ExecuteScriptInterface execute = (ExecuteScriptInterface) c.newInstance();
                    cattlePool.submit(() -> {
                        runLogService.updateJobInfo(cattleJob.getJobId(), batchId, cattleJob.getJobName(), JobStatus.RUNNING);
                        cattleJob.setBatchId(batchId);
                        execute.setCattleJob(cattleJob);
                        execute.run();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runLogService.updateErrorInfo(batchId,e.getMessage());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    runLogService.updateErrorInfo(batchId,e.getMessage());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    runLogService.updateErrorInfo(batchId,e.getMessage());
                }
            }
        }
    }

    @Override
    public void close() throws IOException {

    }

    /**
     * 监控processContext
     * 1.更新执行信息
     * 2.如果脚本中途执行失败，保存错误信息
     * 4.脚本执行成功，则更新job信息，保存结果信息
     * 3.删除对应的context，防止内存占用过多
     */
    class MonitorContextThread extends Thread{
        @Override
        public void run() {
            log.info("context 监控线程启动成功！");
            while (true){
                Map<Long, ProcessContext> contextMap = JobContextHelper.getAllContext();
                contextMap.forEach((batchId,context) -> {
                    try {
                        switch (context.getJobStatus()){
                            //执行中断
                            case INTERRUPT:
                                log.error("{} - {} 执行中断",batchId,context.getJobName());
                                Set<String> errors = context.getErrors();
                                StringBuilder errorStr = new StringBuilder();
                                errors.forEach(error -> {
                                    errorStr.append(error);
                                });
                                StringBuilder warnStr = new StringBuilder();
                                Set<String> warns = context.getWarns();
                                warns.forEach(warn -> {
                                    warnStr.append(warn);
                                });
                                runLogService.updateResult(batchId,0,errors.size(),errorStr.toString(),warns.size(),warnStr.toString(),JobStatus.INTERRUPT);
                                JobContextHelper.remove(batchId);
                                ItemsHelper.remove(batchId);
                                break;
                            //执行完成
                            case FINISH:
                                log.info("{} - {} 执行完成！",batchId,context.getJobName());
                                StringBuilder warnStrFin = new StringBuilder();
                                Set<String> warnsFin = context.getWarns();
                                warnsFin.forEach(warn -> {
                                    warnStrFin.append(warn);
                                });
                                runLogService.updateResult(batchId,context.getSuccessCount(),0,null,warnsFin.size(),warnStrFin.toString(),JobStatus.FINISH);
                                if("spider".equals(context.getScriptType())){
                                    storage(context);
                                }
                                JobContextHelper.remove(batchId);
                                ItemsHelper.remove(batchId);
                                break;
                            //执行中
                            case RUNNING:
                            default:
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        context.putError(this,e);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加执行队列
     * @param cattleJob
     * @throws InterruptedException
     */
    public void putQueue(CattleJob cattleJob) throws InterruptedException {
        queue.put(cattleJob);
    }

    public void storage(ProcessContext processContext){
        SpiderConfig spiderConfig = (SpiderConfig) processContext.get("spiderConfig");
        List<LinkedHashMap<String, String>> result = ItemsHelper.getPageField(spiderConfig.getBatchId());
        if(result != null && result.size() > 0){
            try {
                Set<String> columns = new HashSet<>();
                if(StrUtil.isNotBlank(spiderConfig.getFieldsJson())){
                    columns.addAll(spiderConfig.getFields().keySet());
                }
                if(StrUtil.isNotBlank(spiderConfig.getContentXpath())){
                    columns.addAll(spiderConfig.getContentFields().keySet());
                }
                spiderService.doPrepareSaveData(result,spiderConfig.getTableName(),columns, String.valueOf(processContext.getBatchId()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                processContext.putError(this,throwables);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                processContext.putError(this,e);
            }
            processContext.setSuccessCount(result.size());
        }
    }
}
