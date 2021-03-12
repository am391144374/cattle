package com.cattle.component.spider.handler;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.context.ProcessContext;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ProcessHandler;
import com.cattle.component.spider.SpiderConfig;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class ScanPageTest {

    public static void main(String[] args){
        ProcessHandler processHandler = new SpiderProcessHandler();
        long batchId = IdUtil.getSnowflake(1,1).nextId();
        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setBatchId(batchId);
        spiderConfig.setSpiderName("杭州楼盘");
        spiderConfig.setListRegex("https://hz\\.newhouse\\.fang\\.com/house/s/b\\d+");
        spiderConfig.setEntryUrl("https://hz.newhouse.fang.com/house/s/b91");
        spiderConfig.setFieldsJson("[{\"index\":0,\"key\":\"name\",\"value\":\"//div[@class='nlc_details']//div[@class='nlcd_name']//a/text()\"},{\"index\":1,\"key\":\"address\",\"value\":\"//div[@class='address']//a/text()\"},{\"index\":2,\"key\":\"price\",\"value\":\"//div[@class='nhouse_price']/*[1]/text()\"}]");
        spiderConfig.setXPathSelection(0);
        spiderConfig.setThreadNum(2);

        ProcessContext processContext = new ProcessContext();
        processContext.setJobStatus(JobStatus.RUNNING);
        processContext.setBatchId(batchId);
        processContext.put("spiderConfig",spiderConfig);
        processHandler.execute(processContext);

        while (true){
            try {
                if(processContext.get("result") != null){
                    List<LinkedHashMap<String, String>> resultList = (List<LinkedHashMap<String, String>>) processContext.get("result");
                    System.out.println("result size " + resultList.size());
                }
                if(processContext.get("spiderWorker") != null){
                    Spider spider = (Spider) processContext.get("spiderWorker");
                    if(spider.getStatus() == Spider.Status.Stopped){
                        if(processContext.get("result") != null){
                            List<LinkedHashMap<String, String>> resultList = (List<LinkedHashMap<String, String>>) processContext.get("result");
                            System.out.println("result size " + resultList.size());
                            resultList.forEach(m -> {
                                m.forEach((k,v) -> {
                                    System.out.println("key :" + k + " value :" + v);
                                });
                            });
                        }
                        break;
                    }
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
