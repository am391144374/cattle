package com.cattle.common.plugin;

import org.junit.Test;

public class ExtensionComponentLoaderTest {

    @Test
    public void loadDirectoryTest(){
        ExtensionComponentLoader.loadDirectory(ExtensionComponentLoader.COMPONENT_DIRECTORY);
    }

}
