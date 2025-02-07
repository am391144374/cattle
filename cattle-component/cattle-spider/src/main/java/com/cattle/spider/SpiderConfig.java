package com.cattle.spider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattle.common.filter.UrlFilterInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Data
@ToString
@ApiModel
public class SpiderConfig {

    @ApiModelProperty
    private long batchId;
    @ApiModelProperty(notes = "爬虫名")
    private String spiderName;
    @ApiModelProperty(notes = "保存的表名",required = true)
    private String tableName;

    @ApiModelProperty(notes = "列表页正则表达式", required = true)
    private String listRegex;

    @ApiModelProperty(notes = "正文页正则表达式",required = false)
    private String listContentRegex;

    @ApiModelProperty(notes = "入口页", required = true)
    private String entryUrl;

    @ApiModelProperty(notes = "正文页xpath")
    private String contentXpath;

    @ApiModelProperty(notes = "列表页字段规则json字符串")
    private String fieldsJson;

    @ApiModelProperty(notes = "正文页规则json字符串")
    private String contentFieldsJson;

    //爬虫配置项
    @ApiModelProperty(notes = "线程数")
    private Integer threadNum;

    @ApiModelProperty(notes = "每个页面处理完后的睡眠时间 单位秒")
    private Integer sleepTime;

    @ApiModelProperty(notes = "页面下载失败重试次数")
    private Integer retryTimes;

    @ApiModelProperty(notes = "重试睡眠时间 单位秒")
    private Integer retrySleepTime;

    @ApiModelProperty(notes = "页面爬取失败后放回队列的次数")
    private Integer cycleRetryTimes;

    @ApiModelProperty(notes = "下载页面超时时间 单位秒")
    private Integer timeOut;

    /**
     * htmlCleaner 与xpth语法更接近 但比xSoup效率低
     * xSoup 效率较好，但对Xpath 语法支持不够完善 部分语法不支持
     */
    @ApiModelProperty(notes = "xpath 解析选型 0 - htmlCleaner，1 - xSoup")
    private Integer xPathSelection;

    @ApiModelProperty(notes = "url 扫描类型   0 - 全量，1 - 增量")
    private Integer scanUrlType;

    private UrlFilterInterface urlFilterInterface;

    /**
     * key - 字段名
     * val - xpath
     */
    private LinkedHashMap<String, String> fields;

    /**
     * key - 字段名
     * val - xpath
     */
    private LinkedHashMap<String, String> contentFields;

    public LinkedHashMap<String, String> getFields() {
        if(fields == null){
            if(fieldsJson != null){
                fields = parseFields(fieldsJson);
            }
        }
        return fields;
    }

    public LinkedHashMap<String, String> getContentFields() {
        if(contentFields == null){
            if(contentFieldsJson != null){
                contentFields = parseFields(contentFieldsJson);
            }
        }
        return contentFields;
    }

    public LinkedHashMap<String, String> parseFields(String fieldsJson) {
        if (StrUtil.isBlank(fieldsJson)) {
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

    public Set<String> getUpdateWhereColumn(){
        if (StrUtil.isBlank(fieldsJson)) {
            return new HashSet<>();
        }
        Set<String> whereColumns = new HashSet<>();
        JSONArray fields = JSON.parseArray(fieldsJson);
        for (Object fieldObj : fields) {
            JSONObject field = (JSONObject) fieldObj;
            if("是".equals(field.getString("isWhere"))){
                whereColumns.add(field.getString("key"));
            }
        }
        if(StrUtil.isNotBlank(contentFieldsJson)){
            JSONArray contentFields = JSON.parseArray(fieldsJson);
            for (Object fieldObj : contentFields) {
                JSONObject field = (JSONObject) fieldObj;
                if("是".equals(field.getString("isWhere"))){
                    whereColumns.add(field.getString("key"));
                }
            }
        }
        return whereColumns;
    }

}
