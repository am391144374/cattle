package com.cattle.component.spider;

import com.cattle.component.spider.handler.SpiderMonitorProcessHandler;
import com.cattle.component.spider.handler.SpiderProcessHandler;
import com.cattle.component.ProcessScript;
import com.cattle.common.context.ProcessContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫执行类
 */
public class SpiderScript extends ProcessScript {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SpiderConfig spiderConfig;
    private ProcessContext context;

    public SpiderScript(ProcessContext processContext){
        this.context = processContext;
    }

    @Override
    public void run() {
        context.setBatchId(spiderConfig.getBatchId());
        context.put("spiderConfig",spiderConfig);
        logger.info("{} start spider config:{}",Thread.currentThread().getName(),spiderConfig.toString());
        first.execute(context);
    }

    public void buildSpiderScript(SpiderConfig spiderConfig){
        this.spiderConfig = spiderConfig;
        SpiderProcessHandler spiderProcess = new SpiderProcessHandler();
        SpiderMonitorProcessHandler monitorProcess = new SpiderMonitorProcessHandler();

        addLastProcess(spiderProcess);
        addLastProcess(monitorProcess);
    }

}
