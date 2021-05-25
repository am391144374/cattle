package com.cattle.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.cattle.common.filter.RedisBloomFilter;
import com.cattle.common.filter.UrlFilterInterface;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;

@Data
@ApiModel
@TableName("cattle_spider_info")
public class CattleSpiderInfo {

    @ApiModelProperty
    @TableId(type= IdType.AUTO)
    private Integer spiderId;

    @ApiModelProperty(notes = "爬虫名")
    @TableField
    private String spiderName;

    @ApiModelProperty(notes = "保存的表名",required = true)
    @TableField
    private String tableName;

    @ApiModelProperty(notes = "列表页正则表达式", required = true)
    @TableField
    private String listRegex;

    @ApiModelProperty(notes = "入口页", required = true)
    @TableField
    private String entryUrl;

    @ApiModelProperty(notes = "正文页xpath")
    @TableField
    private String contentXpath;

    @ApiModelProperty(notes = "列表页字段规则json字符串")
    @TableField
    private String fieldsJson;

    @ApiModelProperty(notes = "正文页规则json字符串")
    @TableField
    private String contentFieldsJson;

    //爬虫配置项
    @TableField
    @ApiModelProperty(notes = "线程数")
    private Integer threadNum;

    @ApiModelProperty(notes = "每个页面处理完后的睡眠时间 单位秒")
    @TableField
    private Integer sleepTime;

    @ApiModelProperty(notes = "页面下载失败重试次数")
    @TableField
    private Integer retryTimes;

    @ApiModelProperty(notes = "重试睡眠时间 单位秒")
    @TableField
    private Integer retrySleepTime;

    @ApiModelProperty(notes = "页面爬取失败后放回队列的次数")
    @TableField
    private Integer cycleRetryTimes;

    @ApiModelProperty(notes = "下载页面超时时间 单位秒")
    @TableField
    private Integer timeOut;

    /**
     * htmlCleaner 与xpth语法更接近 但比xSoup效率低
     * xSoup 效率较好，但对Xpath 语法支持不够完善 部分语法不支持
     */
    @ApiModelProperty(notes = "xpath 解析选型 0 - htmlCleaner，1 - xSoup")
    @TableField
    private Integer xPathSelection;

    @ApiModelProperty(notes = "url 扫描类型   0 - 全量，1 - 增量")
    @TableField
    private Integer scanUrlType;

    @TableField
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(update="now()",fill = FieldFill.UPDATE)
    private Date modifyTime;

    /**
     * key - 字段名
     * val - xpath
     */
    @TableField(exist = false)
    private LinkedHashMap<String, String> fields;

    /**
     * key - 字段名
     * val - xpath
     */
    @TableField(exist = false)
    private LinkedHashMap<String, String> contentFields;

    public LinkedHashMap<String, String> getFields() {
        if(fields == null){
            if(fieldsJson != null){
                fields = parseFields(fieldsJson);
            }
        }
        return fields;
    }

    public LinkedHashMap<String, String> parseFields(String fieldsJson) {
        if (Strings.isNullOrEmpty(fieldsJson)) {
            return new LinkedHashMap<>();
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        JSONArray fields = JSON.parseArray(fieldsJson);
        for (Object fieldObj : fields) {
            JSONObject field = (JSONObject) fieldObj;
            map.put(field.get("key").toString(), field.get("value").toString());
        }
        return map;
    }

    public UrlFilterInterface getUrlFilter(){
        if("1".equals(scanUrlType)){
            return new RedisBloomFilter();
        }
        return null;
    }

}
