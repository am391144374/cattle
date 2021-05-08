package com.cattle.web.config;

import com.cattle.service.api.ConfigurableSpiderService;
import com.cattle.service.api.RunLogService;
import com.cattle.web.CattleRun;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class CattleRunConfig {

    @Bean
    public CattleRun cattleRun(RunLogService runLogService, ConfigurableSpiderService spiderService){
        CattleRun cattleRun = new CattleRun(runLogService,spiderService);
        cattleRun.init();
        return cattleRun;
    }

}
