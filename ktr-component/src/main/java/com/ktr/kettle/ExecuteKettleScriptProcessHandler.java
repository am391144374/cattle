package com.ktr.kettle;

import com.ktr.ExecuteProcessHandler;
import com.ktr.ResultHelper;
import com.ktr.context.ProcessContext;
import com.ktr.kettle.exception.ExecuteKettleScriptException;
import com.ktr.kettle.trans.MyTrans;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.trans.TransMeta;

import java.util.Map;

/**
 * @author lsj
 * 脚本执行类
 */
public class ExecuteKettleScriptProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContext processContext, Map<String,Object> variables) {
        TransMeta transMeta = (TransMeta) processContext.get("transMeta");
        long batchId = (long) processContext.get("batchId");
        if(transMeta == null){
            ResultHelper.setException(variables,this,new ExecuteKettleScriptException("execute kettle script error TransMeta is null"));
            return;
        }
        MyTrans trans = null;
        trans = new MyTrans(transMeta);
        // 执行转换
        try {
            trans.setBatchId(batchId);
            trans.execute(null);
            // 等待转换执行结束
            trans.waitUntilFinished();
            // 抛出异常
            if (trans.getErrors() > 0) {
                ResultHelper.setException(variables,this,new RuntimeException("There are errors during transformation exception!(传输过程中发生异常)"));
                String[] errMsgList = KettleLogStore.getAppender().getBuffer(trans.getLogChannelId(), false).toString().split("\n\r\n");
                String errMsg=errMsgList[0];
                ResultHelper.setException(variables,this,new RuntimeException(errMsg));
            }
        } catch (KettleException e) {
            e.printStackTrace();
            ResultHelper.setException(variables,this,e);
        }finally {
            trans.cleanup();
        }


    }
}
