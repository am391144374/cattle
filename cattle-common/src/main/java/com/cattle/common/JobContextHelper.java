package com.cattle.common;


import com.cattle.common.context.ProcessContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 随时修改job执行数据与执行状态
 */
public class JobContextHelper {

    final static private Map<Long, ProcessContext> jobContext = new ConcurrentHashMap<>();

    public static ProcessContext getContext(long batchId){
        return jobContext.get(batchId);
    }

    public static void setJobContext(long batchId, ProcessContext processContext){
        jobContext.put(batchId,processContext);
    }

    /**
     * job执行完一定要删除，否则可能占用很多内存
     * @param batchId
     */
    public static void remove(long batchId){
        jobContext.remove(batchId);
    }

    public static Map<Long, ProcessContext> getAllContext(){
        return jobContext;
    }

}
