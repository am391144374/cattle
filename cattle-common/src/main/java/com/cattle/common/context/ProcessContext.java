package com.cattle.common.context;

import com.cattle.common.enums.JobStatus;

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
    /** 计划名 */
    private String jobName;

    private Integer count;

    private JobStatus jobStatus;

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
        jobStatus = JobStatus.INTERRUPT;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public List<String> getError(){
        return (List<String>) this.get(EXECUTOR_EXCEPTION);
    }

    public Map<String,Object> getResult(){
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
        setJobStatus(JobStatus.INTERRUPT);
    }

    public boolean isSuccess(){
        return jobStatus.getName().equals(JobStatus.FINISH);
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "ProcessContext{" +
                "batchId=" + batchId +
                ", jobName='" + jobName + '\'' +
                ", count=" + count +
                ", jobStatus=" + jobStatus.getName() +
                '}';
    }
}
