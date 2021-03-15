package com.cattle.web;

import cn.hutool.core.util.IdUtil;
import com.cattle.common.JobContextHelper;
import com.cattle.common.enums.JobStatus;
import com.cattle.entity.CattleJob;
import com.cattle.entity.CattleRunLog;
import com.cattle.entity.spider.SpiderConfigurable;
import com.cattle.service.api.JobService;
import com.cattle.service.api.RunLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicBoolean;

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

        SpiderConfigurable spiderConfigurable = new SpiderConfigurable();
        spiderConfigurable.setTableName("hz_lp");
        spiderConfigurable.setSpiderName("杭州楼盘");
        spiderConfigurable.setListRegex("https://hz\\.newhouse\\.fang\\.com/house/s/b\\d+");
        spiderConfigurable.setEntryUrl("https://hz.newhouse.fang.com/house/s/b91");
        spiderConfigurable.setFieldsJson("[{\"index\":0,\"key\":\"name\",\"value\":\"//div[@class='nlc_details']//div[@class='nlcd_name']//a/text()\"},{\"index\":1,\"key\":\"address\",\"value\":\"//div[@class='address']//a/text()\"},{\"index\":2,\"key\":\"price\",\"value\":\"//div[@class='nhouse_price']/*[1]/text()\"}]");
        spiderConfigurable.setXPathSelection(0);
        spiderConfigurable.setThreadNum(2);

        CattleJob job = new CattleJob();
        job.setBatchId(IdUtil.getSnowflake(1,1).nextId());
        job.setJobName("测试");
        job.setConfigurable(spiderConfigurable);

        try {
            cattleRun.putSpiderJob(job);
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
        job.setBatchId(IdUtil.getSnowflake(1,1).nextId());

        try {
            cattleRun.putKettleJob(job);
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
