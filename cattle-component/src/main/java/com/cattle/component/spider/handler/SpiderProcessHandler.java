package com.cattle.component.spider.handler;

import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContext;
import com.cattle.component.spider.download.DefaultHttpClientDownloader;
import com.cattle.common.filter.UrlFilterInterface;
import com.cattle.component.spider.process.PageTargetProcess;
import com.cattle.component.spider.SpiderConfig;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 创建 webMagic 并执行
 */
public class SpiderProcessHandler extends ExecuteProcessHandler {

    private SpiderConfig spiderConfig;

    @Override
    public void executeContent(ProcessContext processContext) {
        spiderConfig = (SpiderConfig) processContext.get("spiderConfig");
        PageTargetProcess pageTargetProcess = new PageTargetProcess();
        pageTargetProcess.setSpiderConfig(spiderConfig);
        pageTargetProcess.setProcessContext(processContext);
        pageTargetProcess.setUrlFilter(spiderConfig.getUrlFilterInterface());
        Spider spider = Spider.create(pageTargetProcess);
        DefaultHttpClientDownloader downloader = new DefaultHttpClientDownloader();
        downloader.setProcessContext(processContext);
        spider.addUrl(spiderConfig.getEntryUrl())
                .setUUID(String.valueOf(spiderConfig.getBatchId()))
                .thread(spiderConfig.getThreadNum())
                .setDownloader(downloader)
                .addPipeline(new ConsolePipeline())
                .runAsync(); //异步执行，使后续监控操作正常执行
        processContext.put("spiderWorker",spider);
    }
}
