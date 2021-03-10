package com.ktr.kettle;

import cn.hutool.core.util.StrUtil;
import com.ktr.ExecuteProcessHandler;
import com.ktr.context.ProcessContext;
import com.ktr.ResultHelper;
import com.ktr.kettle.meta.ExcelMeta;
import com.ktr.kettle.meta.FieldMeta;
import com.ktr.kettle.excel.WriteExcel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * excel模板处理，设置字段头
 * @author lsj
 */
public class ExcelHeadFormatHandler extends ExecuteProcessHandler {

    private ExcelMeta excelMeta;
    private int writeHeadRowIndex;
    private List<FieldMeta> excelFieldMetas;

    public ExcelHeadFormatHandler(ExcelMeta excelMeta,List<FieldMeta> excelFieldMetas,int writeHeadRowIndex){
        this.excelMeta = excelMeta;
        this.excelFieldMetas = excelFieldMetas;
        this.writeHeadRowIndex = writeHeadRowIndex;
    }

    @Override
    public void executeContent(ProcessContext processContext) {
        WriteExcel writeExcel = new WriteExcel(excelMeta.getFileName(),excelMeta.getSheetName()[0]);
        try {
            writeExcel.writeHeadRow(writeHeadRowIndex,
                    excelFieldMetas.stream().map(fieldMate -> {
                        //todo excel字段设置规则，按执行脚本来适配
                        if(StrUtil.isBlank(fieldMate.getComment())){
                            return fieldMate.getName();
                        }
                        return fieldMate.getName() + "-" + fieldMate.getComment();
                    }).collect(Collectors.toList()));
            writeExcel.flushAll();
        }catch (Exception e){
            e.printStackTrace();
            ResultHelper.setException(processContext.getBatchId(),this,e);
        }finally {
            writeExcel.close();
        }
    }

}
