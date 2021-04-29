package com.cattle.component.kettle;


import com.cattle.component.kettle.handler.ExecuteKettleScriptProcessHandler;
import com.cattle.component.kettle.handler.StepProcessHandler;
import com.cattle.component.kettle.step.ConstantStep;
import com.cattle.component.kettle.step.ExcelInputStep;
import com.cattle.component.kettle.step.SelectValuesStep;
import com.cattle.component.ProcessScript;
import com.cattle.common.context.ProcessContext;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lsj
 * kettle 执行
 */
public class KettleScript extends ProcessScript {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String scriptFile;
    private long batchId;
    private KettleConfig kettleConfig;

    private ProcessContext context;

    public KettleScript(ProcessContext context){
        this.context = context;
    }

    private void addExecuteKettleScriptActuator(){
        ExecuteKettleScriptProcessHandler kettleScriptProcessHandler = new ExecuteKettleScriptProcessHandler();
        addLastProcess(kettleScriptProcessHandler);
    }

    @Override
    public void run(){
        //最后加上 kettle脚本执行器
        addExecuteKettleScriptActuator();

        TransMeta transMeta = null;
        try {
            transMeta = new TransMeta(scriptFile);
            context.put("transMeta",transMeta);
            context.put("batchId",batchId);
            logger.info("{} start spider config:{}",Thread.currentThread().getName(),kettleConfig.toString());
            first.execute(context);
        } catch (KettleXMLException e) {
            e.printStackTrace();
            context.putError(this,e);
        } catch (KettleMissingPluginsException e) {
            e.printStackTrace();
            context.putError(this,e);
        }
    }

    public KettleScript setScriptFile(String scriptFile){
        this.scriptFile = scriptFile;
        return this;
    }

    public KettleScript setBatchId(long batchId){
        this.batchId = batchId;
        return this;
    }

    public void buildKettleProcess(KettleConfig kettleConfig){
        /** --脚本步骤执行器*/
        StepProcessHandler stepProcessHandler = new StepProcessHandler();
        ExcelInputStep excelInputStep = new ExcelInputStep(kettleConfig);
        stepProcessHandler.addStepMeta(excelInputStep);
        SelectValuesStep selectValuesStep = new SelectValuesStep(kettleConfig);
        stepProcessHandler.addStepMeta(selectValuesStep);
        ConstantStep constantStep = new ConstantStep(kettleConfig);
        stepProcessHandler.addStepMeta(constantStep);
        /** -------------*/

        /** excel 处理 */
//        ExcelHeadFormatHandler excelHeadFormatHandler = new ExcelHeadFormatHandler(kettleConfig);
        /** ---------- */

//        addLastProcess(excelHeadFormatHandler);
        addLastProcess(stepProcessHandler);

        setBatchId(kettleConfig.getBatchId());
        setScriptFile(kettleConfig.getScriptFile());

        this.kettleConfig = kettleConfig;

    }
}
