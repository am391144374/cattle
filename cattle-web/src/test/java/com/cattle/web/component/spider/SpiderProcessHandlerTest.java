package com.cattle.web.component.spider;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.context.ProcessContent;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ProcessHandler;
import com.cattle.spider.SpiderConfig;
import com.cattle.spider.handler.SpiderMonitorProcessHandler;
import com.cattle.spider.handler.SpiderProcessHandler;
import com.cattle.service.api.ConfigurableSpiderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SpiderProcessHandlerTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigurableSpiderService spiderService;

    @Test
    public void autoCreateTable(){
        ProcessHandler processHandler = new SpiderProcessHandler();
        long batchId = IdUtil.getSnowflake(1,1).nextId();
        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setBatchId(batchId);
        spiderConfig.setTableName("hz_lp");
        spiderConfig.setSpiderName("杭州楼盘");
        spiderConfig.setListRegex("https://hz\\.newhouse\\.fang\\.com/house/s/b\\d+");
        spiderConfig.setEntryUrl("https://hz.newhouse.fang.com/house/s/b91");
        spiderConfig.setFieldsJson("[{\"index\":0,\"key\":\"name\",\"value\":\"//div[@class='nlc_details']//div[@class='nlcd_name']//a/text()\"},{\"index\":1,\"key\":\"address\",\"value\":\"//div[@class='address']//a/text()\"},{\"index\":2,\"key\":\"price\",\"value\":\"//div[@class='nhouse_price']/*[1]/text()\"}]");
        spiderConfig.setXPathSelection(0);
        spiderConfig.setThreadNum(2);

        ProcessContent processContent = new ProcessContent();
        processContent.setBatchId(batchId);
        processContent.put("spiderConfig",spiderConfig);
        processContent.setJobStatus(JobStatus.RUNNING);

        processHandler.setNextHandler(new SpiderMonitorProcessHandler());
        processHandler.execute(processContent);
        logger.info(processContent.toString());
    }

}
