package com.cattle.spider.handler;

import com.cattle.ExecuteProcessHandler;
import com.cattle.context.ProcessContext;
import com.cattle.service.JobStatusHelper;
import com.cattle.spider.Process.PageTargetProcess;
import com.cattle.spider.meta.SpiderConfig;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * spider执行方法
 */
public class SpiderProcessHandler extends ExecuteProcessHandler {

    private SpiderConfig spiderConfig;

    @Override
    public void executeContent(ProcessContext processContext) {
        spiderConfig = (SpiderConfig) processContext.get("spiderConfig");
        PageTargetProcess pageTargetProcess = new PageTargetProcess();
        pageTargetProcess.setSpiderConfig(spiderConfig);
        JobStatusHelper.updateStatus(spiderConfig.getBatchId(), JobStatusHelper.Status.RUNNING);
        final List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
        Spider spider = Spider.create(pageTargetProcess);
        spider.addUrl(spiderConfig.getEntryUrl())
                .setUUID(String.valueOf(spiderConfig.getBatchId()))
                .thread(spiderConfig.getThreadNum())
                .addPipeline(((resultItems, task) -> {
                    Map<String, Object> resultMap = resultItems.getAll();
                    resultMap.forEach((k, v) -> {
                        List<String> vstr = (ArrayList<String>) v;
                        if (resultList.isEmpty()) {
                            for (int i = 0; i < vstr.size(); i++) {
                                resultList.add(new LinkedHashMap<>());
                            }
                        }
                        for (int i = 0; i < resultList.size(); i++) {
                            Map<String, String> obj = resultList.get(i);
                            obj.put(k, vstr.get(i));
                        }
                    });
                })).addPipeline(new ConsolePipeline()).runAsync();
        processContext.put("spiderWorker",spider);
        processContext.put("result",resultList);
    }

}
