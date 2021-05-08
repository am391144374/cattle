package com.cattle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cattle.common.enums.JobStatus;
import com.cattle.entity.CattleRunLog;
import com.cattle.mapper.RunLogMapper;
import com.cattle.service.api.RunLogService;
import org.springframework.stereotype.Service;

@Service
public class RunLogServiceImpl extends ServiceImpl<RunLogMapper,CattleRunLog> implements RunLogService {

    public boolean createLog(long batchId){
        CattleRunLog runLog = new CattleRunLog();
        runLog.setBatchId(batchId);
        runLog.setJobStatus(JobStatus.CREATE.getName());
        return save(runLog);
    }

    @Override
    public boolean updateJobInfo(Integer jobId, long batchId, String jobName) {
        CattleRunLog runLog = new CattleRunLog();
        runLog.setBatchId(batchId);
        runLog.setJobName(jobName);
        runLog.setJobId(jobId);
        return updateById(runLog);
    }

    public boolean updateStatus(long batchId, JobStatus status){
        CattleRunLog runLog = new CattleRunLog();
        runLog.setBatchId(batchId);
        runLog.setJobStatus(status.getName());
        return updateById(runLog);
    }

    @Override
    public boolean updateResult(long batchId,Integer count,Integer errorCount,String errorText,Integer warnCount,String warnText,JobStatus jobStatus) {
        CattleRunLog runLog = new CattleRunLog();
        runLog.setBatchId(batchId);
        runLog.setJobStatus(jobStatus.getName());
        runLog.setCount(count == null ? 0 : count);
        runLog.setErrorCount(errorCount == null ? 0 : errorCount);
        runLog.setWarnCount(errorCount == null ? 0 : warnCount);
        runLog.setErrorText(errorText);
        runLog.setWarnText(warnText);
        return updateById(runLog);
    }

    @Override
    public boolean updateErrorInfo(long batchId, String errorText) {
        CattleRunLog runLog = new CattleRunLog();
        runLog.setBatchId(batchId);
        runLog.setJobStatus(JobStatus.INTERRUPT.getName());
        runLog.setErrorText(errorText);
        return updateById(runLog);
    }

}
