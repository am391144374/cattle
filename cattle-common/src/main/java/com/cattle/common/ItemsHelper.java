package com.cattle.common;

import java.util.*;

/**
 *
 * 页数据处理
 *
 * 设置三级缓存
 * 一级缓存为了拼接正文页字段与列表页字段匹配正确
 * 二级缓存为拼接完成可保存数据 做中间缓存层，防止保存完成后删除缓存导致把为保存数据直接保存的问题
 * 三级缓存为可保存数据，保存完后直接清除，不占用内存
 *
 */
public class ItemsHelper {

    /**
     * 根据正文页的url保存列表页的数据
     */
    public static Map<Long /* batchId */,Map<String /* contentUrl */,LinkedHashMap<String, String> /* list field */>> pageFields = new HashMap<>();

    /**
     * 中间缓冲层
     */
    public static Map<Long /* batchId */,Map<String /* contentUrl */,LinkedHashMap<String, String> /* list field */>> cachePageFields = new HashMap<>();

    /** 可直接保存的页数据 */
    public static Map<Long /* batchId */,Map<String /* contentUrl */,LinkedHashMap<String, String> /* list field */>> storagePageFields = new HashMap<>();

    /**
     * 新增content数据
     * @param batchId
     * @param contentUrl
     * @param fields
     */
    public static void addContentField(long batchId,String contentUrl,List<LinkedHashMap<String, String>> fields){
        if(pageFields.containsKey(batchId)){
            Map<String,LinkedHashMap<String, String>> listFieldMap = pageFields.get(batchId);
            if(listFieldMap.containsKey(contentUrl)){
                LinkedHashMap<String, String> listField = listFieldMap.get(contentUrl);
                fields.get(0).forEach((k,v) -> {
                    listField.put(k,v);
                });
            }else{
                listFieldMap.put(contentUrl,fields.get(0));
            }
        }else{
            Map<String,LinkedHashMap<String, String>> listFieldMap = new HashMap<>();
            listFieldMap.put(contentUrl,fields.get(0));
            pageFields.put(batchId,listFieldMap);
        }
    }

    /**
     * 新增列表页面字段
     * @param batchId
     * @param contentUrl
     * @param field
     */
    public static void addListField(long batchId,String contentUrl,LinkedHashMap<String, String> field){
        if(pageFields.containsKey(batchId)){
            pageFields.get(batchId).put(contentUrl,field);
        }else{
            Map<String,LinkedHashMap<String, String>> listFieldMap = new HashMap<>();
            listFieldMap.put(contentUrl,field);
            pageFields.put(batchId,listFieldMap);
        }
    }

    public static List<LinkedHashMap<String, String>> getPageField(long batchId){
        if(!pageFields.containsKey(batchId)){
            return null;
        }
        List<LinkedHashMap<String,String>> resultList = new ArrayList<>();
        Map<String,LinkedHashMap<String, String>> fieldMaps = pageFields.get(batchId);
        fieldMaps.forEach((k,v) -> {
           resultList.add(v);
        });
        return resultList;
    }

    public static void remove(long batchId){
        pageFields.remove(batchId);
    }

}
