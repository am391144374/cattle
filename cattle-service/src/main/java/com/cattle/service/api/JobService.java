package com.cattle.service.api;

import com.cattle.entity.CattleJob;

public interface JobService {

    CattleJob selectById(Integer jobId);

    CattleJob buildExecuteJobInfo(Integer jobId);

    int updateJobStatus(long batchId,String status);
}
