package com.cattle.common.handler;

import com.cattle.common.constant.JobStatus;
import com.cattle.common.context.ProcessContext;

public abstract class ExecuteProcessHandler implements ProcessHandler {

    private ProcessHandler next;

    @Override
    public void execute(ProcessContext processContext){
        //上一步执行成功，则继续，否则跳过后续执行步骤
        if(processContext.isSuccess()){
            executeContent(processContext);
            if(next != null){
                next.execute(processContext);
            }
        }else {
            setStatus(processContext, JobStatus.Status.INTERRUPT);
        }
    }

    @Override
    public void setNextHandler(ProcessHandler processHandler) {
        next = processHandler;
    }

    @Override
    public void setStatus(ProcessContext processContext, JobStatus.Status status) {
        processContext.setJobStatus(status);
    }
}
