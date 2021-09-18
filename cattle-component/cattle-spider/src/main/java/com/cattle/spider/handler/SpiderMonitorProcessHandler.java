package com.cattle.spider.handler;

import com.cattle.common.context.ProcessContent;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.spider.SpiderConfig;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

/**
 * 爬虫监控
 * todo 对爬虫进程进行监控，数据采集速率等
 */
@Slf4j
public class SpiderMonitorProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContent processContent) {
        Spider spider = (Spider) processContent.get("spiderWorker");
        SpiderConfig spiderConfig = (SpiderConfig) processContent.get("spiderConfig");
        while (spider.getStatus() != Spider.Status.Stopped){
            //阻塞等待完成
            try {
                //可自己中断
                if(processContent.getJobStatus().getName().equals(JobStatus.INTERRUPT.getName())){
                    spider.stop();
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                processContent.putError(this,e);
            }
        }
        if(!processContent.getJobStatus().getName().equals(JobStatus.INTERRUPT.getName())){
            processContent.setJobStatus(JobStatus.FINISH);
        }
    }

}
