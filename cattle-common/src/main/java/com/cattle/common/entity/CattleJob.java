package com.cattle.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 执行计划
 */
@Data
@TableName("cattle_job_info")
public class CattleJob {

    @TableId(type = IdType.AUTO)
    private Integer jobId;
    @TableField
    private String jobName;
    @TableField
    /** 脚本类别 kettle脚本、爬虫脚本等  kettle  spider  */
    private String scriptType;
    /** 关联的脚本ID */
    @TableField
    private Integer relateId;
    @TableField
    private Date startTime;
    @TableField
    private Date endTime;

    @TableField(exist = false)
    private long batchId;

    /**
     * kettle 执行信息
     */
    @TableField(exist = false)
    private CattleKtrStep stepInfo;

    /**
     * 爬虫 执行信息
     */
    @TableField(exist = false)
    private CattleSpiderInfo spiderInfo;

    @TableField(exist = false)
    /** 数据存储 */
    private Map<String /* dbName */,CattleKtrDb> dataBaseMetas;

}
