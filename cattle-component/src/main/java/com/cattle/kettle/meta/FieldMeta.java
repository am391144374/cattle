package com.cattle.kettle.meta;

import lombok.Builder;
import lombok.Data;

/**
 * @author lsj
 * 公共字段
 */
@Data
@Builder
public class FieldMeta {

    private String name;
    private String comment;
    private String type;
    private int length = -1;
    //new precision of field (for numbers)
    private int precision = -1;
    private String value;

}
