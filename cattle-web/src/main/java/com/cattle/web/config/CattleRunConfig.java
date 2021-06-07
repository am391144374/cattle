package com.cattle.web.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.cattle.mapper.CustomizeSqlMapper;
import com.cattle.service.api.ConfigurableSpiderService;
import com.cattle.service.api.RunLogService;
import com.cattle.web.listener.FinishJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class CattleRunConfig {

    @Resource
    CustomizeSqlMapper customizeSqlMapper;

    @Bean
    public FinishJob cattleRun(RunLogService runLogService, ConfigurableSpiderService spiderService){
        FinishJob finishJob = new FinishJob(runLogService,spiderService,customizeSqlMapper);
        finishJob.init();
        return finishJob;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
