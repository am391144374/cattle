package com.cattle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cattle.entity.kettle.KtrStep;
import com.cattle.entity.spider.SpiderInfoBO;
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
    /** 脚本类别 kettle脚本、爬虫脚本等  kettle  spider  */
    private String scriptType;
    /** 对应脚本类别，关联的脚本信息ID  step_id  spider_id*/
    @TableField
    private Integer relateId;
    /** 用于kettle脚本文件地址 kettle执行需要有个默认的  以 .ktr结尾的文件作为模板*/
    @TableField
    private String scriptPath;
    @TableField
    private Date startTime;
    @TableField
    private Date endTime;
    @TableField
    private String tableName;

    /**
     * kettle 执行信息
     */
    @TableField(exist = false)
    private List<KtrStep> stepInfoList;
    /**
     * 爬虫 执行信息
     */
    @TableField(exist = false)
    private SpiderInfoBO configurable;

}
