package com.cattle.web;

import cn.hutool.core.util.IdUtil;
import com.cattle.entity.CattleJob;
import com.cattle.kettle.KettleRunner;
import com.cattle.entity.kettle.KtrStepField;
import com.cattle.entity.kettle.KtrStepInfo;
import com.cattle.kettle.config.KettleConfig;
import com.cattle.kettle.step.StepTypeInterface;
import com.cattle.kettle.meta.ExcelMeta;
import com.cattle.kettle.meta.FieldMeta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KtrRunFactory {

    private static KettleRunner runner;
    private static KtrRunFactory runFactory;
    private final String scriptFilePath = "D:\\code\\new work space from git\\ktr\\doc\\科技企业孵化\\省-科技企业孵化.ktr";

    public KtrRunFactory(){
        runner = new KettleRunner();
    }

    public KettleConfig putRunJob(CattleJob jobInfo) throws InterruptedException {
        KettleConfig kettleConfig = new KettleConfig();
        List<KtrStepInfo> stepInfoList = jobInfo.getStepInfoList();
        for(KtrStepInfo stepInfo : stepInfoList){
            String stepType = stepInfo.getStepType();
            List<FieldMeta> fieldMetaList = new ArrayList<>();
            List<KtrStepField> stepFields = stepInfo.getFieldList();
            stepFields.forEach(stepField -> {
                FieldMeta fieldMeta = FieldMeta.builder()
                        .comment(stepField.getComment())
                        .length(stepField.getLength())
                        .name(stepField.getFieldName())
                        .type(stepField.getFieldType())
                        .precision(stepField.getPrecision())
                        .value(stepField.getDefaultValue()).build();
                fieldMetaList.add(fieldMeta);
            });
            if(stepType.equals(StepTypeInterface.FieldBelongType.CONSTANT.getName())){
                kettleConfig.putFieldMeta(StepTypeInterface.FieldBelongType.CONSTANT,fieldMetaList);
            }else if(stepType.equals(StepTypeInterface.FieldBelongType.SELECT_VALUE.getName())){
                kettleConfig.putFieldMeta(StepTypeInterface.FieldBelongType.SELECT_VALUE,fieldMetaList);
            }else if(stepInfo.equals(StepTypeInterface.FieldBelongType.EXCEL_IMPORT)){
                kettleConfig.putFieldMeta(StepTypeInterface.FieldBelongType.EXCEL_IMPORT,fieldMetaList);
                ExcelMeta excelMeta = ExcelMeta.builder()
                        .fileName(jobInfo.getFileList())
                        .sheetName(new String[]{stepInfo.getSheetName()})
                        .startRow(new int[]{stepInfo.getStartRow()})
                        .startCol(new int[]{stepInfo.getStartCol()})
                        .build();
                kettleConfig.setExcelMeta(excelMeta);
            }else{
                continue;
            }
        }

        kettleConfig.setScriptFile(scriptFilePath);
        kettleConfig.setJobName(jobInfo.getJobName());
        kettleConfig.setWriteHeadRowIndex(2);
        kettleConfig.setBatchId(IdUtil.getSnowflake(1,1).nextId());
        runner.putJob(kettleConfig);

        return kettleConfig;
    }

}
