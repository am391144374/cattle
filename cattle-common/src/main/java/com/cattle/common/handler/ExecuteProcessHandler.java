package com.cattle.common.handler;

import com.cattle.common.context.ProcessContent;
import com.cattle.common.enums.JobStatus;

/**
 * 组件通用执行类，链式调用，更好划分处理边界
 */
public abstract class ExecuteProcessHandler implements ProcessHandler {

    private ProcessHandler next;

    @Override
    public void execute(ProcessContent processContent){
        //上一步执行成功，则继续，否则跳过后续执行步骤
        try{
            if(processContent.getJobStatus() == JobStatus.RUNNING){
                executeContent(processContent);
                if(next != null){
                    next.execute(processContent);
                }
            }else {
                setStatus(processContent, JobStatus.INTERRUPT);
            }
        }catch (Exception e){
            e.printStackTrace();
            processContent.putError(this,e);
        }
    }

    @Override
    public void setNextHandler(ProcessHandler processHandler) {
        next = processHandler;
    }

    @Override
    public void setStatus(ProcessContent processContent, JobStatus status) {
        processContent.setJobStatus(status);
    }

}
