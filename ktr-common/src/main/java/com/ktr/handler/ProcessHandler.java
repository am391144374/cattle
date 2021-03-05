package com.ktr.handler;

import com.ktr.context.ProcessContext;

import java.util.Map;

public interface ProcessHandler {

    void execute(ProcessContext processContext, Map<String, Object> variables);

    void setNextHandler(ProcessHandler processHandler);

    /**
     * 执行主要内容
     */
    void executeContent(ProcessContext processContext, Map<String, Object> variables);
}
