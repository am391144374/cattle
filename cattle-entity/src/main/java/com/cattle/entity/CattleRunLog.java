package com.cattle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("cattle_run_log")
public class CattleRunLog {

    @TableField
    private Integer jobId;
    @TableId(type = IdType.INPUT)
    private long batchId;
    @TableField
    private String jobName;
    @TableField
    private String jobStatus;
    @TableField
    private Integer count;
    @TableField
    private Integer errorCount;
    @TableField
    private String errorText;
    @TableField
    private Date startTime;
    @TableField
    private Date endTime;

}
