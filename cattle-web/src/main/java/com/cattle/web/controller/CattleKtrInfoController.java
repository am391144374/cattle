package com.cattle.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.util.ResponseUtil;
import com.cattle.entity.CattleKtrField;
import com.cattle.entity.CattleKtrInfo;
import com.cattle.entity.CattleKtrStep;
import com.cattle.service.api.CattleKtrInfoService;
import com.cattle.service.api.CattleKtrStepFieldService;
import com.cattle.service.api.CattleKtrStepInfoService;
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
    private CattleKtrStepInfoService ktrStepInfoService;

    @Autowired
    private CattleKtrStepFieldService ktrStepFieldService;

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1",required = false) Integer offset,
                       @RequestParam(defaultValue = "10",required = false) Integer limit,
                       @RequestParam(required = false) String queryJson){
        Map<String,Object> queryMap = null;
        if(queryJson != null){
            queryMap = JSONObject.parseObject(queryJson, HashMap.class);
        }
        IPage page = ktrInfoService.selectPage(offset,limit,queryMap);
        return ResponseUtil.success(page);
    }

    @PostMapping("/add")
    public Object add(@RequestBody CattleKtrInfo cattleKtrInfo){
        if(ktrInfoService.insert(cattleKtrInfo) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("add error");
    }

    @PostMapping("/edit")
    public Object edit(@NotNull @RequestBody CattleKtrInfo cattleKtrInfo){
        if(ktrInfoService.updateById(cattleKtrInfo) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("edit error");
    }

    @GetMapping("/detail")
    public Object detail(@NotNull Integer id){
        CattleKtrInfo cattleKtrInfo = ktrInfoService.selectById(id);
        return ResponseUtil.success(cattleKtrInfo);
    }

    @DeleteMapping("/remove")
    public Object remove(@NotNull Integer id){
        if(ktrInfoService.delete(id) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("不存在的Id");
    }

    @GetMapping("/step/detail")
    public Object stepDetail(@NotNull Integer stepId){
        CattleKtrStep cattleKtrStep = ktrStepInfoService.selectStepInfoById(stepId);
        return ResponseUtil.success(cattleKtrStep);
    }

    @GetMapping("/step/list")
    public Object stepList(@NotNull Integer ktrInfoId,
                            @RequestParam(required = false,defaultValue = "1") Integer offset,
                           @RequestParam(required = false,defaultValue = "10") Integer limit){
        IPage<CattleKtrStep> data = ktrStepInfoService.selectStepInfoByKtrInfoIdPage(ktrInfoId,offset,limit);
        return ResponseUtil.success(data);
    }

    @PostMapping("/step/add")
    public Object stepAdd(@RequestBody CattleKtrStep cattleKtrStep){
        if(ktrStepInfoService.insert(cattleKtrStep) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("add error");
    }

    @PostMapping("/step/edit")
    public Object stepEdit(@NotNull @RequestBody CattleKtrStep cattleKtrStep){
        if(ktrStepInfoService.updateById(cattleKtrStep) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("edit error");
    }

    @DeleteMapping("/step/remove")
    public Object stepRemove(@NotNull Integer stepId){
        if(ktrStepInfoService.delete(stepId) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("不存在的Id");
    }

    @GetMapping("/step/field/list")
    public Object stepFieldList(@NotNull Integer stepId,
                           @RequestParam(required = false,defaultValue = "1") Integer offset,
                           @RequestParam(required = false,defaultValue = "10") Integer limit){
        IPage<CattleKtrField> data = ktrStepFieldService.selectFieldListPageByStepId(stepId,offset,limit);
        return ResponseUtil.success(data);
    }

    @PostMapping("/step/field/add")
    public Object stepFieldAdd(@RequestBody CattleKtrField cattleKtrField){
        if(ktrStepFieldService.insert(cattleKtrField) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("add error");
    }

    @PostMapping("/step/field/edit")
    public Object stepFieldEdit(@NotNull @RequestBody CattleKtrField cattleKtrField){
        if(ktrStepFieldService.updateById(cattleKtrField) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("edit error");
    }

    @DeleteMapping("/step/field/remove")
    public Object stepFieldRemove(@NotNull Integer fieldId){
        if(ktrStepFieldService.delete(fieldId) > 0){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("不存在的Id");
    }

    @GetMapping("/step/field/detail")
    public Object stepFieldDetail(@NotNull Integer fieldId){
        CattleKtrField cattleKtrStep = ktrStepFieldService.selectFieldById(fieldId);
        return ResponseUtil.success(cattleKtrStep);
    }
}
