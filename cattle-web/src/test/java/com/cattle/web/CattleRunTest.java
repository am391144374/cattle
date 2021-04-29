package com.cattle.web;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.enums.JobStatus;
import com.cattle.entity.CattleJob;
import com.cattle.entity.CattleRunLog;
import com.cattle.entity.spider.SpiderInfoBO;
import com.cattle.service.api.JobService;
import com.cattle.service.api.RunLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CattleRunTest {

    @Autowired
    private RunLogService runLogService;

    @Autowired
    private JobService jobService;

    @Test
    public void putSpiderTest(){
        CattleRun cattleRun = new CattleRun(runLogService);
        cattleRun.init();

        SpiderInfoBO spiderInfoBO = new SpiderInfoBO();
        spiderInfoBO.setTableName("hz_lp");
        spiderInfoBO.setSpiderName("杭州楼盘");
        spiderInfoBO.setListRegex("https://hz\\.newhouse\\.fang\\.com/house/s/b\\d+");
        spiderInfoBO.setEntryUrl("https://hz.newhouse.fang.com/house/s/b91");
        spiderInfoBO.setFieldsJson("[{\"index\":0,\"key\":\"name\",\"value\":\"//div[@class='nlc_details']//div[@class='nlcd_name']//a/text()\"},{\"index\":1,\"key\":\"address\",\"value\":\"//div[@class='address']//a/text()\"},{\"index\":2,\"key\":\"price\",\"value\":\"//div[@class='nhouse_price']/*[1]/text()\"}]");
        spiderInfoBO.setXPathSelection(0);
        spiderInfoBO.setThreadNum(2);

        CattleJob job = new CattleJob();
        job.setJobName("测试");
        job.setConfigurable(spiderInfoBO);

        try {
            cattleRun.putQueue(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printJobLog(job);

    }

    @Test
    public void putKettleTest(){
        CattleRun cattleRun = new CattleRun(runLogService);
        cattleRun.init();

        CattleJob job = jobService.buildExecuteJobInfo(1);

        try {
            cattleRun.putQueue(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printJobLog(job);
    }

    private void printJobLog(CattleJob job){
        while (true){
            CattleRunLog runLog = runLogService.getById(job.getBatchId());
            if(runLog.getJobStatus().equals(JobStatus.FINISH.getName()) || runLog.getJobStatus().equals(JobStatus.INTERRUPT.getName())){
                log.info(runLog.toString());
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
