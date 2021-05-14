package com.cattle.component.spider;

import cn.hutool.core.bean.BeanUtil;
import com.cattle.common.JobContextHelper;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.plugin.ExecuteScriptInterface;
import com.cattle.component.spider.filter.RedisBloomFilter;
import com.cattle.component.spider.handler.SpiderMonitorProcessHandler;
import com.cattle.component.spider.handler.SpiderProcessHandler;
import com.cattle.common.plugin.ProcessScript;
import com.cattle.common.context.ProcessContext;
import com.cattle.entity.CattleJob;
import com.cattle.entity.CattleSpiderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫执行类
 */
public class SpiderScript extends ProcessScript implements ExecuteScriptInterface {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CattleJob cattleJob;

    private SpiderConfig spiderConfig;

    @Override
    public void run() {
        ProcessContext context = new ProcessContext();
        JobContextHelper.setJobContext(cattleJob.getBatchId(),context);
        context.setJobStatus(JobStatus.RUNNING);
        try {
            buildConfig(cattleJob);
            buildSpiderScript();
            context.setScriptType(getScriptType());
            context.setJobId(cattleJob.getJobId());
            context.setBatchId(spiderConfig.getBatchId());
            context.setJobName(spiderConfig.getSpiderName());
            context.put("spiderConfig",spiderConfig);
            logger.info("{} start spider config:{}",Thread.currentThread().getName(),spiderConfig.toString());
            first.execute(context);
        }catch (Exception e){
            e.printStackTrace();
            context.putError(this,e);
        }
    }

    public void buildSpiderScript(){
        SpiderProcessHandler spiderProcess = new SpiderProcessHandler();
        if(spiderConfig.getScanUrlType() == 1){
            spiderProcess.setUrlFilter(new RedisBloomFilter());
        }
        SpiderMonitorProcessHandler monitorProcess = new SpiderMonitorProcessHandler();

        addLastProcess(spiderProcess);
        addLastProcess(monitorProcess);
    }

    @Override
    public void buildConfig(CattleJob job) {
        SpiderConfig spiderConfig = new SpiderConfig();
        CattleSpiderInfo cattleSpiderInfo = job.getSpiderInfo();
        BeanUtil.copyProperties(cattleSpiderInfo,spiderConfig,true);
        spiderConfig.setBatchId(job.getBatchId());
        this.spiderConfig = spiderConfig;
    }

    @Override
    public void setCattleJob(CattleJob job) {
        this.cattleJob = job;
    }

    @Override
    public String getScriptType() {
        return "spider";
    }

    @Override
    public void initEnv() {

    }
}
