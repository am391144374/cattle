package com.cattle.service;

import cn.hutool.extra.spring.SpringUtil;
import com.cattle.common.constant.JobStatus;
import com.cattle.service.api.JobService;

public class JobStatusHelper {

    private static JobService jobService;

    public void init(){
        jobService = SpringUtil.getBean(JobService.class);
    }

    public static int updateStatus(long batchId, JobStatus.Status status){
        return jobService.updateJobStatus(batchId,status.getName());
    }

}
