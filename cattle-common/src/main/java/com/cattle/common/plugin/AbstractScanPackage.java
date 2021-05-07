package com.cattle.common.plugin;


import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;

@Slf4j
public abstract class AbstractScanPackage {

    private List<String> fileList = new LinkedList<>();

    public List<URL> scanUrlByPackage(String packagePath) {
        log.info("扫描路径：{}",packagePath);
        List<URL> urlPaths = new ArrayList<>();
        String path = packagePath.replace(".", File.separator);
        EnumerationIter<URL> dirs = ResourceUtil.getResourceIter(path);
        while (dirs.hasNext()) {
            URL url = dirs.next();
            urlPaths.add(url);
        }
        return urlPaths;
    }

    public String subClassPath(String filePath,String packagePath){
        filePath = filePath.replace(File.separator,".");
        int lastPackageIndex = filePath.lastIndexOf(packagePath);
        String lastClassName = filePath.substring(lastPackageIndex).replace(File.separator,".");
        return lastClassName.substring(0,lastClassName.length() - 6);
    }

    /**
     *
     * @param reg 文件结尾匹配
     * @throws Exception
     */
    public List<String> scanPackage(String rootPackages,String reg) throws Exception {
        if(rootPackages == null){
            throw new NullPointerException("rootPackage is null");
        }
        log.info("扫描目录:{},扫描匹配：{}",rootPackages,reg);
        List<URL> urls = scanUrlByPackage(rootPackages);
        for(URL url : urls){
            String protocol = url.getProtocol();
            if("file".equals(protocol)){
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findFiles(filePath,reg);
            }else if("jar".equals(protocol)){
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                Enumeration<JarEntry> jarEntrys = jarURLConnection.getJarFile().entries();
                while (jarEntrys.hasMoreElements()){
                    JarEntry jarEntry = jarEntrys.nextElement();
                    findJarFiles(jarEntry,reg);
                }
            }
        }
        return fileList;
    }

    /**
     * 解析file
     * @param filePath
     * @param reg
     */
    private void findFiles(String filePath,String reg){
        File parentFile = new File(filePath);
        if(!parentFile.exists()){
            return;
        }
        File[] files = parentFile.listFiles(file -> (parentFile.isDirectory()) || parentFile.getName().endsWith(reg));
        for(File file : files){
            if(file.isDirectory()){
                findFiles(file.getPath(),reg);
            }else if(file.getName().endsWith(reg)){
                fileList.add(file.getPath());
            }
        }
    }

    /**
     * 解析jar地址
     * @param filePath
     * @param reg
     */
    private void findJarFiles(JarEntry filePath,String reg){
        if(filePath == null){
            return;
        }
        if(filePath.getName().endsWith(reg)){
            fileList.add(filePath.getName());
        }
    }


}
