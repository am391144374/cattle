package com.cattle.spider.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.cattle.ExecuteProcessHandler;
import com.cattle.ResultHelper;
import com.cattle.context.ProcessContext;
import com.cattle.service.api.spider.ConfigurableSpiderService;
import com.cattle.spider.meta.SpiderConfig;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 爬虫数据存储实例
 */
@Slf4j
public class SpiderStorageProcessHandler extends ExecuteProcessHandler {

    private ConfigurableSpiderService spiderService;

    @Override
    public void executeContent(ProcessContext processContext) {
        if(spiderService == null){
            spiderService = SpringUtil.getBean(ConfigurableSpiderService.class);
        }
        SpiderConfig spiderConfig = (SpiderConfig) processContext.get("spiderConfig");
        Spider spider = (Spider) processContext.get("spiderWorker");
        List<LinkedHashMap<String, String>> resultList = (List<LinkedHashMap<String, String>>) processContext.get("result");
        //防止保存数据过大，占用内存，处理一部分数据后，清空 resultList，再等待执行
        if(resultList.size() > 500){
            try {
                spider.wait();
//                spiderService.saveData(resultList,spiderConfig,spider.getUUID());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                spider.close();
                ResultHelper.setException(processContext.getBatchId(),this,e);
            }finally {
                resultList.clear();
                if(!spider.isExitWhenComplete()){
                    spider.notify();
                }
            }
        }

    }

}
