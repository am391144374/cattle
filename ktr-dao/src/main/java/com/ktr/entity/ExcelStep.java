package com.ktr.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *
 */
@TableName("ktr_excel_step")
public class ExcelStep extends StepBase {

    @TableField
    private String sheetName;
    @TableField
    private String fileName;
    @TableField
    private int startRow;
    @TableField
    private int startCol;
    @TableField
    private String sheetType;

}
