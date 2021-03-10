package com.ktr.spider.parse;

import org.htmlcleaner.XPatherException;
import us.codecraft.webmagic.Page;

import java.util.List;

public interface XpathParse {

    List<String> xpath(String xpath) throws Exception;

    void setPage(Page page);
}
