package com.cattle.web.controller;

import com.cattle.common.util.ResponseUtil;
import com.cattle.entity.CattleJob;
import com.cattle.service.api.JobService;
import com.cattle.web.CattleRun;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 执行job信息api
 */
@Validated
@Slf4j
@RestController()
@RequestMapping("/job")
public class CattleJobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CattleRun cattleRun;

    @GetMapping("/execute")
    public Object executeJob(Integer jobId){
        CattleJob cattleJob = jobService.buildExecuteJobInfo(jobId);
        if(cattleJob != null){
            try {
                cattleRun.putQueue(cattleJob);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ResponseUtil.success("");
    }

    @GetMapping("/list")
    public Object list(Integer jobId){
        CattleJob cattleJob = jobService.selectById(jobId);
        return ResponseUtil.success(cattleJob);
    }

}
