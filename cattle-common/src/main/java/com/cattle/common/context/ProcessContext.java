package com.cattle.common.context;

import com.cattle.common.enums.JobStatus;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private AtomicInteger count = new AtomicInteger(0);

    private JobStatus jobStatus;

    private final String CONNECTION_NAME = "yearbook";

    private final Set<String> errors = new HashSet<>();

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

    public Set<String> getError(){
        return errors;
    }

    public Map<String,Object> getResult(){
        return this;
    }

    public void putError(Object errorClass,Exception e){
        String exMsg = String.format(errorClass.getClass().getName() +
                "executor error Exception:%s --- message:%s",e.getClass().getName(),e.getMessage());
        errors.add(exMsg);
        setJobStatus(JobStatus.INTERRUPT);
    }

    public boolean isSuccess(){
        return jobStatus.getName().equals(JobStatus.FINISH);
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getCount() {
        return count.get();
    }

    public int increment(Integer num){
        return count.addAndGet(num);
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
