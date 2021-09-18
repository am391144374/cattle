package component.kettle.meta;

import lombok.Data;

/**
 * 数据存储相关
 */
@Data
public class DBMate extends BaseMeta{

    /**
     * 在脚本中的连接名
     */
    private String dbName;
    private String port;
    private String hostName;
    private String database;
    private String userName;
    private String password;

}
