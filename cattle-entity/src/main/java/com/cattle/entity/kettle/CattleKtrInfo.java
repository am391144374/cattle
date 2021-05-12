package com.cattle.entity.kettle;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("cattle_ktr_info")
public class CattleKtrInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 脚本名 */
    @TableField
    private String ktrName;
    /** 用于kettle脚本文件地址 kettle执行需要有个默认的  以 .ktr结尾的文件作为模板*/
    @TableField
    private String scriptFile;
    @TableField
    private String tableName;
    /**
     * 脚本执行方式
     * normal-普通模式   即只执行脚本，不对脚本进行改造
     * edit-编辑模式     即对脚本进行编辑，然后再执行
     */
    @TableField
    private String processType;
    @TableLogic(value = "0",delval = "1")
    @TableField
    private Integer deleted;
    @TableField
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE,update = "now()")
    private Date updateTime;
    /** 备注 */
    @TableField
    private String remark;

    @TableField(exist = false)
    private List<CattleKtrStep> stepInfoList;
}
