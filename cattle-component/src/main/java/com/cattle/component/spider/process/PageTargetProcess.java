package com.cattle.component.spider.process;

import cn.hutool.core.util.StrUtil;
import com.cattle.component.spider.parse.HtmlCleanerParse;
import com.cattle.component.spider.parse.XsoupParse;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.component.spider.parse.XpathParse;
import com.cattle.common.context.ProcessContext;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        //不是列表页就是正文页
        if(page.getUrl().regex(spiderConfig.getListRegex()).match() || page.getRequest().getUrl().equals(spiderConfig.getEntryUrl())) {
            page.addTargetRequests(page.getHtml().links().regex(spiderConfig.getListRegex()).all());
            //添加匹配的正文页
            if(StrUtil.isNotBlank(spiderConfig.getContentXpath())){
                page.addTargetRequests(page.getHtml().xpath(spiderConfig.getContentXpath()).all());
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
            LinkedHashMap<String,List<String>> contentMap = new LinkedHashMap<>();
            spiderConfig.getContentFields().forEach((column,xpath) -> {
                try {
                    List<String> vstr = xpathParse.xpath(xpath);
                    contentMap.put(column,vstr);
                } catch (Exception e) {
                    e.printStackTrace();
                    processContext.putError(this,e);
                }
            });

            if(contentMap.size() > 0){

            }
        }

        columnMap.forEach((column,vstr) -> {
            try {
                if (resultList.isEmpty() || resultList.size() != vstr.size()) {
                    resultList.clear();
                    for (int i = 0; i < vstr.size(); i++) {
                        resultList.add(new LinkedHashMap<>());
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
