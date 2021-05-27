package com.cattle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cattle.common.entity.CattleKtrStep;
import com.cattle.mapper.CattleKtrStepInfoMapper;
import com.cattle.service.api.CattleKtrStepInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CattleKtrStepInfoServiceImpl implements CattleKtrStepInfoService {

    @Resource
    private CattleKtrStepInfoMapper stepInfoMapper;

    @Override
    public List<CattleKtrStep> selectStepInfoByKtrInfoId(Integer ktrInfoId) {
        QueryWrapper<CattleKtrStep> queryWrapper = new QueryWrapper<CattleKtrStep>();
        queryWrapper.eq("ktr_info_id",ktrInfoId);
        return stepInfoMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<CattleKtrStep> selectStepInfoByKtrInfoIdPage(Integer ktrInfoId,Integer offset,Integer limit){
        Page<CattleKtrStep> page = new Page<>(offset,limit);
        QueryWrapper<CattleKtrStep> queryWrapper = new QueryWrapper<CattleKtrStep>();
        queryWrapper.eq("ktr_info_id",ktrInfoId);
        return stepInfoMapper.selectPage(page,queryWrapper);
    }

    @Override
    public CattleKtrStep selectStepInfoById(Integer stepId){
        return stepInfoMapper.selectById(stepId);
    }

    @Override
    public int insert(CattleKtrStep stepInfo){
        return stepInfoMapper.insert(stepInfo);
    }

    @Override
    public int updateById(CattleKtrStep cattleKtrStep) {
        return stepInfoMapper.updateById(cattleKtrStep);
    }

    @Override
    public int delete(Integer stepId) {
        return stepInfoMapper.deleteById(stepId);
    }

    @Override
    public CattleKtrStep selectOne(QueryWrapper<CattleKtrStep> queryWrapper) {
        return stepInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<CattleKtrStep> selectList(QueryWrapper<CattleKtrStep> queryWrapper) {
        return stepInfoMapper.selectList(queryWrapper);
    }

}
