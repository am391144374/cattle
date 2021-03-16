package com.cattle.component.spider.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.cattle.common.ItemsHelper;
import com.cattle.common.context.ProcessContext;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ProcessHandler;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.component.spider.service.Impl.ConfigurableSpiderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

import java.util.*;

public class ScanPageTest {

    Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void spiderProcessHandlerTest(){
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

        printValue(processContext);
    }

    @Test
    public void spiderContentTest(){
        ConfigurableSpiderServiceImpl spiderService = new ConfigurableSpiderServiceImpl();
        spiderService.setDataSource(buildDataSource());
        SpiderProcessHandler processHandler = new SpiderProcessHandler();
        SpiderMonitorProcessHandler monitorProcessHandler = new SpiderMonitorProcessHandler();
        monitorProcessHandler.setSpiderService(spiderService);
        processHandler.setNextHandler(monitorProcessHandler);

        long batchId = IdUtil.getSnowflake(1,1).nextId();
        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setBatchId(batchId);
        spiderConfig.setSpiderName("电影首发站");
        //采集全部可放开
        spiderConfig.setListRegex("https://www\\.wuhaozhan\\.net/movie/list/\\?p=\\d+");
//        spiderConfig.setListRegex("https://www.wuhaozhan.net/movie/list/?p=1");

        spiderConfig.setEntryUrl("https://www.wuhaozhan.net/movie/list/?p=1");
        spiderConfig.setFieldsJson("[{\"index\":1,\"key\":\"title\",\"value\":\"//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/text()\"},{\"index\":2,\"key\":\"rate\",\"value\":\"//div[@Class='pure-g']/div/div[position() > 10]/div/div[3]//a/span/text()\"},{\"index\":3,\"key\":\"url\",\"value\":\"//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/@href\"}]");
        spiderConfig.setContentXpath("//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/@href");
        spiderConfig.setContentFieldsJson("[{\"index\":1,\"key\":\"another\",\"value\":\"//div[@id='info']/text()\"}]");
        spiderConfig.setXPathSelection(0);
        spiderConfig.setTimeOut(500);
        spiderConfig.setRetrySleepTime(0);
        spiderConfig.setThreadNum(3);
        spiderConfig.setSleepTime(3000);
        spiderConfig.setTableName("spider_movie_info");

        ProcessContext processContext = new ProcessContext();
        processContext.setJobStatus(JobStatus.RUNNING);
        processContext.setBatchId(batchId);
        processContext.put("spiderConfig",spiderConfig);
        processHandler.execute(processContext);

        while (true){
            try {
                if(ItemsHelper.getField(batchId) != null){
                    List<LinkedHashMap<String, String>> resultList = ItemsHelper.getField(batchId);
                    System.out.println("result size " + resultList.size());
                    resultList.forEach(m -> {
                        m.forEach((k,v) -> {
                            System.out.println("key :" + k + " value :" + v);
                        });
                    });
                }
                if(processContext.get("spiderWorker") != null){
                    Spider spider = (Spider) processContext.get("spiderWorker");
                    if(spider.getStatus() == Spider.Status.Stopped){
                        List<LinkedHashMap<String, String>> resultList = ItemsHelper.getField(batchId);
                        System.out.println("result size " + resultList.size());
                        resultList.forEach(m -> {
                            m.forEach((k,v) -> {
                                System.out.println("key :" + k + " value :" + v);
                            });
                        });
                        break;
                    }
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public DruidDataSource buildDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("liushijie123");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/cattle?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return druidDataSource;
    }

    public void printValue(ProcessContext processContext){
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

    @Test
    public void spiderConfigTest(){
        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setFieldsJson("[{\"index\":0,\"key\":\"title\",\"value\":\"//*[@id=\\\"content\\\"]/div/div[1]/ol/li/div/div[2]/div[1]/a/span[1]/text()\"},{\"index\":1,\"key\":\"rate\",\"value\":\"//*[@id=\\\"content\\\"]/div/div[1]/ol/li/div/div[2]/div[2]/div/span[2]/text()\"}]");
        spiderConfig.setContentXpath("//*[@class='article']//div[@class='hd']/a/@href");
        spiderConfig.setContentFieldsJson("[{\"index\":0,\"key\":\"taptap\",\"value\":\"//*[@id='content']/h1/span[1]/text()\"}]");

        Set<String> columns = new HashSet<>();
        columns.addAll(spiderConfig.getFields().keySet());
        columns.addAll(spiderConfig.getContentFields().keySet());

        columns.forEach(k -> {
            log.info(k);
        });
    }
}
