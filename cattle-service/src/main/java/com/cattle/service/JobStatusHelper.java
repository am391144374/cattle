package com.cattle.service;

import cn.hutool.extra.spring.SpringUtil;
import com.cattle.service.api.JobService;

public class JobStatusHelper {

    private static JobService jobService;

    public void init(){
        jobService = SpringUtil.getBean(JobService.class);
    }

    public static int updateStatus(long batchId,Status status){
        return jobService.updateJobStatus(batchId,status.getName());
    }

    public enum Status{
        START("start"),
        RUNNING("running"),
        WAIT("wait"),
        STOP("stop"),
        FINISH("finish");

        private String name;

        Status(String name){
            this.name = name;
        }

        String getName(){
            return this.name;
        }
    }

}
