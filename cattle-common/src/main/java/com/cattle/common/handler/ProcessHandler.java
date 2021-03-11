package com.cattle.common.handler;

import com.cattle.common.constant.JobStatus;
import com.cattle.common.context.ProcessContext;

public interface ProcessHandler {

    void execute(ProcessContext processContext);

    void setNextHandler(ProcessHandler processHandler);

    /**
     * 执行主要内容
     */
    void executeContent(ProcessContext processContext);

    void setStatus(ProcessContext processContext, JobStatus.Status status);
}
