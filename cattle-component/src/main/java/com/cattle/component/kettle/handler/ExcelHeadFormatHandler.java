package com.cattle.component.kettle.handler;

import cn.hutool.core.util.StrUtil;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContext;
import com.cattle.component.kettle.meta.ExcelMeta;
import com.cattle.component.kettle.meta.FieldMeta;
import com.cattle.component.kettle.excel.WriteExcel;

import java.util.List;
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
            processContext.putError(this,e);
        }finally {
            writeExcel.close();
        }
    }

}
