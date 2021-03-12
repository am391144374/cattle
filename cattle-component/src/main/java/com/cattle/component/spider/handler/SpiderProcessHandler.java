package com.cattle.component.spider.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContext;
import com.cattle.component.spider.service.ConfigurableSpiderService;
import com.cattle.component.spider.process.PageTargetProcess;
import com.cattle.component.spider.SpiderConfig;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                        }else if(resultList.size() != vstr.size()){
                            resultList.clear();
                            for (int i = 0; i < vstr.size(); i++) {
                                resultList.add(new LinkedHashMap<>());
                            }
                        }
                        for (int i = 0; i < resultList.size(); i++) {
                            Map<String, String> obj = resultList.get(i);
                            obj.put(k, vstr.get(i));
                        }
                    });
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
                })).addPipeline(new ConsolePipeline()).runAsync(); //异步执行，使后续监控操作正常执行
        processContext.put("spiderWorker",spider);
        processContext.put("result",resultList);
    }
}
