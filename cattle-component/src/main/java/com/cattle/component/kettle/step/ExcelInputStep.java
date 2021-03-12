package com.cattle.component.kettle.step;

import cn.hutool.core.util.StrUtil;
import com.cattle.component.kettle.KettleConfig;
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

    private List<FieldMeta> fieldMetaList;
    public static String stepName = "Excel输入";
    private KettleConfig kettleConfig;

    public ExcelInputStep(KettleConfig kettleConfig){
        fieldMetaList = kettleConfig.getSelectValueMap();
        this.kettleConfig = kettleConfig;
    }

    @Override
    public StepMeta buildCurrentStepMate() throws Exception {
        ExcelInputMeta excelInputMeta = new ExcelInputMeta();
        excelInputMeta.setFileName(conversionFileName(kettleConfig.getFileName()));
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
        excelInputMeta.setSheetName(kettleConfig.getSheetName());
        excelInputMeta.setStartRow(kettleConfig.getStartRow());
        excelInputMeta.setStartColumn(kettleConfig.getStartCol());
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
