package com.cattle.entity.kettle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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

}
