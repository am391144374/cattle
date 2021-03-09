package com.ktr.kettle.step;

import cn.hutool.core.util.StrUtil;
import com.ktr.kettle.meta.FieldMeta;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.selectvalues.SelectMetadataChange;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SelectValuesStep extends BaseStepMeta{

    private List<FieldMeta> fieldMetaList;
    private String stepName;

    public SelectValuesStep(List<FieldMeta> fieldMetaList,String stepName){
        this.fieldMetaList = fieldMetaList;
        this.stepName = stepName;
    }

    @Override
    public StepMeta buildCurrentStepMate() throws Exception {
        //字段选择
        SelectValuesMeta selectValuesMeta = new SelectValuesMeta();
        //元数据
        SelectMetadataChange[] selectMetadataChanges = Optional.ofNullable(fieldMetaList).get().stream().map(fieldMeta -> {
            SelectMetadataChange metadataChange = new SelectMetadataChange(null);
            if(StrUtil.isNotBlank(fieldMeta.getComment())){
                metadataChange.setName(fieldMeta.getName() + "-" + fieldMeta.getComment());
            }else{
                metadataChange.setName(fieldMeta.getName());
            }
            metadataChange.setType(fieldMeta.getType());
            metadataChange.setLength(fieldMeta.getLength());
            metadataChange.setPrecision(fieldMeta.getPrecision());
            return metadataChange;
        }).collect(Collectors.toList()).toArray(new SelectMetadataChange[fieldMetaList.size()]);

        selectValuesMeta.setMeta(selectMetadataChanges);
        return new StepMeta(registryID.getPluginId(StepPluginType.class,selectValuesMeta), stepName, selectValuesMeta);
    }

}
