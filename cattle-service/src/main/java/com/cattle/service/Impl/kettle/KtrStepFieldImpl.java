package com.cattle.service.Impl.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.kettle.KtrStepField;
import com.cattle.mapper.kettle.KtrStepFieldMapper;
import com.cattle.service.api.kettle.KtrStepFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrStepFieldImpl implements KtrStepFieldService {

    @Resource
    private KtrStepFieldMapper stepFieldMapper;

    public List<KtrStepField> selectFieldListByStepId(Integer stepId){
        QueryWrapper<KtrStepField> queryWrapper = new QueryWrapper();
        queryWrapper.eq("step_id",stepId);
        return stepFieldMapper.selectList(queryWrapper);
    }

    public KtrStepField selectFieldListById(Integer fieldId){
        return stepFieldMapper.selectById(fieldId);
    }

    public int save(KtrStepField fieldInfo){
        return stepFieldMapper.insert(fieldInfo);
    }
}
