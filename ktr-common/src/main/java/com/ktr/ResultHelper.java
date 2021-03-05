package com.ktr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lsj
 * 变量结果处理
 */
public class ResultHelper {

    private static final String EXECUTOR_STATUS = "executorStatus";
    private static final String EXECUTOR_EXCEPTION = "exceptionException";

    /**
     *
     * @param variables 变量
     * @param errorClass 出错类
     * @param e 异常
     */
    public static void setException(Map<String,Object> variables,Object errorClass,Exception e){
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
        setExecutorStatus(variables,false);
    }

    public static boolean isSuccess(Map<String,Object> variables){
        return (boolean) variables.getOrDefault(EXECUTOR_STATUS,true);
    }

    public static boolean setExecutorStatus(Map<String,Object> variables,boolean status){
        return variables.put(EXECUTOR_STATUS,status) != null ? true : false;
    }

    public static List<String> getException(Map<String,Object> variables){
        return (List<String>) variables.get(EXECUTOR_EXCEPTION);
    }

}
