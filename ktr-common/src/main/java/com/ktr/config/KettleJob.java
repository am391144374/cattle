package com.ktr.config;

import com.ktr.meta.DataBaseMeta;
import com.ktr.meta.ExcelMeta;
import com.ktr.meta.FieldMeta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KettleJob {

    private long batchId;

    private String scriptFile;
    private String jobName;
    private int writeHeadRowIndex;
    /** excel 数据源信息 */
    private ExcelMeta excelMeta;
    /** 字段数据 */
    private Map<String,List<FieldMeta>> fieldMetaMap = new ConcurrentHashMap<>();
    //数据存储
    private DataBaseMeta dataBaseMeta;

    public String getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getWriteHeadRowIndex() {
        return writeHeadRowIndex;
    }

    public void setWriteHeadRowIndex(int writeHeadRowIndex) {
        this.writeHeadRowIndex = writeHeadRowIndex;
    }

    public ExcelMeta getExcelMeta() {
        return excelMeta;
    }

    public void setExcelMeta(ExcelMeta excelMeta) {
        this.excelMeta = excelMeta;
    }

    public DataBaseMeta getDataBaseMeta() {
        return dataBaseMeta;
    }

    public void setDataBaseMeta(DataBaseMeta dataBaseMeta) {
        this.dataBaseMeta = dataBaseMeta;
    }

    public void putFieldMeta(FieldBelongType fieldBelongType, List<FieldMeta> fieldMetas){
        fieldMetaMap.putIfAbsent(fieldBelongType.getName(),fieldMetas);
    }

    public List<FieldMeta> getFieldMeta(FieldBelongType fieldBelongType){
        return fieldMetaMap.get(fieldBelongType.getName());
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public enum FieldBelongType{

        SELECT_VALUE("selectValue"),CONSTANT("constant");


        private String name;

        FieldBelongType(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

}
