package com.cattle.common.event;

import com.cattle.common.entity.CattleJob;
import org.springframework.context.ApplicationEvent;

public class CattleJobEvent extends ApplicationEvent {

    private CattleJob cattleJob;

    public CattleJobEvent(CattleJob cattleJob) {
        super(cattleJob);
        this.cattleJob = cattleJob;
    }

    public CattleJob getCattleJob(){
        return cattleJob;
    }
}
