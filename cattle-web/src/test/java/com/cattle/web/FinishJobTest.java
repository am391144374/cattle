package com.cattle.web;

import com.cattle.common.enums.JobStatus;
import com.cattle.common.entity.CattleJob;
import com.cattle.common.entity.CattleRunLog;
import com.cattle.common.entity.CattleSpiderInfo;
import com.cattle.common.event.JobEventPublish;
import com.cattle.mapper.CustomizeSqlMapper;
import com.cattle.service.api.ConfigurableSpiderService;
import com.cattle.service.api.JobService;
import com.cattle.service.api.RunLogService;
import com.cattle.web.listener.FinishJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class FinishJobTest {

    @Autowired
    private RunLogService runLogService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ConfigurableSpiderService spiderService;

    @Resource
    private CustomizeSqlMapper customizeSqlMapper;

    @Test
    public void putSpiderTest(){
        FinishJob finishJob = new FinishJob(runLogService,spiderService,customizeSqlMapper);
        finishJob.init();
        CattleSpiderInfo cattleSpiderInfo = new CattleSpiderInfo();
        cattleSpiderInfo.setTableName("spider_movie_info");
        cattleSpiderInfo.setSpiderName("电影");
        cattleSpiderInfo.setListRegex("https://www.wuhaozhan.net/movie/list/?p=1");
        cattleSpiderInfo.setEntryUrl("https://www.wuhaozhan.net/movie/list/?p=1");
        cattleSpiderInfo.setFieldsJson("[{\"index\":1,\"key\":\"title\",\"value\":\"//div[@Class='pure-g']/div/div/div/div[2]/h1/a/text()\",\"isWhere\":\"否\"},{\"index\":2,\"key\":\"rate\",\"value\":\"//div[@Class='pure-g']/div/div/div/div[3]//a/span/text()\",\"isWhere\":\"否\"},{\"key\":\"url\",\"value\":\"//div[@Class='pure-g']/div/div/div/div[2]/h1/a/@href\",\"isWhere\":\"否\"}]");
        cattleSpiderInfo.setContentXpath("//div[@Class='pure-g']/div/div[position()>10]/div/div[2]/h1/a/@href");
        cattleSpiderInfo.setContentFieldsJson("[{\"key\":\"contentName\",\"value\":\"//h1/text()\",\"isWhere\":\"是\"}]");
        cattleSpiderInfo.setXPathSelection(0);
        cattleSpiderInfo.setThreadNum(2);

        CattleJob job = new CattleJob();
        job.setJobName("测试");
        job.setSpiderInfo(cattleSpiderInfo);
        job.setScriptType("spider");

        JobEventPublish.publishJobRun(job);

        printJobLog(job);

    }

    @Test
    public void putKettleTest(){
        FinishJob finishJob = new FinishJob(runLogService,null,customizeSqlMapper);
        finishJob.init();

        CattleJob job = jobService.buildExecuteJobInfo(1);
        job.setScriptType("kettle");

        JobEventPublish.publishJobRun(job);

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
