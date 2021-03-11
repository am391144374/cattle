package com.cattle.component.spider;

import com.cattle.component.spider.handler.SpiderMonitorProcessHandler;
import com.cattle.component.spider.handler.SpiderProcessHandler;
import com.cattle.ProcessScript;
import com.cattle.common.context.ProcessContext;

/**
 * 爬虫执行类
 */
public class SpiderScript extends ProcessScript {

    private SpiderConfig spiderConfig;
    private ProcessContext context;

    public SpiderScript(ProcessContext processContext){
        this.context = processContext;
    }

    @Override
    public void run() {
        context.setBatchId(spiderConfig.getBatchId());
        context.put("spiderConfig",spiderConfig);
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
