package com.cattle.web.listener;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.entity.CattleJob;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.event.CattleJobEvent;
import com.cattle.common.plugin.ExecuteScriptInterface;
import com.cattle.common.plugin.ExtensionComponentLoader;
import com.cattle.service.api.RunLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CattleJobEventRun implements ApplicationListener<CattleJobEvent> {

    @Autowired
    private RunLogService runLogService;

    public CattleJobEventRun(){
        initComponent();
    }

    /** 执行脚本 */
    private ExtensionComponentLoader componentLoader;

    @Async(value = "executeJobPool")
    @Override
    public void onApplicationEvent(CattleJobEvent cattleJobEvent) {
        //雪花算法
        long batchId = IdUtil.getSnowflake(1,1).nextId();
        try {
            CattleJob cattleJob = cattleJobEvent.getCattleJob();
            log.info("###执行job 开始：{}",cattleJob.getJobName());
            runLogService.createLog(batchId,cattleJob.getJobId(),cattleJob.getJobName());
            cattleJob.setBatchId(batchId);
            Class c = componentLoader.getComponentClass(cattleJob.getScriptType());
            if(c == null){
                log.error("错误的执行脚本类别 cattleJob:{}",cattleJob.toString());
                runLogService.updateErrorInfo(batchId,"错误的执行脚本类别");
                throw new RuntimeException("错误的执行脚本类别");
            }
            ExecuteScriptInterface execute = (ExecuteScriptInterface) c.newInstance();
            runLogService.updateStatus(batchId,JobStatus.RUNNING);
            cattleJob.setBatchId(batchId);
            execute.setCattleJob(cattleJob);
            execute.buildConfig(cattleJob);
            execute.run();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            runLogService.updateErrorInfo(batchId,e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            runLogService.updateErrorInfo(batchId,e.getMessage());
        }
    }

    public void initComponent(){
        componentLoader = new ExtensionComponentLoader();
        componentLoader.findComponentByPackage();
    }

}
