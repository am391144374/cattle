package com.cattle.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.cattle.service.api.ConfigurableSpiderService;
import com.cattle.service.api.RunLogService;
import com.cattle.web.CattleRun;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class CattleRunConfig {

    @Bean
    public CattleRun cattleRun(RunLogService runLogService, ConfigurableSpiderService spiderService){
        CattleRun cattleRun = new CattleRun(runLogService,spiderService);
        cattleRun.init();
        return cattleRun;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
