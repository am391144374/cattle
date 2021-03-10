package com.ktr;

import com.ktr.context.ProcessContext;
import com.ktr.ResultHelper;
import com.ktr.handler.ProcessHandler;

import java.util.Map;

public abstract class ExecuteProcessHandler implements ProcessHandler {

    private ProcessHandler next;

    @Override
    public void execute(ProcessContext processContext){
        //上一步执行成功，则继续，否则跳过后续执行步骤
        if(ResultHelper.isSuccess(processContext.getBatchId())){
            executeContent(processContext);
            if(next != null){
                next.execute(processContext);
            }
        }
    }

    @Override
    public void setNextHandler(ProcessHandler processHandler) {
        next = processHandler;
    }
}
