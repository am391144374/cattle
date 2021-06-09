package com.cattle.component.kettle;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.util.EnvUtil;

import java.io.File;
import java.util.Arrays;

@Slf4j
public class KettlePluginInit {

    public static boolean initFlag = false;

    public static void init() throws KettleException {
        if(!initFlag){
            log.info("kettle plugins start init ...");
            initFlag = true;
//            ClassPathResource classPathResource = new ClassPathResource("/plugins");
//            File file = new File(classPathResource.getAbsolutePath());
            File file = new File("D:\\code\\new work space from git\\Cattle\\cattle-component\\src\\main\\resources\\plugins");
            for(File file1 : file.listFiles()){
                StepPluginType.getInstance().getPluginFolders()
                        .add(new PluginFolder(file1.getPath(),false,true));
            }
            // 转换元对象
            KettleEnvironment.init();// 初始化
            EnvUtil.environmentInit();
            log.info("kettle plugins init finish ...");
        }
    }
}
