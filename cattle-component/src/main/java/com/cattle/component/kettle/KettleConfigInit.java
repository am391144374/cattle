package com.cattle.component.kettle;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.util.EnvUtil;

import java.io.File;
import java.util.Arrays;

public class KettleConfigInit {

    public static void init() throws KettleException {
        File file = new File("D:\\文档\\公司\\科技厅\\大数据项目\\pdi-ce-9.1.0.0-324\\data-integration\\plugins");
        Arrays.stream(file.listFiles()).forEach(file1 -> {
            StepPluginType.getInstance().getPluginFolders()
                    .add(new PluginFolder(file1.getPath(),false,true));
        });
        // 转换元对象
        KettleEnvironment.init();// 初始化
        EnvUtil.environmentInit();
    }

}
