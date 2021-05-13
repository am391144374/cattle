package com.cattle.service.Impl;

import com.cattle.entity.CattleJob;
import com.cattle.entity.kettle.CattleKtrField;
import com.cattle.entity.kettle.CattleKtrInfo;
import com.cattle.entity.kettle.CattleKtrStep;
import com.cattle.mapper.JobMapper;
import com.cattle.service.api.JobService;
import com.cattle.service.api.kettle.CattleKtrInfoService;
import com.cattle.service.api.kettle.CattleKtrStepFieldService;
import com.cattle.service.api.kettle.CattleKtrStepInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;
    @Autowired
    private CattleKtrInfoService ktrInfoService;
    @Autowired
    private CattleKtrStepInfoService stepInfoService;
    @Autowired
    private CattleKtrStepFieldService stepFieldService;

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
        switch (jobInfo.getScriptType()){
            case "spider":
                break;
            case "kettle":
                CattleKtrInfo cattleKtrInfo = ktrInfoService.selectById(jobInfo.getRelateId());
                List<CattleKtrStep> stepInfoList = stepInfoService.selectStepInfoByKtrInfoId(cattleKtrInfo.getId());
                for(CattleKtrStep stepInfo : stepInfoList){
                    List<CattleKtrField> stepFields = stepFieldService.selectFieldListByStepId(stepInfo.getStepId());
                    stepInfo.setFieldList(stepFields);
                }
                cattleKtrInfo.setStepInfoList(stepInfoList);
                jobInfo.setKtrInfo(cattleKtrInfo);
                break;
        }
        return jobInfo;
    }
}
