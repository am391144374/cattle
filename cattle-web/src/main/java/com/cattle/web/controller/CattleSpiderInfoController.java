package com.cattle.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.util.ResponseUtil;
import com.cattle.entity.CattleSpiderInfo;
import com.cattle.service.api.CattleSpiderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/spider")
public class CattleSpiderInfoController{

    @Autowired
    private CattleSpiderInfoService spiderInfoService;

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1",required = false) Integer offset,
                       @RequestParam(defaultValue = "10",required = false) Integer limit,
                       @RequestParam(required = false) String queryJson){
        Map<String,Object> queryMap = null;
        if(queryJson != null){
            queryMap = JSONObject.parseObject(queryJson, HashMap.class);
        }
        IPage page = spiderInfoService.selectPageList(offset,limit,queryMap);
        return ResponseUtil.success(page);
    }

    @PostMapping("/add")
    public Object add(@RequestBody CattleSpiderInfo cattleSpiderInfo){
        if(spiderInfoService.save(cattleSpiderInfo)){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("add error");
    }

    @PostMapping("/edit")
    public Object edit(@NotNull @RequestBody CattleSpiderInfo cattleSpiderInfo){
        if(spiderInfoService.updateById(cattleSpiderInfo)){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("edit error");
    }

    @GetMapping("/detail")
    public Object detail(@NotNull Integer id){
        CattleSpiderInfo cattleSpiderInfo = spiderInfoService.getById(id);
        return ResponseUtil.success(cattleSpiderInfo);
    }

    @DeleteMapping("/remove")
    public Object remove(@NotNull Integer id){
        if(spiderInfoService.removeById(id)){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("不存在的Id");
    }
}
