package com.cattle.component.kettle.step;

import com.cattle.common.context.ProcessContent;
import com.cattle.common.handler.ExecuteProcessHandler;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;

public abstract class BaseStepMeta extends ExecuteProcessHandler implements StepMetaConnectInterface{

    protected StepMeta currentStepMeta;
    protected StepMeta nextStepMeta;
    protected TransMeta transMeta;
    final protected PluginRegistry registryID = PluginRegistry.getInstance();

    @Override
    public TransHopMeta buildTransHopMeta() {
        if(currentStepMeta != null && nextStepMeta != null){
            return new TransHopMeta(currentStepMeta,nextStepMeta);
        }
        return null;
    }

    @Override
    public void setNextStepMeta(StepMeta stepMeta) {
        this.nextStepMeta = stepMeta;
    }

    @Override
    public void execute(ProcessContent processContent){
        try {
            currentStepMeta = buildCurrentStepMate();
            transMeta.addOrReplaceStep(currentStepMeta);
        } catch (Exception e) {
            e.printStackTrace();
            processContent.putError(currentStepMeta,e);
        }

    }

    @Override
    public StepMeta getCurrentStepMeta() {
        return currentStepMeta;
    }

    @Override
    public void setTransMeta(TransMeta transMeta) {
        this.transMeta = transMeta;
    }

    @Override
    public void executeContent(ProcessContent processContent) {

    }
}
