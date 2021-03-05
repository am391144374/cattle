package com.ktr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 步骤信息
 */
public class StepBase {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField
    private Integer jobId;
    @TableField
    private String stepName;
    @TableField
    private Integer fieldId;
    @TableField
    private String stepType;

}
