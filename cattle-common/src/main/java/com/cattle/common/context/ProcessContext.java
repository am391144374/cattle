package com.cattle.common.context;

import com.cattle.common.constant.JobStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * process执行上下文
 * @author lsj
 */
public class ProcessContext extends HashMap<String,Object> {

    private static final String EXECUTOR_EXCEPTION = "exceptionException";
    /** 批次ID */
    private long batchId;

    private JobStatus.Status jobStatus;

    private final String CONNECTION_NAME = "yearbook";

    public String getConnectionName(){
        return CONNECTION_NAME;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    /**
     * 中断正在执行job
     */
    public void interruptJob(){
        jobStatus = JobStatus.Status.INTERRUPT;
    }

    public JobStatus.Status getJobStatus() {
        return jobStatus;
    }

    public List<String> getError(){
        return getException();
    }

    public Map<String,Object> getResult(long batchId){
        return this;
    }

    public void putError(Object errorClass,Exception e){
        List<String> exList;
        String exMsg = String.format(errorClass.getClass().getName() +
                "executor error Exception:%s --- message:%s",e.getClass().getName(),e.getMessage());
        if(containsKey(EXECUTOR_EXCEPTION)){
            exList = (List<String>) this.get(EXECUTOR_EXCEPTION);
        }else{
            exList = new ArrayList<>();
        }
        exList.add(exMsg);
        put(EXECUTOR_EXCEPTION,exList);
        setJobStatus(JobStatus.Status.INTERRUPT);
    }

    public boolean isSuccess(){
        return jobStatus.getName().equals(JobStatus.Status.FINISH);
    }

    public List<String> getException(){
        return (List<String>) this.get(EXECUTOR_EXCEPTION);
    }

    public void setJobStatus(JobStatus.Status jobStatus) {
        this.jobStatus = jobStatus;
    }
}
