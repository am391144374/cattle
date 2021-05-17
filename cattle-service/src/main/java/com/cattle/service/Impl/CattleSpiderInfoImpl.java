package com.cattle.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cattle.entity.CattleJob;
import com.cattle.entity.CattleSpiderInfo;
import com.cattle.mapper.CattleSpiderMapper;
import com.cattle.service.api.CattleSpiderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CattleSpiderInfoImpl extends ServiceImpl<CattleSpiderMapper,CattleSpiderInfo> implements CattleSpiderInfoService {

    @Resource
    private CattleSpiderMapper spiderMapper;

    @Override
    public IPage<CattleSpiderInfo> selectPageList(Integer offset, Integer limit, Map<String, Object> queryParam) {
        Page<CattleSpiderInfo> page = new Page(offset,limit);
        QueryWrapper<CattleSpiderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Optional.ofNullable(queryParam).orElse(new HashMap<>()).forEach((column, value) -> {
            if(StrUtil.isNotBlank(value.toString())){
                queryWrapper.eq(StrUtil.toUnderlineCase(column),value);
            }
        });
        return spiderMapper.selectPage(page,queryWrapper);
    }
}
