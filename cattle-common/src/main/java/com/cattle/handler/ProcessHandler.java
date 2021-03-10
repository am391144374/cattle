package com.cattle.handler;

import com.cattle.context.ProcessContext;

public interface ProcessHandler {

    void execute(ProcessContext processContext);

    void setNextHandler(ProcessHandler processHandler);

    /**
     * 执行主要内容
     */
    void executeContent(ProcessContext processContext);
}
