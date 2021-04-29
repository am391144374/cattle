package com.cattle.common.context;

import com.cattle.common.enums.JobStatus;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * process执行上下文
 * @author lsj
 */
public class ProcessContext extends HashMap<String,Object> {

    /** 批次ID */
    private long batchId;
    /** 计划名 */
    private String jobName;

    private int successCount;

    private JobStatus jobStatus;

    private final Set<String> errors = new HashSet<>();

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
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

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public Set<String> getErrors() {
        return errors;
    }

    public void putError(Object errorClass, Exception e){
        String exMsg = String.format(errorClass.getClass().getName() +
                "executor error Exception:%s --- message:%s",e.getClass().getName(),e.getMessage());
        errors.add(exMsg);
        setJobStatus(JobStatus.INTERRUPT);
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

    @Override
    public String toString() {
        return "ProcessContext{" +
                "batchId=" + batchId +
                ", jobName='" + jobName + '\'' +
                ", successCount=" + successCount +
                ", jobStatus=" + jobStatus.getName() +
                '}';
    }
}
