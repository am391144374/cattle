package com.cattle.common.handler;

import com.cattle.common.context.ProcessContext;
import com.cattle.common.enums.JobStatus;

/**
 * 组件通用执行类，链式调用，更好划分处理边界
 */
public abstract class ExecuteProcessHandler implements ProcessHandler {

    private ProcessHandler next;

    @Override
    public void execute(ProcessContext processContext){
        //上一步执行成功，则继续，否则跳过后续执行步骤
        try{
            if(processContext.getJobStatus() == JobStatus.RUNNING){
                executeContent(processContext);
                if(next != null){
                    next.execute(processContext);
                }
            }else {
                setStatus(processContext, JobStatus.INTERRUPT);
            }
        }catch (Exception e){
            e.printStackTrace();
            processContext.putError(this,e);
        }
    }

    @Override
    public void setNextHandler(ProcessHandler processHandler) {
        next = processHandler;
    }

    @Override
    public void setStatus(ProcessContext processContext, JobStatus status) {
        processContext.setJobStatus(status);
    }
}
