package com.cattle.component.kettle.handler;

import com.cattle.common.enums.JobStatus;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContent;
import com.cattle.component.kettle.trans.MyTrans;
import com.cattle.common.exception.ExecuteKettleScriptException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.trans.TransMeta;

/**
 * @author lsj
 * 脚本执行类
 */
public class ExecuteKettleScriptProcessHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContent processContent) {
        TransMeta transMeta = (TransMeta) processContent.get("transMeta");
        long batchId = processContent.getBatchId();
        if(transMeta == null){
            processContent.putError(this,new ExecuteKettleScriptException("execute kettle script error TransMeta is null"));
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
                processContent.putError(this,new RuntimeException("There are errors during transformation exception!(传输过程中发生异常)"));
                String[] errMsgList = KettleLogStore.getAppender().getBuffer(trans.getLogChannelId(), false).toString().split("\n\r\n");
                String errMsg=errMsgList[0];
                processContent.putError(this,new RuntimeException(errMsg));
            }else{
                processContent.setJobStatus(JobStatus.FINISH);
            }
        } catch (KettleException e) {
            e.printStackTrace();
            processContent.putError(this,e);
        }finally {
            trans.cleanup();
        }
    }
}
