package com.cattle.component.spider.process;

import com.cattle.component.spider.parse.HtmlCleanerParse;
import com.cattle.component.spider.parse.XsoupParse;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.component.spider.parse.XpathParse;
import com.cattle.common.context.ProcessContext;
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
    private ProcessContext processContext;

    @Override
    public void process(Page page) {
        XpathParse xpathParse = getXpathParse(page);
        if(page.getUrl().regex(spiderConfig.getListRegex()).match() || page.getRequest().getUrl().equals(spiderConfig.getEntryUrl())){
            page.addTargetRequests(page.getHtml().links().regex(spiderConfig.getListRegex()).all());
            //todo 优先级最高，需要优化存储格式，在pipeline保存时不需要做数据新增，直接解析入库
            spiderConfig.getFields().forEach((column,xpath) -> {
                try {
                    page.putField(column,xpathParse.xpath(xpath));
                } catch (Exception e) {
                    e.printStackTrace();
                    processContext.putError(this,e);
                }
            });
        }else{
            spiderConfig.getContentFields().forEach((column,xpath) -> {
                try {
                    page.putField(column,xpathParse.xpath(xpath));
                } catch (Exception e) {
                    e.printStackTrace();
                    processContext.putError(this,e);
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
                .setSleepTime(config.getSleepTime() != null ? config.getSleepTime() : 10)
                .setRetrySleepTime(config.getRetrySleepTime() != null ? config.getRetrySleepTime() : 5);
        this.spiderConfig = config;
    }

    public void setProcessContext(ProcessContext processContext){
        this.processContext = processContext;
    }

    public XpathParse getXpathParse(Page page){
        XpathParse xpathParse;
        switch (spiderConfig.getXPathSelection()){
            case 1 :
                xpathParse = new XsoupParse();
                break;
            case 0:
            default:
                xpathParse = new HtmlCleanerParse();
        }
        xpathParse.setPage(page);
        return xpathParse;
    }
}
