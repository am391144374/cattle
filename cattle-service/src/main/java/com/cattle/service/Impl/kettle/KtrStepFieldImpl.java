package com.cattle.service.Impl.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.kettle.KtrField;
import com.cattle.mapper.kettle.KtrStepFieldMapper;
import com.cattle.service.api.kettle.KtrStepFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrStepFieldImpl implements KtrStepFieldService {

    @Resource
    private KtrStepFieldMapper stepFieldMapper;

    public List<KtrField> selectFieldListByStepId(Integer stepId){
        QueryWrapper<KtrField> queryWrapper = new QueryWrapper();
        queryWrapper.eq("step_id",stepId);
        return stepFieldMapper.selectList(queryWrapper);
    }

    public KtrField selectFieldListById(Integer fieldId){
        return stepFieldMapper.selectById(fieldId);
    }

    public int save(KtrField fieldInfo){
        return stepFieldMapper.insert(fieldInfo);
    }
}
