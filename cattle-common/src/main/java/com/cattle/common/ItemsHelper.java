package com.cattle.common;

import java.util.*;

/**
 * todo 目前在列表页和正文页拼接字段
 * 正文页拼接数据处理
 */
public class ItemsHelper {

    /**
     * 根据正文页的url保存列表页的数据
     */
    public static Map<Long /* batchId */,Map<String /* contentUrl */,LinkedHashMap<String, String> /* list field */>> pageFields = new HashMap<>();

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
