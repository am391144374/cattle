package com.cattle.common;


import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemsHelperTest{

    long batchId = 123456789L;
    String requestUrl = "http://www.baidu.com";

    private LinkedHashMap buildParam(String[] key,String[] val){
        LinkedHashMap linkedHashMap = new LinkedHashMap<>();
        for(int i = 0 ; i < key.length ; i++){
            linkedHashMap.put(key[i],val[i]);
        }
        return linkedHashMap;
    }

    @Test
    public void testPutContentField() {
        List<LinkedHashMap<String,String>> param = new ArrayList<>();
        List<LinkedHashMap<String,String>> param1 = new ArrayList<>();
        param.add(buildParam(new String[]{"title","date"},new String[]{"新的东西","2021-03-16 00:00:00"}));
        param1.add(buildParam(new String[]{"title","date"},new String[]{"好东西","2021-03-18 00:00:00"}));
    }

    @Test
    public void testPutListRequestField() {
        List<LinkedHashMap<String,String>> param = new ArrayList<>();
        param.add(buildParam(new String[]{"url","rate"},new String[]{"www.baidu.com","2.0"}));
        param.add(buildParam(new String[]{"url","rate"},new String[]{"www.google.com","10.0"}));
    }

    @Test
    public void testBuildField(){
        testPutListRequestField();
        testPutContentField();
        List<LinkedHashMap<String,String>> result = ItemsHelper.getPageField(batchId);
        result.forEach(map -> {
            map.forEach((k,v) -> {
               System.out.println(k + "---" + v);
            });
            System.out.println("-------------new line --------");
        });
    }
}