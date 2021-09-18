package com.cattle.spider.parse;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlCleanerParse implements XpathParse{

    private TagNode tagNode;

    @Override
    public List<String> xpath(String xpath) throws Exception {
        Object[] objects = tagNode.evaluateXPath(xpath);
        return Stream.of(objects).map(o -> {
            return o.toString();
        }).collect(Collectors.toList());
    }

    @Override
    public void setPage(Page page) {
        HtmlCleaner cleaner = new HtmlCleaner();
        tagNode = cleaner.clean(page.getHtml().get());
    }

    @Override
    public List<String> xpath(Html baseHtml, String xpath) throws Exception {
        HtmlCleaner cleaner = new HtmlCleaner();
        tagNode = cleaner.clean(baseHtml.get());
        return xpath(xpath);
    }


}
