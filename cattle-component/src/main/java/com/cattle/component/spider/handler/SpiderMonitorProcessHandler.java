package com.cattle.component.spider.handler;

import cn.hutool.core.util.StrUtil;
import com.cattle.common.ItemsHelper;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContext;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.component.spider.service.ConfigurableSpiderService;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 爬虫监控
 * todo 对爬虫进程进行监控，数据采集速率等
 */
@Slf4j
public class SpiderMonitorProcessHandler extends ExecuteProcessHandler {

    private ConfigurableSpiderService spiderService;

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
        List<LinkedHashMap<String, String>> result = ItemsHelper.getField(spiderConfig.getBatchId());
        if(result.size() > 0){
            try {
                Set<String> columns = new HashSet<>();
                columns.addAll(spiderConfig.getFields().keySet());
                if(StrUtil.isNotBlank(spiderConfig.getContentXpath())){
                    columns.addAll(spiderConfig.getContentFields().keySet());
                }
                spiderService.doPrepareSaveData(result,spiderConfig.getTableName(),columns,spider.getUUID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                processContext.putError(this,throwables);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                processContext.putError(this,e);
            }
        }
        if(!processContext.getJobStatus().getName().equals(JobStatus.INTERRUPT.getName())){
            processContext.setJobStatus(JobStatus.FINISH);
        }
    }

    public void setSpiderService(ConfigurableSpiderService spiderService) {
        this.spiderService = spiderService;
    }
}
