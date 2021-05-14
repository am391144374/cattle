package com.cattle.web;

import com.cattle.common.enums.JobStatus;
import com.cattle.entity.CattleJob;
import com.cattle.entity.CattleRunLog;
import com.cattle.entity.CattleSpiderInfo;
import com.cattle.service.api.ConfigurableSpiderService;
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

    @Autowired
    private ConfigurableSpiderService spiderService;

    @Test
    public void putSpiderTest(){
        CattleRun cattleRun = new CattleRun(runLogService,spiderService);
        cattleRun.init();

        CattleSpiderInfo cattleSpiderInfo = new CattleSpiderInfo();
        cattleSpiderInfo.setTableName("spider_movie_info");
        cattleSpiderInfo.setSpiderName("电影");
        cattleSpiderInfo.setListRegex("https://www.wuhaozhan.net/movie/list/?p=1");
        cattleSpiderInfo.setEntryUrl("https://www.wuhaozhan.net/movie/list/?p=1");
        cattleSpiderInfo.setFieldsJson("[{\"index\":1,\"key\":\"title\",\"value\":\"//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/text()\"},{\"index\":2,\"key\":\"rate\",\"value\":\"//div[@Class='pure-g']/div/div[position() > 10]/div/div[3]//a/span/text()\"},{\"index\":3,\"key\":\"url\",\"value\":\"//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/@href\"}]");
        cattleSpiderInfo.setContentXpath("//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/@href");
        cattleSpiderInfo.setContentFieldsJson("[{\"index\":1,\"key\":\"another\",\"value\":\"//div[@id='info']/text()\"}]");
        cattleSpiderInfo.setXPathSelection(0);
        cattleSpiderInfo.setThreadNum(2);

        CattleJob job = new CattleJob();
        job.setJobName("测试");
        job.setSpiderInfo(cattleSpiderInfo);
        job.setScriptType("spider");

        try {
            cattleRun.putQueue(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printJobLog(job);

    }

    @Test
    public void putKettleTest(){
        CattleRun cattleRun = new CattleRun(runLogService,null);
        cattleRun.init();

        CattleJob job = jobService.buildExecuteJobInfo(1);
        job.setScriptType("kettle");

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
            if(runLog != null){
                if(runLog.getJobStatus().equals(JobStatus.FINISH.getName()) || runLog.getJobStatus().equals(JobStatus.INTERRUPT.getName())){
                    log.info(runLog.toString());
                    break;
                }
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
