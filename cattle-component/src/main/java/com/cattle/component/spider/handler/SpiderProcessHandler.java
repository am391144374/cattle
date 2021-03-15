package com.cattle.component.spider.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContext;
import com.cattle.component.spider.download.DefaultHttpClientDownloader;
import com.cattle.component.spider.service.ConfigurableSpiderService;
import com.cattle.component.spider.process.PageTargetProcess;
import com.cattle.component.spider.SpiderConfig;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 创建 webMagic 并执行
 */
public class SpiderProcessHandler extends ExecuteProcessHandler {

    private ConfigurableSpiderService spiderService;

    private SpiderConfig spiderConfig;

    @Override
    public void executeContent(ProcessContext processContext) {
        spiderConfig = (SpiderConfig) processContext.get("spiderConfig");
        if(spiderService == null){
            spiderService = SpringUtil.getBean(ConfigurableSpiderService.class);
        }
        PageTargetProcess pageTargetProcess = new PageTargetProcess();
        pageTargetProcess.setSpiderConfig(spiderConfig);
        pageTargetProcess.setProcessContext(processContext);
        Spider spider = Spider.create(pageTargetProcess);
        spider.addUrl(spiderConfig.getEntryUrl())
                .setUUID(String.valueOf(spiderConfig.getBatchId()))
                .thread(spiderConfig.getThreadNum())
                .setDownloader(new DefaultHttpClientDownloader())
                .addPipeline(((resultItems, task) -> {
                    List<LinkedHashMap<String, String>> resultList = resultItems.get("resultList");
                    //每调用一次则批量保存一次，防止后续调用pipeline进来时被覆盖
                    try {
                        spiderService.doPrepareSaveData(resultList,spiderConfig.getTableName(),spiderConfig.getFields(),spider.getUUID());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        processContext.putError(this,throwables);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        processContext.putError(this,e);
                    }
                    processContext.increment(resultList.size());
                })).addPipeline(new ConsolePipeline()).runAsync(); //异步执行，使后续监控操作正常执行
        processContext.put("spiderWorker",spider);
    }
}
