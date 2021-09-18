package component.kettle.handler;

import com.cattle.common.context.ProcessContent;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.exception.ExecuteKettleScriptException;
import com.cattle.common.handler.ExecuteProcessHandler;
import component.kettle.trans.MyTrans;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.trans.step.StepInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lsj
 * 脚本执行类
 */
public class ExecuteKettleScriptProcessHandler extends ExecuteProcessHandler {

    private MyTrans trans = null;

    @Override
    public void executeContent(ProcessContent processContent) {
        TransMeta transMeta = (TransMeta) processContent.get("transMeta");
        long batchId = processContent.getBatchId();
        if(transMeta == null){
            processContent.putError(this,new ExecuteKettleScriptException("execute kettle script error TransMeta is null"));
            return;
        }
        trans = new MyTrans(transMeta);
        // 执行转换
        try {
            trans.setBatchId(batchId);
            trans.execute(null);
            // 等待转换执行结束

            // 记录最后一个步骤的数据
            final List<RowMetaAndData> successRows = new ArrayList<>();
            final List<RowMetaAndData> errorRows = new ArrayList<>();
            //添加成功数
            addSuccessResultListener("successJson",successRows);
            //添加错误数
            addErrorResultListener("errorJson",errorRows);

            trans.waitUntilFinished();
            // 抛出异常
            if (trans.getErrors() > 0) {
                processContent.putError(this,new RuntimeException("There are errors during transformation exception!(传输过程中发生异常)"));
                String[] errMsgList = KettleLogStore.getAppender().getBuffer(trans.getLogChannelId(), false).toString().split("\n\r\n");
                String errMsg=errMsgList[0];
                processContent.putError(this,new RuntimeException(errMsg));
            }else{
                if(!successRows.isEmpty()){
                    RowMetaAndData rmad = successRows.get(0);
                    processContent.addSuccessCount(Math.toIntExact(rmad.getInteger("count")));
                }
                if(!errorRows.isEmpty()){
                    RowMetaAndData rmad = errorRows.get(0);
                    processContent.putError(this,new RuntimeException(rmad.getString("error_msg",null)));
                }

                processContent.setJobStatus(JobStatus.FINISH);
            }
        } catch (KettleException e) {
            e.printStackTrace();
            processContent.putError(this,e);
        }finally {
            trans.cleanup();
            transMeta.clear();
        }
    }

    private void addSuccessResultListener(String stepName,List<RowMetaAndData> successRows){
        StepInterface successStep = trans.findStepInterface(stepName,0);
        if(successStep == null){
            return;
        }
        successStep.addRowListener(new RowListener() {
            public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row)throws KettleStepException {
                successRows.add(new RowMetaAndData(rowMeta, row));
            }

            public void rowReadEvent(RowMetaInterface arg0, Object[] arg1)throws KettleStepException {

            }

            public void errorRowWrittenEvent(RowMetaInterface arg0,Object[] arg1) throws KettleStepException {

            }
        });
    }

    private void addErrorResultListener(String stepName,List<RowMetaAndData> errorRows){
        StepInterface errorStep = trans.findStepInterface(stepName,0);
        if(errorStep == null){
            return;
        }
        errorStep.addRowListener(new RowListener() {
            public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row)throws KettleStepException {
                errorRows.add(new RowMetaAndData(rowMeta, row));
            }

            public void rowReadEvent(RowMetaInterface arg0, Object[] arg1)throws KettleStepException {

            }

            public void errorRowWrittenEvent(RowMetaInterface arg0,Object[] arg1) throws KettleStepException {

            }
        });
    }
}
