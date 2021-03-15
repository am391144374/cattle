package com.cattle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.CattleJob;
import com.cattle.entity.kettle.KtrField;
import com.cattle.entity.kettle.KtrStep;
import com.cattle.mapper.JobMapper;
import com.cattle.service.api.JobService;
import com.cattle.service.api.kettle.KtrStepFieldService;
import com.cattle.service.api.kettle.KtrStepInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;
    @Autowired
    private KtrStepInfoService stepInfoService;
    @Autowired
    private KtrStepFieldService stepFieldService;

    /**
     * 查询job信息
     * @param jobId
     * @return
     */
    public CattleJob selectById(Integer jobId){
        return jobMapper.selectById(jobId);
    }

    /**
     * 构建执行job
     * @param jobId
     * @return
     */
    public CattleJob buildExecuteJobInfo(Integer jobId){
        CattleJob jobInfo = selectById(jobId);
        if(jobInfo == null){
            return null;
        }
        List<KtrStep> stepInfoList = stepInfoService.selectStepInfoByJobId(jobId);
        for(KtrStep stepInfo : stepInfoList){
            List<KtrField> stepFields = stepFieldService.selectFieldListByStepId(stepInfo.getStepId());
            stepInfo.setFieldList(stepFields);
        }
        jobInfo.setStepInfoList(stepInfoList);
        return jobInfo;
    }
}
