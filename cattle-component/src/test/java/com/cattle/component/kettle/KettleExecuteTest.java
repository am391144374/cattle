package com.cattle.component.kettle;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.Test;
import org.pentaho.di.core.exception.KettleException;

import java.util.List;

public class KettleExecuteTest {

    @Test
    public void kettlePluginInitTest(){
        try {
            KettlePluginInit.init();
        } catch (KettleException e) {
            e.printStackTrace();
        }
    }

}
