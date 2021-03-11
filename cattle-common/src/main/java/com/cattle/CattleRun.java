package com.cattle;


import com.cattle.context.ProcessContext;
import com.cattle.exception.ProcessHandlerNullException;
import com.cattle.handler.ProcessHandler;

/**
 * @author lsj
 * 脚本最小执行类
 */
public class CattleRun {

    private ProcessHandler first;
    private ProcessHandler end;

    private CattleRun(){}

    public static CattleRun create(){
        return new CattleRun();
    }

    /**
     * 添加到第一个执行步骤
     * @param processHandler
     * @return
     */
    public CattleRun addFirstProcess(ProcessHandler processHandler){
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
    public CattleRun addLastProcess(ProcessHandler processHandler){
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

    public void execute(long batchId){
        if(first == null || end == null){
            ResultHelper.setException(batchId,this,new ProcessHandlerNullException("processHandler is not init"));
            return ;
        }
        ProcessContext processContext = new ProcessContext();
        processContext.put("batchId",batchId);
        first.execute(processContext);
    }

}
