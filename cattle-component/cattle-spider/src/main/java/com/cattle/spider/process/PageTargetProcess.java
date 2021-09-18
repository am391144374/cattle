package com.cattle.spider.process;

import cn.hutool.core.util.StrUtil;
import com.cattle.common.ItemsHelper;
import com.cattle.common.constants.SpiderConstants;
import com.cattle.common.context.ProcessContent;
import com.cattle.common.filter.UrlFilterInterface;
import com.cattle.spider.SpiderConfig;
import com.cattle.spider.parse.HtmlCleanerParse;
import com.cattle.spider.parse.XpathParse;
import com.cattle.spider.parse.XsoupParse;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 扫描匹配的数据
 * @author lsj
 * @date 2021/03/10
 */
@Slf4j
public class PageTargetProcess implements PageProcessor {

    private Site site;
    private SpiderConfig spiderConfig;
    private ProcessContent processContent;
    private UrlFilterInterface urlFilter;

    @Override
    public void process(Page page) {
        XpathParse xpathParse = getXpathParse(page);
        LinkedHashMap<String,List<String>> columnMap = new LinkedHashMap<>();
        List<String> contentUrls = null;
        //判断当前页面是否为正文页
        boolean isContentUrl = false;
        //不是列表页就是正文页
        if(page.getUrl().regex(spiderConfig.getListRegex()).match() || page.getRequest().getUrl().equals(spiderConfig.getEntryUrl())) {
            List<String> scanUrls = new ArrayList<>();
            scanUrls.addAll(page.getHtml().links().regex(spiderConfig.getListRegex()).all());
            //添加匹配的正文页
            if(StrUtil.isNotBlank(spiderConfig.getContentXpath())){
                try {
                    contentUrls = xpathParse.xpath(spiderConfig.getContentXpath());
                    //url过滤 只正对正文页url 不对列表页url 做过滤 提高后续调用效率
                    if(urlFilter != null && spiderConfig.getScanUrlType() == 1){
                        String key = SpiderConstants.URL_FILTER_KEY + processContent.getJobId();
                        for (String contentUrl : contentUrls) {
                            if(urlFilter.exist(contentUrl,key)){
                                //判断当前页是否已经处理过，已经处理过直接返回
                                continue;
                            }else{
                                urlFilter.add(contentUrl,key);
                                scanUrls.add(contentUrl);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    processContent.putError(this,e);
                }
            }

            page.addTargetRequests(scanUrls);

            if(StrUtil.isNotBlank(spiderConfig.getFieldsJson())){
                AtomicInteger parseSize = new AtomicInteger(0);
                LinkedHashMap<String,String> fields = spiderConfig.getFields();
                for(String column : fields.keySet()){
                    try {
                        List<String> vstr = xpathParse.xpath(fields.get(column));
                        if(parseSize.get() == 0){
                            parseSize.set(vstr.size());
                        }
                        if(parseSize.get() != vstr.size()){
                            String warnText = String.format("当前页面：%s , 字段信息数量无法对应，请检查！",page.getUrl().get());
                            log.warn(warnText);
                            processContent.putWarn(this,new RuntimeException(warnText));
                            return;
                        }
                        columnMap.put(column,vstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        processContent.putError(this,e);
                    }
                }
            }

        }else{
            isContentUrl = true;
            spiderConfig.getContentFields().forEach((column,xpath) -> {
                try {
                    List<String> vstr = xpathParse.xpath(xpath);
                    columnMap.put(column,vstr);
                } catch (Exception e) {
                    e.printStackTrace();
                    processContent.putError(this,e);
                }
            });
        }

        List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
        List<String> finalContentUrls = contentUrls;
        //判断是否有匹配到正文页url
        AtomicBoolean needAddContent = new AtomicBoolean(false);
        if(contentUrls != null && contentUrls.size() > 0){
            needAddContent.set(true);
        }
        columnMap.forEach((column, vstr) -> {
            try {
                if (resultList.isEmpty() || resultList.size() != vstr.size()) {
                    for (int i = 0; i < vstr.size(); i++) {
                        LinkedHashMap param = new LinkedHashMap<>();
                        resultList.add(param);
                    }
                }
                for (int i = 0; i < vstr.size(); i++) {
                    LinkedHashMap<String, String> obj = resultList.get(i);
                    obj.put(column, vstr.get(i));
                    if(needAddContent.get()){
                        //因为要与正文页匹配所以需要单个添加
                        ItemsHelper.addField(spiderConfig.getBatchId(),finalContentUrls.get(i),obj);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                processContent.putError(this,e);
            }
        });

        /**
         * 这一块逻辑 有三种情况 目的是列表页的解析数据与正文页的数据正确匹配
         *
         * 1. 如果列表页有匹配正文页url，则在上面已经添加所以不需要再添加
         * 2. 如果没有匹配正文页url或者不需要匹配的，只有列表页面数据，则在这里直接添加
         * 3. 如果当前页面是正文页数据，则直接匹配保存
         */
        if(resultList.size() > 0 && !needAddContent.get()){
            if(isContentUrl){
                ItemsHelper.addContentField(spiderConfig.getBatchId(),page.getUrl().get(),resultList.get(0));
            }else{
                ItemsHelper.addFields(spiderConfig.getBatchId(),page.getUrl().get(),resultList);
            }

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
        getSite().setCycleRetryTimes(config.getCycleRetryTimes() != null ? config.getCycleRetryTimes() : 3)
                .setRetryTimes(config.getRetryTimes() != null ? config.getRetryTimes() : 3)
                .setSleepTime(config.getSleepTime() != null ? config.getSleepTime() * 1000 : 3000)
                .setTimeOut(config.getTimeOut() != null ? config.getTimeOut() * 1000 : 10000)
                .setRetrySleepTime(config.getRetrySleepTime() != null ? config.getRetrySleepTime() * 1000 : 2000);
        this.spiderConfig = config;
    }

    public void setProcessContext(ProcessContent processContent){
        this.processContent = processContent;
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

    public void setUrlFilter(UrlFilterInterface urlFilter){
        this.urlFilter = urlFilter;
    }
}
