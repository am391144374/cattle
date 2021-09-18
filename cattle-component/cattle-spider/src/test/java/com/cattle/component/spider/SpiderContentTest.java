package com.cattle.component.spider;

import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class SpiderContentTest {

    /**
     * 连标签一起爬
     */
    @Test
    public void spiderALLContentTest(){
        Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                //保存包括所有的html标签
                page.putField("result",page.getHtml().xpath("//div[@class=tys-main-zt-show]/outerHtml()").all());
            }

            @Override
            public Site getSite() {
                return Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
            }
        }).addUrl("http://kjt.hunan.gov.cn/kjt/xxgk/gzdt/yw/202103/t20210317_14863216.html")
                .addPipeline(new ConsolePipeline()).run();
    }

}
