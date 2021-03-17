package com.cattle.common;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * todo 目前在列表页和正文页拼接字段
 * 正文页拼接数据处理
 */
public class ItemsHelper {

//    public static Map<Long /* batchId */,Map<String /* listUrl */, List<String> /* contentUrl */>> contentUrls = new HashMap<>();
//
//    public static Map<Long /* batchId */,Map<String /* listUrl */, List<LinkedHashMap<String, String>> /* 字段 */>> fields = new HashMap<>();

    /**
     * 更具正文页的url保存列表页的数据
     */
    public static Map<Long /* batchId */,Map<String /* contentUrl */,LinkedHashMap<String, String> /* list field */>> needBuildContentListField = new HashMap<>();
    /**
     * 维护正文合并字段下标
     */
//    private static Map<Long/* batchId */,Map<String /* listUrl */, AtomicInteger> /* content field add index */> fieldAddIndex = new HashMap<>();

    /**
     * 新增content数据
     * @param batchId
     * @param contentUrl
     * @param fields
     */
    public static void addContentField(long batchId,String contentUrl,List<LinkedHashMap<String, String>> fields){
        if(needBuildContentListField.containsKey(batchId)){
            Map<String,LinkedHashMap<String, String>> listFieldMap = needBuildContentListField.get(batchId);
            if(listFieldMap.containsKey(contentUrl)){
                LinkedHashMap<String, String> listField = listFieldMap.get(contentUrl);
                fields.get(0).forEach((k,v) -> {
                    listField.put(k,v);
                });
            }
        }
    }

    /**
     * 新增列表页面字段
     * @param batchId
     * @param contentUrl
     * @param field
     */
    public static void addListField(long batchId,String contentUrl,LinkedHashMap<String, String> field){
        if(needBuildContentListField.containsKey(batchId)){
            needBuildContentListField.get(batchId).put(contentUrl,field);
        }else{
            Map<String,LinkedHashMap<String, String>> listFieldMap = new HashMap<>();
            listFieldMap.put(contentUrl,field);
            needBuildContentListField.put(batchId,listFieldMap);
        }
    }

    public static List<LinkedHashMap<String, String>> getPageField(long batchId){
        if(!needBuildContentListField.containsKey(batchId)){
            return null;
        }
        List<LinkedHashMap<String,String>> resultList = new ArrayList<>();
        Map<String,LinkedHashMap<String, String>> fieldMaps = needBuildContentListField.get(batchId);
        fieldMaps.forEach((k,v) -> {
           resultList.add(v);
        });
        return resultList;
    }

//    /**
//     * 保存正文页的字段
//     * @param batchId
//     * @param requestUrl
//     * @param pageField
//     */
//    public static void putContentField(Long batchId,String requestUrl,List<LinkedHashMap<String, String>> pageField){
//        if(fields.containsKey(batchId)){
//            Map<String,List<LinkedHashMap<String, String>>> urlFieldMap = fields.get(batchId);
//            if(urlFieldMap.containsKey(requestUrl)){
//                List<LinkedHashMap<String, String>> requestField = urlFieldMap.get(requestUrl);
//                if(requestField.isEmpty()){
//                    urlFieldMap.put(requestUrl,pageField);
//                }else{
//                    int index = fieldAddIndex.get(batchId).get(requestUrl).getAndIncrement();
//                    LinkedHashMap<String,String> field = requestField.get(index);
//                    //因为正文页是匹配的一页数据所以只会有一个数据
//                    LinkedHashMap<String,String> copyField = pageField.get(0);
//                    copyField.forEach((k,v) -> {
//                        field.put(k,v);
//                    });
//                }
//            }
//        }else{
//            Map<String,List<LinkedHashMap<String, String>>> map = new HashMap<>();
//            map.put(requestUrl,pageField);
//            fields.put(batchId,map);
//
//            Map<String,AtomicInteger> indexMap = new HashMap<>();
//            indexMap.put(requestUrl,new AtomicInteger(0));
//            fieldAddIndex.put(batchId,indexMap);
//        }
//    }
//
//    /**
//     * 保存列表页字段
//     * @param batchId
//     * @param requestUrl
//     * @param pageField
//     */
//    public static void putListRequestField(Long batchId,String requestUrl,List<LinkedHashMap<String, String>> pageField){
//        if(fields.containsKey(batchId)){
//            Map<String,List<LinkedHashMap<String, String>>> urlFieldMap = fields.get(batchId);
//            if(urlFieldMap.containsKey(requestUrl)){
//                List<LinkedHashMap<String, String>> requestField = urlFieldMap.get(requestUrl);
//                if(requestField.isEmpty()){
//                    urlFieldMap.put(requestUrl,pageField);
//                }else{
//                    for(int i = 0 ; i < requestField.size() ; i++){
//                        LinkedHashMap<String,String> field = requestField.get(i);
//                        LinkedHashMap<String,String> copyField = pageField.get(i);
//                        copyField.forEach((k,v) -> {
//                            field.put(k,v);
//                        });
//                    }
//                }
//            }
//        }else{
//            Map<String,List<LinkedHashMap<String, String>>> map = new HashMap<>();
//            map.put(requestUrl,pageField);
//            fields.put(batchId,map);
//
//            Map<String,AtomicInteger> indexMap = new HashMap<>();
//            indexMap.put(requestUrl,new AtomicInteger(0));
//            fieldAddIndex.put(batchId,indexMap);
//        }
//    }

//    /**
//     *
//     * @param batchId
//     * @param parentUrl 父类列表url
//     * @param contentUrl
//     */
//    public static void putContentUrl(long batchId,String parentUrl,List<String> contentUrl){
//        if(contentUrls.containsKey(batchId)){
//            Map<String,List<String>> requestMap = contentUrls.get(batchId);
//            if(requestMap.containsKey(parentUrl)){
//                List<String> urls = requestMap.get(parentUrl);
//                contentUrl.forEach(url -> {
//                    urls.add(url);
//                });
//            }
//        }else{
//            Map<String,List<String>> requestMap = new HashMap<>();
//            if(contentUrl == null){
//                requestMap.put(parentUrl,new ArrayList<>());
//            }else{
//                requestMap.put(parentUrl,contentUrl);
//            }
//            contentUrls.put(batchId,requestMap);
//        }
//    }
//
//    /**
//     * 获取父类列表url
//     * @param batchId
//     * @param contentUrl
//     * @return
//     */
//    public static String getParentUrlByContentUrl(long batchId,String contentUrl){
//        String parentUrl = null;
//        if(!contentUrls.containsKey(batchId)){
//            return null;
//        }
//        Map<String,List<String>> parentMaps = contentUrls.get(batchId);
//        for(String url : parentMaps.keySet()){
//            List<String> contentUrls = parentMaps.get(url);
//            if(contentUrls.contains(contentUrl)){
//                parentUrl = url;
//            }
//        }
//        return parentUrl;
//    }

    public static void remove(long batchId){
        needBuildContentListField.remove(batchId);
    }

//    /**
//     * 获取所有字段
//     * @param batchId
//     * @return
//     */
//    public static List<LinkedHashMap<String, String>> getField(long batchId){
//        if(!fields.containsKey(batchId)){
//            return null;
//        }
//        List<LinkedHashMap<String, String>> result = new ArrayList<>();
//        fields.get(batchId).forEach((k,v) -> {
//            v.forEach(fieldMap -> {
//                result.add(fieldMap);
//            });
//        });
//        return result;
//    }
//
//    /**
//     * 当正文页面出现下载超时的时候操作
//     * 因为如果下载页面超时，则当前页面的url下载事件会放到队列的最后端，如果不移动错误结果到最后端，会出现后面下载成功的数据错行，结果不对情况
//     * 源码详见：
//     * @see us.codecraft.webmagic.Spider doCycleRetry方法  437行
//     */
//    public static void errorPutContentField(long batchId,String requestUrl){
//        if(fields.containsKey(batchId)){
//            Map<String,List<LinkedHashMap<String, String>>> urlFieldMap = fields.get(batchId);
//            if(urlFieldMap.containsKey(requestUrl)){
//                List<LinkedHashMap<String, String>> requestField = urlFieldMap.get(requestUrl);
//                if(requestField.size() > 0){
//                    int index = fieldAddIndex.get(batchId).get(requestUrl).get();
//                    LinkedHashMap<String, String> errorFiled = requestField.get(index);
//                    requestField.remove(index);
//                    requestField.add(errorFiled);
//                }
//            }
//        }
//    }
}
