package com.cattle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cattle.entity.CattleKtrField;
import com.cattle.mapper.CattleKtrStepFieldMapper;
import com.cattle.service.api.CattleKtrStepFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CattleKtrStepFieldImpl implements CattleKtrStepFieldService {

    @Resource
    private CattleKtrStepFieldMapper stepFieldMapper;

    public List<CattleKtrField> selectFieldListByStepId(Integer stepId){
        QueryWrapper<CattleKtrField> queryWrapper = new QueryWrapper();
        queryWrapper.eq("step_id",stepId);
        return stepFieldMapper.selectList(queryWrapper);
    }

    public CattleKtrField selectFieldById(Integer fieldId){
        return stepFieldMapper.selectById(fieldId);
    }

    public int insert(CattleKtrField fieldInfo){
        return stepFieldMapper.insert(fieldInfo);
    }

    @Override
    public IPage<CattleKtrField> selectFieldListPageByStepId(Integer stepId, Integer offset, Integer limit) {
        Page<CattleKtrField> page = new Page<>(offset,limit);
        QueryWrapper<CattleKtrField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("step_id",stepId);
        return stepFieldMapper.selectPage(page,queryWrapper);
    }

    @Override
    public int updateById(CattleKtrField cattleKtrField) {
        return stepFieldMapper.updateById(cattleKtrField);
    }

    @Override
    public int delete(Integer fieldId) {
        return stepFieldMapper.deleteById(fieldId);
    }
}
