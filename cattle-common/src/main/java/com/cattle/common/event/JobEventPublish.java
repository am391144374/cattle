package com.cattle.common.event;

import com.cattle.common.entity.CattleJob;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class JobEventPublish implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static void publishJobRun(CattleJob cattleJob){
        CattleJobEvent event = new CattleJobEvent(cattleJob);
        context.publishEvent(event);
    }

}
