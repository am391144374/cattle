package com.cattle.web;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import com.cattle.common.ItemsHelper;
import com.cattle.common.JobContextHelper;
import com.cattle.common.context.ProcessContext;
import com.cattle.common.enums.JobStatus;
import com.cattle.component.kettle.KettleConfig;
import com.cattle.component.kettle.KettleConfigInit;
import com.cattle.component.kettle.KettleScript;
import com.cattle.component.kettle.meta.FieldMeta;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.component.spider.SpiderScript;
import com.cattle.entity.CattleJob;
import com.cattle.entity.kettle.KtrField;
import com.cattle.entity.kettle.KtrStep;
import com.cattle.entity.spider.SpiderConfigurable;
import com.cattle.service.api.RunLogService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lsj
 * 脚本最小执行类
 */
@Slf4j
public class CattleRun implements Closeable {

    /** 执行线程池 */
    private ExecutorService cattlePool = Executors.newFixedThreadPool(3, ThreadUtil.newNamedThreadFactory("cattle run -",false));
    /** 任务等待队列 */
    private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(3000);
    private boolean runFlag = true;
    /** kettle需要初始init 多线程调用使用线程安全的 AtomicBoolean*/
    private AtomicBoolean kettleInitFlag = new AtomicBoolean(false);
    /** 监控context线程 */
    private MonitorContext monitorContext;
    /** 执行脚本线程 */
    private RunJob scriptRunJob;
    // 日志保存
    private RunLogService runLogService;

    public CattleRun(RunLogService runLogService){
        this.runLogService = runLogService;
    }

    public void init(){
        //启动执行线程
        this.scriptRunJob = new RunJob();
        scriptRunJob.start();

        //启动监控context线程
        monitorContext = new MonitorContext();
        monitorContext.start();
    }

    class RunJob extends Thread{
        /**
         * 执行脚本
         */
        @Override
        public void run() {
            log.info("脚本执行线程启动成功！");
            while (runFlag){
                try {
                    Object config = queue.take();
                    cattlePool.submit(() -> {
                        ProcessContext context = new ProcessContext();
                        context.setJobStatus(JobStatus.RUNNING);
                        //spider
                        if(config instanceof SpiderConfig){
                            SpiderConfig spiderConfig = (SpiderConfig) config;
                            SpiderScript spiderScript = new SpiderScript(context);
                            context.setBatchId(spiderConfig.getBatchId());
                            context.setJobName(spiderConfig.getSpiderName());
                            spiderScript.buildSpiderScript(spiderConfig);
                            JobContextHelper.setJobContext(spiderConfig.getBatchId(),context);
                            runLogService.updateStatus(spiderConfig.getBatchId(), JobStatus.RUNNING);
                            spiderScript.run();
                        }else if(config instanceof  KettleConfig){
                            //kettle
                            if(!kettleInitFlag.get()){
                                try {
                                    KettleConfigInit.init();
                                    kettleInitFlag.set(true);
                                } catch (KettleException e) {
                                    e.printStackTrace();
                                    context.putError(this,e);
                                }
                            }
                            KettleConfig kettleConfig = (KettleConfig) config;
                            context.setJobName(kettleConfig.getJobName());
                            context.setBatchId(kettleConfig.getBatchId());
                            KettleScript kettleScript = new KettleScript(context);
                            kettleScript.buildKettleProcess(kettleConfig);
                            JobContextHelper.setJobContext(kettleConfig.getBatchId(),context);
                            runLogService.updateStatus(kettleConfig.getBatchId(), JobStatus.RUNNING);
                            kettleScript.run();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析 CattleJob 放到执行队列中
     * @param job
     * @throws InterruptedException
     */
    public void putSpiderJob(CattleJob job) throws InterruptedException {
        SpiderConfig spiderConfig = new SpiderConfig();
        SpiderConfigurable spiderConfigurable = job.getConfigurable();
        BeanUtil.copyProperties(spiderConfigurable,spiderConfig,true);
        spiderConfig.setBatchId(job.getBatchId());

        createRunLog(job);
        queue.put(spiderConfig);
    }

    /**
     * 解析cattleJob 放到执行队列中
     * @param job
     * @throws InterruptedException
     */
    public void putKettleJob(CattleJob job) throws InterruptedException {
        KettleConfig kettleConfig = new KettleConfig();
        List<KtrStep> stepInfoList = job.getStepInfoList();
        for(KtrStep stepInfo : stepInfoList){
            String stepType = stepInfo.getStepType();
            List<FieldMeta> fieldMetaList = new ArrayList<>();
            List<KtrField> stepFields = stepInfo.getFieldList();
            stepFields.forEach(stepField -> {
                FieldMeta fieldMeta = FieldMeta.builder()
                        .comment(stepField.getComment())
                        .name(stepField.getFieldName())
                        .type(stepField.getFieldType())
                        .value(stepField.getDefaultValue()).build();
                if(fieldMeta.getType().equals("Number")){
                    fieldMeta.setPrecision(stepField.getPrecision());
                }else if(!fieldMeta.getType().equals("Integer")){
                    fieldMeta.setLength(stepField.getLength());
                }
                fieldMetaList.add(fieldMeta);
            });
            switch (stepType){
                // excel导入
                case "excelImport":
                    kettleConfig.setSelectValueMap(fieldMetaList);
                    kettleConfig.setFileName(stepInfo.getFileList());
                    kettleConfig.setSheetName(new String[]{stepInfo.getSheetName()});
                    kettleConfig.setStartRow(new int[]{stepInfo.getStartRow()});
                    kettleConfig.setStartCol(new int[]{stepInfo.getStartCol()});
                    break;
                    // 字段设置
                case "selectValue":
                    kettleConfig.setSelectValueMap(fieldMetaList);
                    break;
                    //新增常量
                case "constant":
                    kettleConfig.setConstantMap(fieldMetaList);
                    break;
            }
        }
        kettleConfig.setScriptFile(job.getScriptPath());
        kettleConfig.setJobName(job.getJobName());
        kettleConfig.setWriteHeadRowIndex(2);
        kettleConfig.setBatchId(job.getBatchId());

        createRunLog(job);
        queue.put(kettleConfig);
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
    class MonitorContext extends Thread{
        @Override
        public void run() {
            log.info("context 监控线程启动成功！");
            while (true){
                Map<Long, ProcessContext> contextMap = JobContextHelper.getAllContext();
                contextMap.forEach((batchId,context) -> {
                    try {
                        switch (context.getJobStatus()){
                            //执行错误
                            case INTERRUPT:
                                log.error("{} - {} 执行错误",batchId,context.getJobName());
                                Set<String> errors = context.getError();
                                StringBuilder errorStr = new StringBuilder();
                                errors.forEach(error -> {
                                    errorStr.append(error);
                                });
                                runLogService.updateResult(batchId,0,errors.size(),errorStr.toString(),JobStatus.INTERRUPT);
                                JobContextHelper.remove(batchId);
                                ItemsHelper.remove(batchId);
                                break;
                            //执行完成
                            case FINISH:
                                log.error("{} - {} 执行完成！",batchId,context.getJobName());
                                runLogService.updateResult(batchId,context.getCount(),0,null,JobStatus.FINISH);
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
     * 创建执行log
     * @param job
     */
    private void createRunLog(CattleJob job){
        runLogService.createLog(job.getJobId(),job.getBatchId(),job.getJobName());
    }
}
