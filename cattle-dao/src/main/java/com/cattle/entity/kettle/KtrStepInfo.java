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
@TableName("ktr_step_info")
public class KtrStepInfo {

    @TableId(type = IdType.AUTO)
    private Integer stepId;
    @TableField
    private String stepName;
    @TableField
    private Integer jobId;
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
    @TableField(exist = false)
    private List<KtrStepField> fieldList;

}
