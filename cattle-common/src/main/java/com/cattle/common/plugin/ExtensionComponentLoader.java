package com.cattle.common.plugin;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册脚本组件
 */
@Slf4j
public class ExtensionComponentLoader {

    public static final String COMPONENT_DIRECTORY = "MATE-INF/cattle/";
    private final String COMPONENT_PACKAGE = "com.cattle.component";

    private Map<String,Class<? extends ExecuteScriptInterface>> scriptMaps = new HashMap<>();

    public static void loadDirectory(String dir){
        String fileName = dir + ExecuteScriptInterface.class.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = getClassLoader(ExtensionComponentLoader.class);
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    System.out.println(resourceURL.toString());
                }
            }
        } catch (Throwable t) {
            log.error("Exception occurred when loading extension class (interface: " +
                      ", description file: " + fileName + ").", t);
        }
    }

    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = clazz.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }

        return cl;
    }
}
