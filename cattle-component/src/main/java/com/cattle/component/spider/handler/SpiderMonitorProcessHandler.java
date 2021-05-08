package com.cattle.component.spider.handler;

import cn.hutool.core.util.StrUtil;
import com.cattle.common.ItemsHelper;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContext;
import com.cattle.component.spider.SpiderConfig;
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
        SpiderConfig spiderConfig = (SpiderConfig) processContext.get("spiderConfig");
        while (spider.getStatus() != Spider.Status.Stopped){
            //阻塞等待完成
            try {
                if(processContext.getJobStatus().getName().equals(JobStatus.INTERRUPT.getName())){
                    spider.stop();
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                processContext.putError(this,e);
            }
        }
        if(!processContext.getJobStatus().getName().equals(JobStatus.INTERRUPT.getName())){
            processContext.setJobStatus(JobStatus.FINISH);
        }
    }

}
