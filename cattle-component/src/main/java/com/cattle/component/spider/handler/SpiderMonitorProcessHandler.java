package com.cattle.component.spider.handler;

import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.constant.JobStatus;
import com.cattle.common.context.ProcessContext;
import com.cattle.service.JobStatusHelper;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

/**
 * 爬虫监控
 * todo 对爬虫进程进行监控，数据采集速率等
 */
@Slf4j
public class SpiderMonitorProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContext processContext) {
        Spider spider = (Spider) processContext.get("spiderWorker");
        while (!spider.isExitWhenComplete()){
            //阻塞等待完成
            try {
                if(processContext.getJobStatus().getName().equals(JobStatus.Status.INTERRUPT.getName())){
                    spider.stop();
                    break;
                }
                Thread.currentThread().wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                processContext.putError(this,e);
            }
        }
        spider.close();
        log.info("{} 爬虫已经完成 ！",processContext.getBatchId());
        JobStatusHelper.updateStatus(processContext.getBatchId(), JobStatus.Status.FINISH);
    }

}
