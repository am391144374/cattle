package com.cattle.service.Impl;

import com.cattle.entity.CattleSpiderInfo;
import com.cattle.mapper.CattleSpiderMapper;
import com.cattle.service.api.CattleSpiderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CattleSpiderInfoImpl implements CattleSpiderInfoService {

    @Resource
    private CattleSpiderMapper spiderMapper;

    @Override
    public CattleSpiderInfo selectById(Integer spiderId) {
        return spiderMapper.selectById(spiderId);
    }

    @Override
    public int insert(CattleSpiderInfo spiderInfo) {
        return spiderMapper.insert(spiderInfo);
    }

    @Override
    public int updateById(CattleSpiderInfo spiderInfo) {
        return spiderMapper.updateById(spiderInfo);
    }

    @Override
    public int delete(Integer spiderId) {
        return spiderMapper.deleteById(spiderId);
    }
}
