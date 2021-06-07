package com.cattle.common.handler;

import com.cattle.common.context.ProcessContent;
import com.cattle.common.enums.JobStatus;

/**
 * 组件执行类
 */
public interface ProcessHandler {

    void execute(ProcessContent processContent);

    void setNextHandler(ProcessHandler processHandler);

    /**
     * 执行主要内容
     */
    void executeContent(ProcessContent processContent);

    void setStatus(ProcessContent processContent, JobStatus status);
}
