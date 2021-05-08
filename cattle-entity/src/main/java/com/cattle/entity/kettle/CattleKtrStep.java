package com.cattle.entity.kettle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private Integer ktrInfoId;
    /** 步骤类型：excel导入-excelImport、字段选择-selectValue、自定义字段-constant */
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
    @TableField
    private Integer inputDbId;
    @TableField
    private String fileList;

    @TableField
    private Integer outputDbId;
    @TableField(exist = false)
    private List<CattleKtrField> fieldList;

    public String[] getFileList(){
        return fileList.split(",");
    }

}
