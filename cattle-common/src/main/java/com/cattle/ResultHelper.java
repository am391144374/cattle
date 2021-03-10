package com.cattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lsj
 * 变量结果处理
 */
public class ResultHelper {

    private static final String EXECUTOR_STATUS = "executorStatus";
    private static final String EXECUTOR_EXCEPTION = "exceptionException";

    /**
     * 缓存结果信息
     */
    private static Map<Long,Map<String,Object>> variablesHolder = new ConcurrentHashMap<>();
    /**
     *
     * @param batchId 批次ID
     * @param errorClass 出错类
     * @param e 异常
     */
    public static void setException(Long batchId,Object errorClass,Exception e){
        Map<String,Object> variables = createMap(batchId);
        List<String> exList;
        String exMsg = String.format(errorClass.getClass().getName() +
                "executor error Exception:%s --- message:%s",e.getClass().getName(),e.getMessage());
        if(variables.containsKey(EXECUTOR_EXCEPTION)){
            exList = (List<String>) variables.get(EXECUTOR_EXCEPTION);
        }else{
            exList = new ArrayList<>();
        }
        exList.add(exMsg);
        variables.put(EXECUTOR_EXCEPTION,exList);
        setExecutorStatus(batchId,false);
    }

    public static boolean isSuccess(long batchId){
        Map<String,Object> variables = createMap(batchId);
        return (boolean) variables.getOrDefault(EXECUTOR_STATUS,true);
    }

    public static boolean setExecutorStatus(long batchId,boolean status){
        Map<String,Object> variables = createMap(batchId);
        return variables.put(EXECUTOR_STATUS,status) != null ? true : false;
    }

    public static List<String> getException(long batchId){
        Map<String,Object> variables = createMap(batchId);
        return (List<String>) variables.get(EXECUTOR_EXCEPTION);
    }

    public static Map<String,Object> createMap(long batchId){
        Map<String,Object> variables;
        if(variablesHolder.containsKey(batchId)){
            variables = variablesHolder.get(batchId);
        }else{
            variables = new HashMap<>();
        }
        return variables;
    }

}
