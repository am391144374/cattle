package com.cattle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cattle.entity.kettle.CattleKtrInfo;
import com.cattle.entity.kettle.CattleKtrStep;
import com.cattle.entity.spider.CattleSpiderInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private CattleKtrInfo ktrInfo;
    /**
     * 爬虫 执行信息
     */
    @TableField(exist = false)
    private CattleSpiderInfo spiderInfo;

}
