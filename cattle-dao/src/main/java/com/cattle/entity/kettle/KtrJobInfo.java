package com.cattle.entity.kettle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 执行计划
 */
@Data
@TableName("ktr_job_info")
public class KtrJobInfo {

    @TableId(type = IdType.AUTO)
    private Integer jobId;
    @TableField
    private String jobName;
    @TableField
    /** 脚本类别 kettle脚本、爬虫脚本等 */
    private String scriptName;
    /** 对应脚本类别，关联的脚本信息ID */
    @TableField
    private Integer relateId;
    @TableField
    private Integer inputDbId;
    @TableField
    private Integer outputDbId;
    @TableField
    private String cron;
    @TableField
    private String executeType;
    @TableField
    private Integer retryNum;
    @TableField
    private Integer timeout;
    @TableField
    private Integer timeoutUnit;
    @TableField
    private String fileList;
    @TableField
    private Integer deleted;
    @TableField(exist = false)
    private List<KtrStepInfo> stepInfoList;

    public String[] getFileList(){
        return fileList.split(",");
    }
}
