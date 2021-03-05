package com.ktr.handler.step;

import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;

public interface StepMetaConnectInterface {

    /**
     * 下一个执行步骤
     * @param stepMeta
     */
    void setNextStepMeta(StepMeta stepMeta);

    /**
     * 新增连接
     * @return
     */
    TransHopMeta buildTransHopMeta();

    /**
     * 构建一个执行步骤
     */
    StepMeta buildCurrentStepMate() throws Exception;

    StepMeta getCurrentStepMeta();

    void setTransMeta(TransMeta transMeta);

}
