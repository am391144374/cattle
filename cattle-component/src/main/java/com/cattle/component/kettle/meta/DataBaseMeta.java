package com.cattle.component.kettle.meta;

import lombok.Data;

/**
 * 数据存储相关
 */
@Data
public class DataBaseMeta extends BaseMeta{

    /**
     * 在脚本中的连接名
     */
    private String dbName;
    private String url;
    private String database;
    private String userName;
    private String password;

}
