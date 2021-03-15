package com.cattle.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 正文页拼接数据处理
 */
public class ItemsHelper {

    public Map<Long /* batchId */,Map<String /* listUrl */, List<String> /* contentUrl */>> contextUrls = new HashMap<>();

    public Map<Long /* batchId */,Map<String /* listUrl */, List<LinkedHashMap<String, String>>>> contextField = new HashMap<>();

    public void putField(Long batchId,String requestUrl,List<LinkedHashMap<String, String>> fields){
        if(contextField.containsKey(batchId)){
            Map<String,List<LinkedHashMap<String, String>>> urlFieldMap = contextField.get(batchId);
            if(urlFieldMap.containsKey(requestUrl)){
                List<LinkedHashMap<String, String>> requestField = urlFieldMap.get(requestUrl);
                if(requestField.isEmpty()){
                    urlFieldMap.put(requestUrl,fields);
                }else{
                    for(int i = 0 ; i < requestField.size() ; i++){
                        LinkedHashMap<String,String> field = requestField.get(i);
                        LinkedHashMap<String,String> copyField = fields.get(i);
                        copyField.forEach((k,v) -> {
                            field.put(k,v);
                        });
                    }
                }
            }
        }else{
            Map<String,List<LinkedHashMap<String, String>>> map = new HashMap<>();
            map.put(requestUrl,fields);
            contextField.put(batchId,map);
        }
    }

}
