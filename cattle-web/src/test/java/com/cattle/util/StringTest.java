package com.cattle.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class StringTest {

    @Test
    public void splitYearTest(){
        String fileName = "2013";
        int sepIndex = fileName.lastIndexOf(File.separator);
        int extIndex = fileName.lastIndexOf(".");
        if(sepIndex > 0 && extIndex > 0){
            log.info(fileName.substring(sepIndex + 1,extIndex));
        }else if(extIndex > 0){
            log.info(fileName.substring(0,extIndex));
        }else{
            //都不成立则设置当前年
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            log.info(String.valueOf(calendar.get(Calendar.YEAR)));
        }
    }

}
