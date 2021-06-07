package com.cattle.common;


import com.cattle.common.context.ProcessContent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 随时修改job执行数据与执行状态
 */
public class JobContextHelper {

    final static private Map<Long, ProcessContent> jobContext = new ConcurrentHashMap<>();

    public static ProcessContent getContext(long batchId){
        return jobContext.get(batchId);
    }

    public static void setJobContext(long batchId, ProcessContent processContent){
        jobContext.put(batchId, processContent);
    }

    /**
     * job执行完一定要删除，否则可能占用很多内存
     * @param batchId
     */
    public static void remove(long batchId){
        jobContext.remove(batchId);
    }

    public static Map<Long, ProcessContent> getAllContext(){
        return jobContext;
    }

}
