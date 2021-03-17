package com.cattle.component.spider.process;

import cn.hutool.core.util.StrUtil;
import com.cattle.common.ItemsHelper;
import com.cattle.component.spider.parse.HtmlCleanerParse;
import com.cattle.component.spider.parse.XsoupParse;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.component.spider.parse.XpathParse;
import com.cattle.common.context.ProcessContext;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.*;

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
        List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
        LinkedHashMap<String,List<String>> columnMap = new LinkedHashMap<>();
        boolean isContentField = false;
        List<String> contentUrls = null;
        //不是列表页就是正文页
        if(page.getUrl().regex(spiderConfig.getListRegex()).match() || page.getRequest().getUrl().equals(spiderConfig.getEntryUrl())) {
            page.addTargetRequests(page.getHtml().links().regex(spiderConfig.getListRegex()).all());
            //添加匹配的正文页
            if(StrUtil.isNotBlank(spiderConfig.getContentXpath())){
                try {
                    contentUrls = xpathParse.xpath(spiderConfig.getContentXpath());
                } catch (Exception e) {
                    e.printStackTrace();
                    processContext.putError(this,e);
                }
                if(!contentUrls.isEmpty()){
                    page.addTargetRequests(contentUrls);
                }
            }

            spiderConfig.getFields().forEach((column,xpath) -> {
                try {
                    List<String> vstr = xpathParse.xpath(xpath);
                    columnMap.put(column,vstr);
                } catch (Exception e) {
                    e.printStackTrace();
                    processContext.putError(this,e);
                }
            });

        }else{
            spiderConfig.getContentFields().forEach((column,xpath) -> {
                try {
                    List<String> vstr = xpathParse.xpath(xpath);
                    columnMap.put(column,vstr);
                } catch (Exception e) {
                    e.printStackTrace();
                    processContext.putError(this,e);
                }
            });
            isContentField = true;
        }

        List<String> finalContentUrls = contentUrls;
        boolean finalIsContentField = isContentField;
        columnMap.forEach((column, vstr) -> {
            try {
                if (resultList.isEmpty() || resultList.size() != vstr.size()) {
                    resultList.clear();
                    for (int i = 0; i < vstr.size(); i++) {
                        LinkedHashMap param = new LinkedHashMap<>();
                        if(finalContentUrls != null && !finalIsContentField){
                            ItemsHelper.addListField(spiderConfig.getBatchId(),finalContentUrls.get(i),param);
                        }
                        resultList.add(param);
                    }
                }
                for (int i = 0; i < resultList.size(); i++) {
                    Map<String, String> obj = resultList.get(i);
                    obj.put(column, vstr.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
                processContext.putError(this,e);
            }
        });

        if(isContentField){
            ItemsHelper.addContentField(spiderConfig.getBatchId(),page.getUrl().get(),resultList);
        }

        page.putField("resultList",resultList);
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
                .setTimeOut(config.getTimeOut() != null ? config.getTimeOut() : 10)
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
