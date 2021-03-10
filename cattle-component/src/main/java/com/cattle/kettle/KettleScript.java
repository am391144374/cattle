package com.cattle.kettle;


import com.cattle.ResultHelper;
import com.cattle.context.ProcessContext;
import com.cattle.exception.ProcessHandlerNullException;
import com.cattle.handler.ProcessHandler;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.trans.TransMeta;

/**
 * @author lsj
 * kettle 最小执行类
 */
public class KettleScript {

    private TransMeta transMeta;
    private String scriptFile;
    private ProcessHandler first;
    private ProcessHandler end;

    private KettleScript(){}

    public static KettleScript create(){
        return new KettleScript();
    }

    public KettleScript addScriptFile(String scriptFile){
        this.scriptFile = scriptFile;
        return this;
    }

    /**
     * 添加到第一个执行步骤
     * @param processHandler
     * @return
     */
    public KettleScript addFirstProcess(ProcessHandler processHandler){
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
    public KettleScript addLastProcess(ProcessHandler processHandler){
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

    private void addExecuteKettleScriptActuator(){
        ExecuteKettleScriptProcessHandler kettleScriptProcessHandler = new ExecuteKettleScriptProcessHandler();
        addLastProcess(kettleScriptProcessHandler);
    }

    public void execute(long batchId){
        if(first == null || end == null){
            ResultHelper.setException(batchId,this,new ProcessHandlerNullException("processHandler is not init"));
            return ;
        }
        //最后加上 kettle脚本执行器
        addExecuteKettleScriptActuator();

        ProcessContext processContext = new ProcessContext();

        TransMeta transMeta = null;
        try {
            transMeta = new TransMeta(scriptFile);
            processContext.put("transMeta",transMeta);
            processContext.put("batchId",batchId);
            first.execute(processContext);
        } catch (KettleXMLException e) {
            e.printStackTrace();
            ResultHelper.setException(processContext.getBatchId(),this,e);
        } catch (KettleMissingPluginsException e) {
            e.printStackTrace();
            ResultHelper.setException(processContext.getBatchId(),this,e);
        }
    }

}
