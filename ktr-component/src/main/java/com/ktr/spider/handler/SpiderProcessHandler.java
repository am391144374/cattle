package com.ktr.spider.handler;

import com.ktr.ExecuteProcessHandler;
import com.ktr.context.ProcessContext;
import com.ktr.spider.meta.SpiderConfig;

/**
 * spider执行方法
 */
public class SpiderProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContext processContext) {
        SpiderConfig spiderConfig = (SpiderConfig) processContext.get("spiderConfig");

    }

}
