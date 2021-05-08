package com.cattle.service.Impl.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.kettle.CattleKtrField;
import com.cattle.mapper.kettle.CattleKtrStepFieldMapper;
import com.cattle.service.api.kettle.KtrStepFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrStepFieldImpl implements KtrStepFieldService {

    @Resource
    private CattleKtrStepFieldMapper stepFieldMapper;

    public List<CattleKtrField> selectFieldListByStepId(Integer stepId){
        QueryWrapper<CattleKtrField> queryWrapper = new QueryWrapper();
        queryWrapper.eq("step_id",stepId);
        return stepFieldMapper.selectList(queryWrapper);
    }

    public CattleKtrField selectFieldListById(Integer fieldId){
        return stepFieldMapper.selectById(fieldId);
    }

    public int save(CattleKtrField fieldInfo){
        return stepFieldMapper.insert(fieldInfo);
    }
}
