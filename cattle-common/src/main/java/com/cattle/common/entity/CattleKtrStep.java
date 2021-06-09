package com.cattle.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * kettle 处理步骤信息
 * @author lsj
 */
@Data
@TableName("cattle_ktr_step")
public class CattleKtrStep {

    @TableId(type = IdType.AUTO)
    private Integer stepId;
    @TableField
    private String stepName;
    @TableField
    private String tableName;
    @TableField
    private Integer ktrInfoId;
    /** 步骤类型：excel导入-excelImport、自定义变量-constant
     * 自定义变量目前不做新增
     * */
    @TableField
    private String stepType;
    @TableField
    private String sheetName;
    @TableField
    private Integer startRow;
    @TableField
    private Integer startCol;
    @TableField
    private Integer nextStepId;
    @JsonIgnore
    @TableField
    private String fileList;
    @TableField(update = "now()")
    private Date updateTime;
    @TableField
    private Date createTime;
    @TableLogic(value = "0",delval = "1")
    @TableField
    private Integer deleted;

    @TableField(exist = false)
    private List<CattleKtrField> fieldList;
    /**
     * kettle 执行信息
     */
    @TableField(exist = false)
    private CattleKtrInfo ktrInfo;

    public String[] parseFileList(){
        return fileList.split(",");
    }

}
