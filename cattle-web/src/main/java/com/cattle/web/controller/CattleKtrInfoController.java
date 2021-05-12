package com.cattle.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.util.ResponseUtil;
import com.cattle.entity.kettle.CattleKtrInfo;
import com.cattle.entity.kettle.CattleKtrStep;
import com.cattle.service.api.kettle.CattleKtrInfoService;
import com.cattle.service.api.kettle.KtrStepInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Valid
@RequestMapping("/ktr")
public class CattleKtrInfoController {

    @Autowired
    private CattleKtrInfoService ktrInfoService;

    @Autowired
    private KtrStepInfoService ktrStepInfoService;

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1",required = false) Integer offset,
                       @RequestParam(defaultValue = "10",required = false) Integer limit,
                       @RequestParam(required = false) String queryJson){
        Map<String,Object> queryMap = null;
        if(queryJson != null){
            queryMap = JSONObject.parseObject(queryJson, HashMap.class);
        }
        IPage page = ktrInfoService.selectPage(offset,limit,queryMap);
        return ResponseUtil.defaultSuccess(page);
    }

    @PostMapping("/add")
    public Object add(@RequestBody CattleKtrInfo cattleKtrInfo){
        if(ktrInfoService.insert(cattleKtrInfo) > 0){
            return ResponseUtil.defaultSuccess("");
        }
        return ResponseUtil.defaultFail("add error");
    }

    @PostMapping("/edit")
    public Object edit(@NotNull @RequestBody CattleKtrInfo cattleKtrInfo){
        if(ktrInfoService.updateById(cattleKtrInfo) > 0){
            return ResponseUtil.defaultSuccess("");
        }
        return ResponseUtil.defaultFail("edit error");
    }

    @GetMapping("/detail")
    public Object detail(@NotNull Integer id){
        CattleKtrInfo cattleKtrInfo = ktrInfoService.selectById(id);
        return ResponseUtil.defaultSuccess(cattleKtrInfo);
    }

    @DeleteMapping("/remove")
    public Object remove(@NotNull Integer id){
        if(ktrInfoService.delete(id) > 0){
            return ResponseUtil.defaultSuccess("");
        }
        return ResponseUtil.defaultFail("不存在的Id");
    }

    @GetMapping("/step/list")
    public Object stepList(@NotNull Integer ktrInfoId,
                            @RequestParam(required = false,defaultValue = "1") Integer offset,
                           @RequestParam(required = false,defaultValue = "10") Integer limit){
        IPage<CattleKtrStep> data = ktrStepInfoService.selectStepInfoByKtrInfoIdPage(ktrInfoId,offset,limit);
        return ResponseUtil.defaultSuccess(data);
    }

    @PostMapping("/step/add")
    public Object stepAdd(@RequestBody CattleKtrStep cattleKtrStep){
        if(ktrStepInfoService.insert(cattleKtrStep) > 0){
            return ResponseUtil.defaultSuccess("");
        }
        return ResponseUtil.defaultFail("add error");
    }

    @PostMapping("/step/edit")
    public Object stepEdit(@NotNull @RequestBody CattleKtrStep cattleKtrStep){
        if(ktrStepInfoService.updateById(cattleKtrStep) > 0){
            return ResponseUtil.defaultSuccess("");
        }
        return ResponseUtil.defaultFail("edit error");
    }

    @DeleteMapping("/step/remove")
    public Object stepRemove(@NotNull Integer stepId){
        if(ktrStepInfoService.delete(stepId) > 0){
            return ResponseUtil.defaultSuccess("");
        }
        return ResponseUtil.defaultFail("不存在的Id");
    }
}
