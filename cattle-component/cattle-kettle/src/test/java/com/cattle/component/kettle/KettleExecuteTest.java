package com.cattle.component.kettle;

import component.kettle.KettlePluginInit;
import org.junit.Test;
import org.pentaho.di.core.exception.KettleException;

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
