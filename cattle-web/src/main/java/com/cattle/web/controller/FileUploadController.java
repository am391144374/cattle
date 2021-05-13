package com.cattle.web.controller;

import com.cattle.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FileUploadController {

    String diskPath = "D:\\临时文件\\temp";

    @RequestMapping("/upload")
    public Object upload(MultipartFile file){
        if(file == null){
            return ResponseUtil.fail("不存在的文件");
        }

        String fileName = diskPath + File.separator+ file.getOriginalFilename();
        File localFile = new File(fileName);
        if(!localFile.exists()){
            localFile.mkdirs();
        }

        log.info("upload 文件上传成功！filePath:{}",fileName);

        try {
            file.transferTo(localFile);
            Map<String,Object> module = new HashMap<>();
            module.put("fileName",fileName);
            return ResponseUtil.success(module);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseUtil.fail("上传文件错误!");
    }

}
