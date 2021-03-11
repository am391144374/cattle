package com.cattle.component.kettle.step;

import cn.hutool.core.util.StrUtil;
import com.cattle.component.kettle.meta.ExcelMeta;
import com.cattle.component.kettle.meta.FieldMeta;
import com.cattle.component.kettle.excel.WriteExcel;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.pentaho.di.trans.steps.excelinput.ExcelInputMeta;
import org.pentaho.di.trans.steps.excelinput.SpreadSheetType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * kettle脚本步骤 excel导入
 * @author lsj
 */
public class ExcelInputStep extends BaseStepMeta {

    private ExcelMeta excelMeta;
    private List<FieldMeta> fieldMetaList;
    private String stepName;

    public ExcelInputStep(ExcelMeta excelMeta,List<FieldMeta> fieldMetaList,String stepName){
        this.excelMeta = excelMeta;
        this.fieldMetaList = fieldMetaList;
        this.stepName = stepName;
    }

    @Override
    public StepMeta buildCurrentStepMate() throws Exception {
        ExcelInputMeta excelInputMeta = new ExcelInputMeta();
        excelInputMeta.setFileName(conversionFileName(excelMeta.getFileName()));
        //字段构建
        ExcelInputField[] excelInputFields = Optional.ofNullable(fieldMetaList).get().stream().map(fieldMeta -> {
            ExcelInputField excelInputField = new ExcelInputField();
            if(StrUtil.isNotBlank(fieldMeta.getComment())){
                excelInputField.setName(fieldMeta.getName() + "-" + fieldMeta.getComment());
            }else{
                excelInputField.setName(fieldMeta.getName());
            }
            excelInputField.setType(ValueMetaInterface.TYPE_STRING);
            excelInputField.setTrimType(ValueMetaInterface.TRIM_TYPE_BOTH);
            return excelInputField;
        }).collect(Collectors.toList()).toArray(new ExcelInputField[fieldMetaList.size()]);
        excelInputMeta.setField(excelInputFields);
        excelInputMeta.setSheetName(excelMeta.getSheetName());
        excelInputMeta.setStartRow(excelMeta.getStartRow());
        excelInputMeta.setStartColumn(excelMeta.getStartCol());
        excelInputMeta.setSpreadSheetType(SpreadSheetType.getStpreadSheetTypeByDescription(SpreadSheetType.POI.getDescription()));
        return new StepMeta(registryID.getPluginId(StepPluginType.class,excelInputMeta), stepName, excelInputMeta);
    }

    private String[] conversionFileName(String[] oldFileName){
        String[] newFileName = new String[oldFileName.length];
        int index = 0;
        for(String fileName : oldFileName){
            int lastIndex = fileName.lastIndexOf(".");
            String preFileName = fileName.substring(0,lastIndex);
            String sufFileName = fileName.substring(lastIndex);
            newFileName[index] = preFileName + WriteExcel.newFileSuffix + sufFileName;
            index++;
        }
        return newFileName;
    }

}
