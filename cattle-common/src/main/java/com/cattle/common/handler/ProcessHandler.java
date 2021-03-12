package com.cattle.common.handler;

import com.cattle.common.context.ProcessContext;
import com.cattle.common.enums.JobStatus;

/**
 * 组件执行类
 */
public interface ProcessHandler {

    void execute(ProcessContext processContext);

    void setNextHandler(ProcessHandler processHandler);

    /**
     * 执行主要内容
     */
    void executeContent(ProcessContext processContext);

    void setStatus(ProcessContext processContext, JobStatus status);
}
