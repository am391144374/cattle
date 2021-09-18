package component.kettle.handler;//package component.kettle.handler;
//
//import cn.hutool.core.util.StrUtil;
//import com.cattle.common.handler.ExecuteProcessHandler;
//import com.cattle.common.context.ProcessContext;
//import component.kettle.KettleConfig;
//import component.kettle.meta.ExcelMeta;
//import component.kettle.meta.FieldMeta;
//import component.kettle.excel.WriteExcel;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * excel模板处理，设置字段头
// * @author lsj
// */
//public class ExcelHeadFormatHandler extends ExecuteProcessHandler {
//
//    private List<FieldMeta> excelFieldMetas;
//    private KettleConfig kettleConfig;
//
//    public ExcelHeadFormatHandler(KettleConfig kettleConfig){
//        this.excelFieldMetas = kettleConfig.getSelectValueMap();
//        this.kettleConfig = kettleConfig;
//    }
//
//    @Override
//    public void executeContent(ProcessContext processContext) {
//        WriteExcel writeExcel = new WriteExcel(kettleConfig.getFileName(),kettleConfig.getSheetName()[0]);
//        try {
//            writeExcel.writeHeadRow(kettleConfig.getWriteHeadRowIndex(),
//                    excelFieldMetas.stream().map(fieldMate -> {
//                        //todo excel字段设置规则，按执行脚本来适配
//                        if(StrUtil.isBlank(fieldMate.getComment())){
//                            return fieldMate.getName();
//                        }
//                        return fieldMate.getName() + "-" + fieldMate.getComment();
//                    }).collect(Collectors.toList()));
//            writeExcel.flushAll();
//        }catch (Exception e){
//            e.printStackTrace();
//            processContext.putError(this,e);
//        }finally {
//            writeExcel.close();
//        }
//    }
//
//}
