package com.cattle.component.kettle.step;

import cn.hutool.core.util.StrUtil;
import com.cattle.component.kettle.meta.FieldMeta;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.constant.ConstantMeta;

import java.util.List;

/**
 * 新增常量
 * @author lsj
 */
public class ConstantStep extends BaseStepMeta {

    private List<FieldMeta> fieldMetaList;
    private String stepName;

    public ConstantStep(List<FieldMeta> fieldMetaList,String stepName,long batchId){
        this.fieldMetaList = fieldMetaList;
        this.stepName = stepName;
        addBatchId(fieldMetaList,batchId);
    }

    @Override
    public StepMeta buildCurrentStepMate() throws Exception {
        ConstantMeta constantMeta = new ConstantMeta();
        String[] currency = new String[fieldMetaList.size()];
        String[] decimal = new String[fieldMetaList.size()];
        String[] group = new String[fieldMetaList.size()];
        String[] fieldFormat = new String[fieldMetaList.size()];
        String[] value = new String[fieldMetaList.size()];
        String[] fieldName = new String[fieldMetaList.size()];
        String[] fieldType = new String[fieldMetaList.size()];
        int[] fieldLength = new int[fieldMetaList.size()];
        int[] fieldPrecision = new int[fieldMetaList.size()];
        boolean[] setEmptyString = new boolean[fieldMetaList.size()];

        int index = 0;
        for (FieldMeta fieldMeta : fieldMetaList){
            if(StrUtil.isNotBlank(fieldMeta.getValue())){
                value[index] = fieldMeta.getValue();
                setEmptyString[index] = false;
            }else{
                value[index] = "";
                setEmptyString[index] = true;
            }
            fieldName[index] = fieldMeta.getName();
            fieldType[index] = fieldMeta.getType();
            fieldLength[index] = fieldMeta.getLength();
            fieldPrecision[index] = fieldMeta.getPrecision();
            currency[index] = "";
            decimal[index] = "";
            group[index] = "";
            fieldFormat[index] = "";
            index++;
        }
        constantMeta.setValue(value);
        constantMeta.setFieldName(fieldName);
        constantMeta.setFieldLength(fieldLength);
        constantMeta.setEmptyString(setEmptyString);
        constantMeta.setFieldType(fieldType);
        constantMeta.setFieldPrecision(fieldPrecision);
        constantMeta.setCurrency(currency);
        constantMeta.setFieldFormat(fieldFormat);
        constantMeta.setDecimal(decimal);
        constantMeta.setGroup(group);

        return new StepMeta(registryID.getPluginId(StepPluginType.class,constantMeta), stepName, constantMeta);
    }

    private void addBatchId(List<FieldMeta> fieldMetaList,long batchId){
        fieldMetaList.add(FieldMeta.builder().name("batch_id").type("String").length(64).value(String.valueOf(batchId)).build());
    }
}
