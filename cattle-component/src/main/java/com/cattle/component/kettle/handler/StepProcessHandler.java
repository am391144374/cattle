package com.cattle.component.kettle.handler;

import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.common.context.ProcessContent;
import com.cattle.component.kettle.step.BaseStepMeta;
import org.pentaho.di.trans.TransMeta;

import java.util.LinkedList;
import java.util.List;

/**
 * 步骤绑定
 * @author lsj
 */
public class StepProcessHandler extends ExecuteProcessHandler {

    private List<BaseStepMeta> baseStepMetaList = new LinkedList<>();
    private int index = 0;

    @Override
    public void executeContent(ProcessContent processContent) {
        TransMeta transMeta = (TransMeta) processContent.get("transMeta");
        for(BaseStepMeta stepMeta : baseStepMetaList){
            stepMeta.setTransMeta(transMeta);
            stepMeta.execute(processContent);
        }
    }

    public void addStepMeta(BaseStepMeta baseStepMeta,int index){
        baseStepMetaList.add(index,baseStepMeta);
    }

    public void addStepMeta(BaseStepMeta baseStepMeta){
        addStepMeta(baseStepMeta,index);
        index++;
    }

}
