package com.cattle.component.spider.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.constant.JobStatus;
import com.cattle.common.context.ProcessContext;
import com.cattle.service.JobStatusHelper;
import com.cattle.service.api.spider.ConfigurableSpiderService;
import com.cattle.component.spider.Process.PageTargetProcess;
import com.cattle.component.spider.SpiderConfig;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * spider执行方法
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
        JobStatusHelper.updateStatus(spiderConfig.getBatchId(), JobStatus.Status.RUNNING);

        final List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
        Spider spider = Spider.create(pageTargetProcess);
        spider.addUrl(spiderConfig.getEntryUrl())
                .setUUID(String.valueOf(spiderConfig.getBatchId()))
                .thread(spiderConfig.getThreadNum())
                .addPipeline(((resultItems, task) -> {
                    Map<String, Object> resultMap = resultItems.getAll();
                    resultMap.forEach((k, v) -> {
                        List<String> vstr = (ArrayList<String>) v;
                        for (int i = 0; i < vstr.size(); i++) {
                            resultList.add(new LinkedHashMap<>());
                        }
                        for (int i = resultList.size() - vstr.size(); i < resultList.size(); i++) {
                            Map<String, String> obj = resultList.get(i);
                            obj.put(k, vstr.get(i));
                        }
                        try {
                            spiderService.doPrepareSaveData(resultList,spiderConfig.getTableName(),spiderConfig.getFields(),spider.getUUID());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            processContext.putError(this,throwables);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            processContext.putError(this,e);
                        }
                    });
                })).addPipeline(new ConsolePipeline()).runAsync();
        processContext.put("spiderWorker",spider);
        processContext.put("result",resultList);
    }

}
