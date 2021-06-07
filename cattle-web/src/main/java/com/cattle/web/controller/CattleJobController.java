package com.cattle.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.util.ResponseUtil;
import com.cattle.common.entity.CattleJob;
import com.cattle.service.api.CattleKtrStepInfoService;
import com.cattle.service.api.CattleSpiderInfoService;
import com.cattle.service.api.JobService;
import com.cattle.common.event.JobEventPublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private CattleSpiderInfoService spiderInfoService;
    @Autowired
    private CattleKtrStepInfoService ktrStepInfoService;

    @PostMapping("/execute")
    public Object executeJob(Integer jobId){
        CattleJob cattleJob = jobService.buildExecuteJobInfo(jobId);
        if(cattleJob != null){
            JobEventPublish.publishJobRun(cattleJob);
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

    @PostMapping("/add")
    public Object add(@RequestBody CattleJob cattleJob){
        if(jobService.insert(cattleJob) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("保存失败！");
    }

    @PostMapping("/edit")
    public Object edit(@RequestBody CattleJob cattleJob){
        if(jobService.updateById(cattleJob) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("更新失败！");
    }

    @DeleteMapping("/delete/{jobId}")
    public Object delete(@PathVariable Integer jobId){
        if(jobService.delete(jobId) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("删除失败！");
    }

    @GetMapping("/script/list")
    public Object scriptList(String scriptName){
        switch (scriptName){
            case "kettle":
               return ResponseUtil.success(ktrStepInfoService.selectList(new QueryWrapper<>()));
            case "spider":
               return ResponseUtil.success(spiderInfoService.list());
        }
        return ResponseUtil.fail("调用失败");
    }

}
