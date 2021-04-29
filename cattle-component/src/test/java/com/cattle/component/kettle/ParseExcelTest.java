package com.cattle.component.kettle;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.Test;

import java.util.List;

public class ParseExcelTest {

    /**
     *
     * 解析合并单元格
     */
    @Test
    public void parseMergeCellsTest(){
        ExcelReader excelReader = ExcelUtil.getReader("D:\\临时文件\\01-16.xls");
        List<List<Object>> list = excelReader.read();
        list.forEach(row -> {
            row.forEach(str -> {
                System.out.print(str + " - ");
            });
            System.out.println();
        });
    }

}
