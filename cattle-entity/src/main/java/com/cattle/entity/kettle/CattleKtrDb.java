package com.cattle.entity.kettle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lsj
 * 数据库信息表
 */
@Data
@TableName("cattle_ktr_db")
public class CattleKtrDb {

    @TableId(type = IdType.AUTO)
    private Integer dbId;
    @TableField
    private String name;
    @TableField
    private String dbDatabase;
    @TableField
    private String dbType;
    @TableField
    private String connectStr;
    @TableField
    private String loginName;
    @TableField
    private Integer port;
    @TableField
    private String loginPwd;

}
