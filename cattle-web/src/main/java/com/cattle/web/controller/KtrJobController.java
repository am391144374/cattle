package com.cattle.web.controller;

import com.cattle.entity.CattleJob;
import com.cattle.service.api.JobService;
import com.cattle.web.KtrRunFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 执行job信息api
 */
@Controller
public class KtrJobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private KtrRunFactory runFactory;

    @GetMapping("/execute")
    @ResponseBody
    public String executeJob(Integer jobId){
        CattleJob cattleJob = jobService.buildExecuteJobInfo(jobId);
        if(cattleJob != null){
            try {
                runFactory.putRunJob(cattleJob);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "调用成功";
    }

    @GetMapping("/list")
    @ResponseBody
    public CattleJob list(Integer jobId){
        return jobService.buildExecuteJobInfo(jobId);
    }

}
