package com.cattle.spider.parse;

import us.codecraft.webmagic.Page;

import java.util.List;

public class XsoupParse implements XpathParse{

    private Page page;

    @Override
    public List<String> xpath(String xpath) throws Exception {
        return page.getHtml().xpath(xpath).all();
    }

    @Override
    public void setPage(Page page) {
        this.page = page;
    }
}
