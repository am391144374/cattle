package com.cattle.entity.module;

import lombok.Data;

@Data
public class SpiderTestModule {

    /** 入口页 */
    private String entryUrl;
    /** 正文页xpath */
    private String contentXpath;
    /** 列表页正则表达式 */
    private String listRegex;

    /** xpath 解析选型 0 - htmlCleaner，1 - xSoup */
    private Integer xPathSelection;

    /** 列表页字段xpath */
    private String listFieldXPath;
    /** 正文页字段xpath */
    private String contentFieldXPath;

}
