package com.cattle.component.kettle;

import com.cattle.component.kettle.meta.DataBaseMeta;
import com.cattle.component.kettle.meta.ExcelMeta;
import com.cattle.component.kettle.meta.FieldMeta;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class KettleConfig{

    private long batchId;

    /**
     * 脚本执行方式
     * normal-普通模式   即只执行脚本，不对脚本进行改造
     * edit-编辑模式     即对脚本进行编辑，然后再执行
     */
    private KettleProcessType processType;
    /**
     * 脚本文件路径
     */
    private String scriptFile;

    private String jobName;

    private ExcelMeta excelMeta;


    /** 数据导入字段 */
    private List<FieldMeta> selectValueMap = new ArrayList<>();
    /** 新增变量字段 */
    private List<FieldMeta> constantMap = new ArrayList<>();
    /** 数据存储 */
    private List<DataBaseMeta> dataBaseMetas;

   public static enum KettleProcessType{
        NORMAL,
        EDIT
    }
}
