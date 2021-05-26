package com.cattle.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cattle.common.entity.CattleKtrInfo;
import com.cattle.common.entity.CattleRunLog;
import com.cattle.common.util.ResponseUtil;
import com.cattle.service.api.RunLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/run/log")
public class CattleRunLogController {

    @Autowired
    private RunLogService runLogService;

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1",required = false) Integer offset,
                       @RequestParam(defaultValue = "10",required = false) Integer limit,
                       @RequestParam(required = false) String queryJson){
        Map<String,Object> queryMap = null;
        if(queryJson != null){
            queryMap = JSONObject.parseObject(queryJson, HashMap.class);
        }
        Page<CattleRunLog> page = new Page(offset,limit);
        QueryWrapper<CattleRunLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("start_time");
        Optional.ofNullable(queryMap).orElse(new HashMap<>()).forEach((column, value) -> {
            if(StrUtil.isNotBlank(value.toString())){
                queryWrapper.eq(StrUtil.toUnderlineCase(column),value);
            }
        });
        IPage result = runLogService.page(page,queryWrapper);
        return ResponseUtil.success(result);
    }

}
