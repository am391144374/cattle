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

    private Integer xPathSelection;

}
