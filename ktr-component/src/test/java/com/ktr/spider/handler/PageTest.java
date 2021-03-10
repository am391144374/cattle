package com.ktr.spider.handler;

import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class PageTest {

    @Test
    public void runTestPage(){
        Spider.create(new TestPageProcess()).addUrl("").thread(1).addPipeline(new ConsolePipeline()).run();
    }


    class TestPageProcess implements PageProcessor {

        private Site site;

        @Override
        public void process(Page page) {

        }

        @Override
        public Site getSite() {
            if(site == null){
                site = Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
            }
            return site;
        }
    }
}
