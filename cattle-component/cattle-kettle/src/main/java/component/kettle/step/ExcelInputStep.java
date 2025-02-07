package component.kettle.step;

import cn.hutool.core.util.StrUtil;
import component.kettle.KettleConfig;
import component.kettle.excel.WriteExcel;
import component.kettle.meta.ExcelMeta;
import component.kettle.meta.FieldMeta;
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

    public static String stepName = "Excel输入";
    private KettleConfig kettleConfig;

    public ExcelInputStep(KettleConfig kettleConfig){
        this.kettleConfig = kettleConfig;
    }

    @Override
    public StepMeta buildCurrentStepMate() throws Exception {
        ExcelInputMeta excelInputMeta = new ExcelInputMeta();
        ExcelMeta excelMeta = kettleConfig.getExcelMeta();
        List<FieldMeta> fieldMetaList = kettleConfig.getSelectValueMap();
        excelInputMeta.setFileName(excelMeta.getFileName());
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
        //个性化设置，kettle脚本内能够获取文件名（地址+文件名），通过文件名获取年度数据
        excelInputMeta.setFileField("fileName");
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
