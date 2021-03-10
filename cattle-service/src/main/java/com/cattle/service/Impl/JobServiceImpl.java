package com.cattle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cattle.entity.CattleJob;
import com.cattle.entity.kettle.KtrStepField;
import com.cattle.entity.kettle.KtrStepInfo;
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
        List<KtrStepInfo> stepInfoList = stepInfoService.selectStepInfoByJobId(jobId);
        for(KtrStepInfo stepInfo : stepInfoList){
            List<KtrStepField> stepFields = stepFieldService.selectFieldListByStepId(stepInfo.getStepId());
            stepInfo.setFieldList(stepFields);
        }
        jobInfo.setStepInfoList(stepInfoList);
        return jobInfo;
    }

    /**
     * 更新脚本执行状态
     * @param batchId
     * @param status
     * @return
     */
    @Override
    public int updateJobStatus(long batchId,String status) {
        QueryWrapper<CattleJob> queryWrapper = new QueryWrapper();
        CattleJob cattleJob = new CattleJob();
        cattleJob.setStatus(status);
        queryWrapper.eq("batch_id",batchId);
        return jobMapper.update(cattleJob,queryWrapper);
    }

}
