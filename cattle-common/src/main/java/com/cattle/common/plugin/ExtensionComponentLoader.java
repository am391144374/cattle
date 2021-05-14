package com.cattle.common.plugin;

import lombok.extern.slf4j.Slf4j;

import java.util.*;


/**
 * 注册脚本组件
 * 目前只扫描包路径，可以接口实现jar扫描
 */
@Slf4j
public class ExtensionComponentLoader extends AbstractScanPackage {

    private final String COMPONENT_PACKAGE = "com.cattle.component";
    final Map<String, Class<? extends ExecuteScriptInterface>> componentMaps = new HashMap<>();

    public void findComponentByPackage() {
        try {
            log.info("组件注册开始！");
            List<String> list = scanPackage(COMPONENT_PACKAGE, "class");
            initComponent(list);
            log.info("组件注册完成！");
        } catch (Exception e) {
            log.error("find component error:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initComponent(List<String> urls) {
        Optional.ofNullable(urls).get().forEach(url -> {
            String classPath = subClassPath(url, COMPONENT_PACKAGE);
            try {
                Class loadClass = Class.forName(classPath);
                if (ExecuteScriptInterface.class.isAssignableFrom(loadClass)) {
                    ExecuteScriptInterface scriptInterface = (ExecuteScriptInterface) loadClass.newInstance();
                    if (componentMaps.containsKey(scriptInterface.getScriptType())) {
                        log.error("存在相同的组件执行类型，请检查！ scriptType:{}", scriptInterface.getScriptType());
                        return;
                    }
                    log.info("初始化{}组件环境变量  #### start ####",scriptInterface.getScriptType());
                    scriptInterface.initEnv();
                    log.info("初始化{}组件环境变量  #### end ####",scriptInterface.getScriptType());
                    log.info("注册组件 scriptType:{}------className:{}", scriptInterface.getScriptType(), loadClass.getName());
                    componentMaps.put(scriptInterface.getScriptType(), loadClass);
                }
            } catch (ClassNotFoundException e) {
                log.error("class not found error :{}", e.getMessage());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    public Set<String> getAllScriptType() {
        return componentMaps.keySet();
    }

    public Class getComponentClass(String scriptType){
        if(!componentMaps.containsKey(scriptType)){
            return null;
        }
        return componentMaps.get(scriptType);
    }

}
