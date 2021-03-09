package com.ktr.kettle.meta;

import lombok.Data;

/**
 * 数据存储相关
 */
@Data
public class DataBaseMeta {

    private String url;
    private String database;
    private String userName;
    private String password;

}
