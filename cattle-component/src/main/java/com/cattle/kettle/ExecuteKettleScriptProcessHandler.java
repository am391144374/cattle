package com.cattle.kettle;

import com.cattle.ExecuteProcessHandler;
import com.cattle.ResultHelper;
import com.cattle.context.ProcessContext;
import com.cattle.kettle.exception.ExecuteKettleScriptException;
import com.cattle.kettle.trans.MyTrans;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.trans.TransMeta;

/**
 * @author lsj
 * 脚本执行类
 */
public class ExecuteKettleScriptProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContext processContext) {
        TransMeta transMeta = (TransMeta) processContext.get("transMeta");
        long batchId = (long) processContext.get("batchId");
        if(transMeta == null){
            ResultHelper.setException(processContext.getBatchId(),this,new ExecuteKettleScriptException("execute kettle script error TransMeta is null"));
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
                ResultHelper.setException(processContext.getBatchId(),this,new RuntimeException("There are errors during transformation exception!(传输过程中发生异常)"));
                String[] errMsgList = KettleLogStore.getAppender().getBuffer(trans.getLogChannelId(), false).toString().split("\n\r\n");
                String errMsg=errMsgList[0];
                ResultHelper.setException(processContext.getBatchId(),this,new RuntimeException(errMsg));
            }
        } catch (KettleException e) {
            e.printStackTrace();
            ResultHelper.setException(processContext.getBatchId(),this,e);
        }finally {
            trans.cleanup();
        }


    }
}
