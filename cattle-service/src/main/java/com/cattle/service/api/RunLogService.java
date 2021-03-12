package com.cattle.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cattle.common.enums.JobStatus;
import com.cattle.entity.CattleRunLog;

public interface RunLogService extends IService<CattleRunLog> {
    /**
     * 创建执行日志
     * @param jobId
     * @param batchId
     * @param jobName
     * @return
     */
    boolean createLog(Integer jobId,long batchId,String jobName);

    /**
     * 更新job状态
     * @param batchId
     * @param status
     * @return
     */
    public boolean updateStatus(long batchId, JobStatus status);

    /**
     * 保存结果信息
     * @param batchId
     * @param count
     * @param errorCount
     * @param errorText
     * @return
     */
    public boolean updateResult(long batchId,Integer count,Integer errorCount,String errorText,JobStatus jobStatus);


}
