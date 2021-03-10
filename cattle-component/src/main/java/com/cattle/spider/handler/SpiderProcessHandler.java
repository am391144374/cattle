package com.cattle.spider.handler;

import com.cattle.ExecuteProcessHandler;
import com.cattle.context.ProcessContext;
import com.cattle.spider.meta.SpiderConfig;

/**
 * spider执行方法
 */
public class SpiderProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContext processContext) {
        SpiderConfig spiderConfig = (SpiderConfig) processContext.get("spiderConfig");

    }

}
