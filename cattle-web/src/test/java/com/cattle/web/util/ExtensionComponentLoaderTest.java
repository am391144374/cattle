package com.cattle.web.util;

import com.cattle.common.plugin.ExtensionComponentLoader;
import org.junit.Test;

public class ExtensionComponentLoaderTest {

    @Test
    public void loadDirectoryTest(){
        ExtensionComponentLoader componentLoader = new ExtensionComponentLoader();
        componentLoader.findComponentByPackage();
        System.out.println(componentLoader.getAllScriptType());
    }

    @Test
    public void scanPackage(){
        String COMPONENT_PACKAGE = "com.cattle.component";
    }

}
