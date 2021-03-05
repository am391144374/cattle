package com.ktr.meta;

import lombok.Builder;
import lombok.Data;
import org.pentaho.di.core.row.ValueMetaInterface;

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

    /** @see  org.pentaho.di.core.row.ValueMetaInterface  for excel */
    private int fieldType = ValueMetaInterface.TYPE_STRING;
    /** @see  org.pentaho.di.core.row.ValueMetaInterface  for excel */
    private int trimType = ValueMetaInterface.TRIM_TYPE_BOTH;

}
