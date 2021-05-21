package com.cattle.web.util;

import com.cattle.component.spider.download.DefaultHttpClientDownloader;
import us.codecraft.webmagic.selector.Html;


public class HttpUtl {

    private static DefaultHttpClientDownloader httpClientDownloader = new DefaultHttpClientDownloader();

    public static Html downLoad(String url){
        return httpClientDownloader.download(url);
    }
}
