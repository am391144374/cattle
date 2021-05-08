package com.cattle.service.Impl.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.kettle.CattleKtrStep;
import com.cattle.mapper.kettle.CattleKtrStepInfoMapper;
import com.cattle.service.api.kettle.KtrStepInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrStepInfoServiceImpl implements KtrStepInfoService {

    @Resource
    private CattleKtrStepInfoMapper stepInfoMapper;

    public List<CattleKtrStep> selectStepInfoByKtrId(Integer jobId){
        QueryWrapper<CattleKtrStep> queryWrapper = new QueryWrapper<CattleKtrStep>();
        queryWrapper.eq("ktr_info_id",jobId);
        return stepInfoMapper.selectList(queryWrapper);
    }

    public CattleKtrStep selectStepInfoById(Integer stepId){
        return stepInfoMapper.selectById(stepId);
    }

    public int save(CattleKtrStep stepInfo){
        return stepInfoMapper.insert(stepInfo);
    }

}
