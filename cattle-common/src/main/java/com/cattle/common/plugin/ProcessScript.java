package com.cattle.common.plugin;

import com.cattle.common.handler.ProcessHandler;

public abstract class ProcessScript implements ExecuteScriptInterface {

    protected ProcessHandler first;
    protected ProcessHandler end;

    /**
     * 添加到第一个执行步骤
     * @param processHandler
     * @return
     */
    public ProcessScript addFirstProcess(ProcessHandler processHandler){
        if(first == null){
            first = processHandler;
            end = processHandler;
        }else{
            ProcessHandler oldProcess = first;
            processHandler.setNextHandler(oldProcess);
            first = processHandler;
        }
        return this;
    }

    /**
     * 添加到最后一个执行步骤
     * @param processHandler
     * @return
     */
    public ProcessScript addLastProcess(ProcessHandler processHandler){
        if(end == null && first == null){
            first = processHandler;
            end = processHandler;
        }else if(end == null && first != null){
            first.setNextHandler(processHandler);
            end = processHandler;
        }else{
            end.setNextHandler(processHandler);
            end = processHandler;
        }
        return this;
    }
}
