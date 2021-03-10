package com.cattle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cattle.entity.kettle.KtrStepInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 执行计划
 */
@Data
@TableName("cattle_job")
public class CattleJob {

    @TableId(type = IdType.AUTO)
    private Integer jobId;
    @TableField
    private Long batchId;
    @TableField
    private String jobName;
    @TableField
    /** 脚本类别 kettle脚本、爬虫脚本等 */
    private String scriptName;
    /** 对应脚本类别，关联的脚本信息ID */
    @TableField
    private Integer relateId;
    //todo 数据库ID 移动到 ktrStepInfo
    @TableField
    private Integer inputDbId;
    @TableField
    private Integer outputDbId;
    @TableField
    private String cron;
    //todo 文件列表需要移到 ktrStepInfo 后续再移动
    @TableField
    private String fileList;
    /** 执行状态 */
    @TableField
    private String status;
    @TableField
    private Integer deleted;
    @TableField
    private Date startTime;
    @TableField
    private Date endTime;
    //todo 移动
    @TableField(exist = false)
    private List<KtrStepInfo> stepInfoList;
    //todo 移动
    public String[] getFileList(){
        return fileList.split(",");
    }
}
