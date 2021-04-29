package com.cattle.component.kettle;

import com.cattle.common.JobContextHelper;
import com.cattle.component.kettle.handler.ExecuteKettleScriptProcessHandler;
import com.cattle.component.kettle.handler.StepProcessHandler;
import com.cattle.component.kettle.meta.ExcelMeta;
import com.cattle.component.kettle.meta.FieldMeta;
import com.cattle.component.kettle.step.ConstantStep;
import com.cattle.component.kettle.step.ExcelInputStep;
import com.cattle.component.kettle.step.SelectValuesStep;
import com.cattle.component.ProcessScript;
import com.cattle.common.context.ProcessContext;
import com.cattle.entity.CattleJob;
import com.cattle.entity.kettle.KtrField;
import com.cattle.entity.kettle.KtrStep;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lsj
 * kettle 执行
 */
public class KettleScript extends ProcessScript {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private KettleConfig kettleConfig;
    private CattleJob cattleJob;

    public KettleScript() throws KettleException {
        KettlePluginInit.init();
    }

    private void addExecuteKettleScriptActuator(){
        ExecuteKettleScriptProcessHandler kettleScriptProcessHandler = new ExecuteKettleScriptProcessHandler();
        addLastProcess(kettleScriptProcessHandler);
    }

    @Override
    public void run(){

        buildConfig(cattleJob);

        if(kettleConfig.getProcessType() == KettleConfig.KettleProcessType.EDIT) {
            buildKettleEditProcess();
        }

        //最后加上 kettle脚本执行器
        addExecuteKettleScriptActuator();

        ProcessContext context = new ProcessContext();
        try {
            TransMeta transMeta = new TransMeta(cattleJob.getScriptPath());
            context.put("transMeta",transMeta);
            context.setJobName(kettleConfig.getJobName());
            context.setBatchId(kettleConfig.getBatchId());
            JobContextHelper.setJobContext(cattleJob.getBatchId(),context);
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

    public void buildKettleEditProcess(){

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
    }

    public void buildConfig(CattleJob job){
        KettleConfig kettleConfig = new KettleConfig();
        List<KtrStep> stepInfoList = job.getStepInfoList();
        for(KtrStep stepInfo : stepInfoList){
            String stepType = stepInfo.getStepType();
            List<FieldMeta> fieldMetaList = new ArrayList<>();
            List<KtrField> stepFields = stepInfo.getFieldList();
            stepFields.forEach(stepField -> {
                FieldMeta fieldMeta = FieldMeta.builder()
                        .comment(stepField.getComment())
                        .name(stepField.getFieldName())
                        .type(stepField.getFieldType())
                        .value(stepField.getDefaultValue()).build();
                if(fieldMeta.getType().equals("Number")){
                    fieldMeta.setPrecision(stepField.getPrecision());
                }else if(!fieldMeta.getType().equals("Integer")){
                    fieldMeta.setLength(stepField.getLength());
                }
                fieldMetaList.add(fieldMeta);
            });
            switch (stepType){
                // excel导入
                case "excelImport":
                    kettleConfig.setSelectValueMap(fieldMetaList);
                    ExcelMeta excelMeta = new ExcelMeta();
                    excelMeta.setFileName(stepInfo.getFileList());
                    excelMeta.setSheetName(new String[]{stepInfo.getSheetName()});
                    excelMeta.setStartRow(new int[]{stepInfo.getStartRow()});
                    excelMeta.setStartCol(new int[]{stepInfo.getStartCol()});
                    kettleConfig.setExcelMeta(excelMeta);
                    break;
                // 字段设置
                case "selectValue":
                    kettleConfig.setSelectValueMap(fieldMetaList);
                    break;
                //新增常量
                case "constant":
                    kettleConfig.setConstantMap(fieldMetaList);
                    break;
            }
        }
        kettleConfig.setScriptFile(job.getScriptPath());
        kettleConfig.setJobName(job.getJobName());
        kettleConfig.setBatchId(job.getBatchId());

        this.kettleConfig = kettleConfig;
    }

    @Override
    public void setCattleJob(CattleJob job) {
        this.cattleJob = job;
    }
}
