package com.ktr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * excel字段
 */
@Data
@TableName("ktr_field_info")
public class FieldInfo {

    private Integer id;

    private String name;
    private String comment;
    private String type;
    private int length = -1;
    //new precision of field (for numbers)
    private int precision = -1;
    private String defaultValue;
    private String fieldType;

}
