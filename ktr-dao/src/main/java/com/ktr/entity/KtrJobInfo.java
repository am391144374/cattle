package com.ktr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 计划类
 */
@TableName("ktr_job")
public class KtrJobInfo {

    @TableId(type = IdType.AUTO)
    private Integer jobId;
    @TableField
    private String desc;
    @TableField
    private String ktrType;

}
