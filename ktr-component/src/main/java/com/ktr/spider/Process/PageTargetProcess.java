package com.ktr.spider.Process;

import com.ktr.ResultHelper;
import com.ktr.spider.meta.SpiderConfig;
import com.ktr.spider.parse.XpathParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 扫描匹配的数据
 * @author lsj
 * @date 2021/03/10
 */
public class PageTargetProcess implements PageProcessor {

    private Site site;
    private SpiderConfig spiderConfig;

    @Override
    public void process(Page page) {
        XpathParse xpathParse = spiderConfig.getXpathParse(page);
        if(page.getUrl().regex(spiderConfig.getListRegex()).match() || page.getRequest().getUrl().equals(spiderConfig.getEntryUrl())){
            page.addTargetRequests(page.getHtml().links().regex(spiderConfig.getListRegex()).all());
            spiderConfig.getFields().forEach((column,xpath) -> {
                try {
                    page.putField(column,xpathParse.xpath(xpath));
                } catch (Exception e) {
                    e.printStackTrace();
                    ResultHelper.setException(spiderConfig.getBatchId(),this,e);
                }
            });
        }else{
            spiderConfig.getContentFields().forEach((column,xpath) -> {
                try {
                    page.putField(column,xpathParse.xpath(xpath));
                } catch (Exception e) {
                    e.printStackTrace();
                    ResultHelper.setException(spiderConfig.getBatchId(),this,e);
                }
            });
        }
    }

    @Override
    public Site getSite() {
        if(site == null){
            site = Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        }
        return site;
    }

    public void setSpiderConfig(SpiderConfig config){
        getSite().setCycleRetryTimes(config.getCycleRetryTimes() != null ? config.getCycleRetryTimes() : 2)
                .setRetryTimes(config.getRetryTimes() != null ? config.getRetryTimes() : 3)
                .setSleepTime(config.getSleepTime() != null ? config.getSleepTime() : 5)
                .setRetrySleepTime(config.getRetrySleepTime() != null ? config.getRetrySleepTime() : 5);
        this.spiderConfig = config;
    }
}
