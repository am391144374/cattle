package com.cattle.common.context;

import com.cattle.common.enums.JobStatus;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * process执行上下文
 * @author lsj
 */
@Data
public class ProcessContext extends HashMap<String,Object> {

    /** 批次ID */
    private long batchId;
    /** 计划名 */
    private String jobName;

    private Integer jobId;

    /** 默认为0 */
    private int successCount = 0;

    private JobStatus jobStatus;

    private String scriptType;

    private final Set<String> errors = new HashSet<>();

    private final Set<String> warns = new HashSet<>();

    public void addSuccessCount(int addNum){
        successCount += addNum;
    }


    public void putError(Object errorClass, Exception e){
        String exMsg = String.format(errorClass.getClass().getName() +
                "executor error Exception:%s --- message:%s",e.getClass().getName(),e.getMessage());
        errors.add(exMsg);
        setJobStatus(JobStatus.INTERRUPT);
    }

    public void putWarn(Object errorClass, Exception e){
        String warnMsg = String.format(errorClass.getClass().getName() +
                "executor warn Exception:%s --- message:%s",e.getClass().getName(),e.getMessage());
        warns.add(warnMsg);
    }

}
