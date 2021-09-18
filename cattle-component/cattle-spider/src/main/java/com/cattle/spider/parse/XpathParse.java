package com.cattle.spider.parse;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

public interface XpathParse {

    List<String> xpath(String xpath) throws Exception;

    void setPage(Page page);

    List<String> xpath(Html baseHtml,String xpath) throws Exception;
}
