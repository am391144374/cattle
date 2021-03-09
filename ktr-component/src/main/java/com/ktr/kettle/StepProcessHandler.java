package com.ktr.kettle;

import com.ktr.ExecuteProcessHandler;
import com.ktr.context.ProcessContext;
import com.ktr.kettle.step.BaseStepMeta;
import org.pentaho.di.trans.TransMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 步骤绑定
 * @author lsj
 */
public class StepProcessHandler extends ExecuteProcessHandler {

    private List<BaseStepMeta> baseStepMetaList = new LinkedList<>();
    private int index = 0;

    @Override
    public void executeContent(ProcessContext processContext, Map<String,Object> variables) {
        TransMeta transMeta = (TransMeta) processContext.get("transMeta");
        for(BaseStepMeta stepMeta : baseStepMetaList){
            stepMeta.setTransMeta(transMeta);
            stepMeta.execute(processContext, variables);
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
