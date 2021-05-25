package com.cattle.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author lsj
 * 步骤需求字段
 */
@Data
@TableName("cattle_ktr_field")
public class CattleKtrField {

    @TableId(type = IdType.AUTO)
    private Integer fieldId;
    @TableField
    private Integer stepId;
    @TableField
    private String fieldName;
    @TableField
    private String fieldType;
    @TableField(value = "`comment`")
    private String comment;
    @TableField
    private Integer length;
    @TableField(value = "`precision`")
    private Integer precision;
    @TableField
    private String defaultValue;
    @TableField(update = "now()")
    private Date updateTime;
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

}
