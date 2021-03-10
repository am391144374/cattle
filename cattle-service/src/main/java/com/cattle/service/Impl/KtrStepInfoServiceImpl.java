package com.cattle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.kettle.KtrStepInfo;
import com.cattle.mapper.KtrStepInfoMapper;
import com.cattle.service.api.KtrStepInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrStepInfoServiceImpl implements KtrStepInfoService {

    @Resource
    private KtrStepInfoMapper stepInfoMapper;

    public List<KtrStepInfo> selectStepInfoByJobId(Integer jobId){
        QueryWrapper<KtrStepInfo> queryWrapper = new QueryWrapper<KtrStepInfo>();
        queryWrapper.eq("job_id",jobId);
        return stepInfoMapper.selectList(queryWrapper);
    }

    public KtrStepInfo selectStepInfoById(Integer stepId){
        return stepInfoMapper.selectById(stepId);
    }

    public int save(KtrStepInfo stepInfo){
        return stepInfoMapper.insert(stepInfo);
    }

}
