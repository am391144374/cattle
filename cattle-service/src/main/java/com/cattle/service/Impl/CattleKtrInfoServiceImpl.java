package com.cattle.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cattle.entity.CattleKtrInfo;
import com.cattle.mapper.CattleKtrInfoMapper;
import com.cattle.service.api.CattleKtrInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CattleKtrInfoServiceImpl implements CattleKtrInfoService {

    @Resource
    private CattleKtrInfoMapper ktrInfoMapper;

    @Override
    public CattleKtrInfo selectById(Integer id) {
        return ktrInfoMapper.selectById(id);
    }

    @Override
    public IPage<CattleKtrInfo> selectPage(Integer offset, Integer limit, Map<String,Object> queryMap){
        Page<CattleKtrInfo> page = new Page(offset,limit);
        QueryWrapper<CattleKtrInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Optional.ofNullable(queryMap).orElse(new HashMap<>()).forEach((column, value) -> {
            queryWrapper.eq(StrUtil.toUnderlineCase(column),value);
        });
        return ktrInfoMapper.selectPage(page,queryWrapper);
    }

    @Override
    public int insert(CattleKtrInfo cattleKtrInfo){
        return ktrInfoMapper.insert(cattleKtrInfo);
    }

    @Override
    public int updateById(CattleKtrInfo cattleKtrInfo) {
        return ktrInfoMapper.updateById(cattleKtrInfo);
    }

    @Override
    public int delete(Integer id) {
        return ktrInfoMapper.deleteById(id);
    }
}
