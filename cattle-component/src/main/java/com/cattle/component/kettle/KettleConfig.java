package com.cattle.component.kettle;

import com.cattle.component.kettle.meta.DataBaseMeta;
import com.cattle.component.kettle.meta.FieldMeta;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class KettleConfig {

    private long batchId;

    /**
     * 脚本执行方式
     * normal-普通模式   即只执行脚本，不对脚本进行改造
     * edit-编辑模式     即对脚本进行编辑，然后再执行
     */
    private String scriptType;

    private String scriptFile;
    private String jobName;
    private int writeHeadRowIndex;
    /** 文件路径 */
    private String[] fileName;
    /** sheet名 */
    private String[] sheetName;
    /** 开始行 */
    private int[] startRow;
    /** 开始列 */
    private int[] startCol;
    /** 数据导入字段 */
    private List<FieldMeta> selectValueMap = new ArrayList<>();
    /** 新增变量字段 */
    private List<FieldMeta> constantMap = new ArrayList<>();
    /** 数据存储 */
    private DataBaseMeta dataBaseMeta;

    public String getScriptFile() {
        return scriptFile;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
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

    public DataBaseMeta getDataBaseMeta() {
        return dataBaseMeta;
    }

    public void setDataBaseMeta(DataBaseMeta dataBaseMeta) {
        this.dataBaseMeta = dataBaseMeta;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public List<FieldMeta> getSelectValueMap() {
        return selectValueMap;
    }

    public void setSelectValueMap(List<FieldMeta> selectValueMap) {
        this.selectValueMap = selectValueMap;
    }

    public List<FieldMeta> getConstantMap() {
        return constantMap;
    }

    public void setConstantMap(List<FieldMeta> constantMap) {
        this.constantMap = constantMap;
    }

    public String[] getFileName() {
        return fileName;
    }

    public void setFileName(String[] fileName) {
        this.fileName = fileName;
    }

    public String[] getSheetName() {
        return sheetName;
    }

    public void setSheetName(String[] sheetName) {
        this.sheetName = sheetName;
    }

    public int[] getStartRow() {
        return startRow;
    }

    public void setStartRow(int[] startRow) {
        this.startRow = startRow;
    }

    public int[] getStartCol() {
        return startCol;
    }

    public void setStartCol(int[] startCol) {
        this.startCol = startCol;
    }
}
