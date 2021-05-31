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
    public static Map<Long /* batchId */,Map<String /* url */,List<LinkedHashMap<String, String>>/* list field */>> pageFields = new HashMap<>();

    // todo 三级缓存逻辑需要再想想，如何保证多线程情况下快速 ”升级“
    /**
     * 中间缓冲层
     */
    public static Map<Long /* batchId */,Map<String /* url */,List<LinkedHashMap<String, String>>/* list field */>> cachePageFields = new HashMap<>();

    /** 可直接保存的页数据 */
    public static Map<Long /* batchId */,Map<String /* url */,List<LinkedHashMap<String, String>>/* list field */>> storagePageFields = new HashMap<>();

    /**
     * 新增单条数据
     * @param batchId
     * @param url
     * @param field
     */
    public static void addField(long batchId,String url,LinkedHashMap<String, String> field){
        if(pageFields.containsKey(batchId)){
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = pageFields.get(batchId);
            if(listFieldMap.containsKey(url)){
                List<LinkedHashMap<String, String>> listField = listFieldMap.get(url);

                for(int i = 0 ; i < listField.size() ; i++){
                    listField.get(i).putAll(field);
                }
            }else{
                List<LinkedHashMap<String, String>> linkedHashMaps = new ArrayList<>();
                linkedHashMaps.add(field);
                listFieldMap.put(url,linkedHashMaps);
            }
        }else{
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = new HashMap<>();
            List<LinkedHashMap<String, String>> linkedHashMaps = new ArrayList<>();
            linkedHashMaps.add(field);
            listFieldMap.put(url,linkedHashMaps);
            pageFields.put(batchId,listFieldMap);
        }
    }

    /**
     * 添加正文页数据 升级到 cachePageFields
     * 触发 升级
     * @param batchId
     * @param url
     * @param field
     */
    public static void addContentField(long batchId,String url,LinkedHashMap<String, String> field){
        if(pageFields.containsKey(batchId)){
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = pageFields.get(batchId);
            if(listFieldMap.containsKey(url)){
                List<LinkedHashMap<String, String>> listField = listFieldMap.get(url);
                for(int i = 0 ; i < listField.size() ; i++){
                    listField.get(i).putAll(field);
                }
            }else{
                List<LinkedHashMap<String, String>> linkedHashMaps = new ArrayList<>();
                linkedHashMaps.add(field);
                listFieldMap.put(url,linkedHashMaps);
            }
        }else{
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = new HashMap<>();
            List<LinkedHashMap<String, String>> linkedHashMaps = new ArrayList<>();
            linkedHashMaps.add(field);
            listFieldMap.put(url,linkedHashMaps);
            pageFields.put(batchId,listFieldMap);
        }
        upgradeToCachePageFields(batchId,url);
    }

    /**
     * 全量新增
     * 触发 升级
     * @param batchId
     * @param url
     * @param fields
     */
    public static void addFields(long batchId,String url,List<LinkedHashMap<String, String>> fields){
        if(pageFields.containsKey(batchId)){
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = pageFields.get(batchId);
            if(listFieldMap.containsKey(url)){
                List<LinkedHashMap<String, String>> listField = listFieldMap.get(url);
                listField.addAll(fields);
            }else{
                List<LinkedHashMap<String, String>> linkedHashMaps = new ArrayList<>();
                linkedHashMaps.addAll(fields);
                listFieldMap.put(url,linkedHashMaps);
            }
        }else{
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = new HashMap<>();
            listFieldMap.put(url,fields);
            pageFields.put(batchId,listFieldMap);
        }
        upgradeToCachePageFields(batchId,url);
    }

    public static List<LinkedHashMap<String, String>> getPageField(long batchId){
        if(!pageFields.containsKey(batchId)){
            return null;
        }
        upgradeToStoragePageFields(batchId);
        List<LinkedHashMap<String,String>> resultList = new ArrayList<>();
        Map<String,List<LinkedHashMap<String, String>>> fieldMaps = storagePageFields.get(batchId);
        if(fieldMaps == null || fieldMaps.size() <= 0){
            return null;
        }
        fieldMaps.forEach((k,v) -> {
           resultList.addAll(v);
        });
        return resultList;
    }

    public static void removeAll(long batchId){
        pageFields.remove(batchId);
        cachePageFields.remove(batchId);
        storagePageFields.remove(batchId);
    }

    public static void removeStorage(long batchId){
        storagePageFields.remove(batchId);
    }

    private static void upgradeToCachePageFields(Long batchId,String url){
        Map<String,List<LinkedHashMap<String, String>>> cache;
        if(!cachePageFields.containsKey(batchId)){
            cache = new HashMap<>();
            cachePageFields.put(batchId,cache);
        }else{
            cache = cachePageFields.get(batchId);
        }
        Map<String, List<LinkedHashMap<String, String>>> listFieldMap = pageFields.get(batchId);
        if (listFieldMap.containsKey(url)) {
            synchronized (batchId){
                List<LinkedHashMap<String, String>> listField = listFieldMap.get(url);
                cache.put(url, listField);
                listFieldMap.remove(url);
            }
        }
    }

    private static void upgradeToStoragePageFields(Long batchId){
        Map<String,List<LinkedHashMap<String, String>>> storage;
        if(!storagePageFields.containsKey(batchId)){
            storage = new HashMap<>();
            storagePageFields.put(batchId,storage);
        }else{
            storage = storagePageFields.get(batchId);
        }
        synchronized (batchId){
            Map<String,List<LinkedHashMap<String, String>>> listFieldMap = cachePageFields.get(batchId);
            if(listFieldMap != null){
                storage.putAll(listFieldMap);
                storagePageFields.put(batchId,storage);
                cachePageFields.remove(batchId);
            }
        }
    }

}
