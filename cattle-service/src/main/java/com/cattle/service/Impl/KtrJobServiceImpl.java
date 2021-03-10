package com.cattle.service.Impl;

import com.cattle.entity.kettle.KtrJobInfo;
import com.cattle.entity.kettle.KtrStepField;
import com.cattle.entity.kettle.KtrStepInfo;
import com.cattle.mapper.KtrJobMapper;
import com.cattle.service.api.KtrJobService;
import com.cattle.service.api.KtrStepFieldService;
import com.cattle.service.api.KtrStepInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KtrJobServiceImpl implements KtrJobService {

    @Resource
    private KtrJobMapper jobMapper;
    @Autowired
    private KtrStepInfoService stepInfoService;
    @Autowired
    private KtrStepFieldService stepFieldService;

    /**
     * 查询job信息
     * @param jobId
     * @return
     */
    public KtrJobInfo selectById(Integer jobId){
        return jobMapper.selectById(jobId);
    }

    /**
     * 构建执行job
     * @param jobId
     * @return
     */
    public KtrJobInfo buildExecuteJobInfo(Integer jobId){
        KtrJobInfo jobInfo = selectById(jobId);
        if(jobInfo == null){
            return null;
        }
        List<KtrStepInfo> stepInfoList = stepInfoService.selectStepInfoByJobId(jobId);
        for(KtrStepInfo stepInfo : stepInfoList){
            List<KtrStepField> stepFields = stepFieldService.selectFieldListByStepId(stepInfo.getStepId());
            stepInfo.setFieldList(stepFields);
        }
        jobInfo.setStepInfoList(stepInfoList);
        return jobInfo;
    }

}
