package com.cattle.web.controller;

import com.cattle.entity.kettle.KtrJobInfo;
import com.cattle.service.Impl.KtrRunFactory;
import com.cattle.service.api.KtrJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 执行job信息api
 */
@Controller
public class KtrJobController {

    @Autowired
    private KtrJobService ktrJobService;
    @Autowired
    private KtrRunFactory runFactory;

    @GetMapping("/execute")
    @ResponseBody
    public String executeJob(Integer jobId){
        KtrJobInfo ktrJobInfo = ktrJobService.buildExecuteJobInfo(jobId);
        if(ktrJobInfo != null){
            try {
                runFactory.putRunJob(ktrJobInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "调用成功";
    }

    @GetMapping("/list")
    @ResponseBody
    public KtrJobInfo list(Integer jobId){
        return ktrJobService.buildExecuteJobInfo(jobId);
    }

}
