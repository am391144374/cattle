package com.cattle.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.util.ResponseUtil;
import com.cattle.entity.CattleJob;
import com.cattle.service.api.JobService;
import com.cattle.web.CattleRun;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/execute")
    public Object executeJob(Integer jobId){
        CattleJob cattleJob = jobService.buildExecuteJobInfo(jobId);
        if(cattleJob != null){
            try {
                cattleRun.putQueue(cattleJob);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ResponseUtil.success(0,"执行成功",null);
    }

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1",required = false) Integer offset,
                       @RequestParam(defaultValue = "10",required = false) Integer limit,
                       @RequestParam(required = false) String queryJson){
        Map<String,Object> queryMap = null;
        if(queryJson != null){
            queryMap = JSONObject.parseObject(queryJson, HashMap.class);
        }
        IPage page = jobService.selectPage(offset,limit,queryMap);
        return ResponseUtil.success(page);
    }

}
