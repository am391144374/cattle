package com.cattle.service.Impl.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.kettle.KtrStep;
import com.cattle.mapper.kettle.KtrStepInfoMapper;
import com.cattle.service.api.kettle.KtrStepInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrStepInfoServiceImpl implements KtrStepInfoService {

    @Resource
    private KtrStepInfoMapper stepInfoMapper;

    public List<KtrStep> selectStepInfoByJobId(Integer jobId){
        QueryWrapper<KtrStep> queryWrapper = new QueryWrapper<KtrStep>();
        queryWrapper.eq("job_id",jobId);
        return stepInfoMapper.selectList(queryWrapper);
    }

    public KtrStep selectStepInfoById(Integer stepId){
        return stepInfoMapper.selectById(stepId);
    }

    public int save(KtrStep stepInfo){
        return stepInfoMapper.insert(stepInfo);
    }

}
