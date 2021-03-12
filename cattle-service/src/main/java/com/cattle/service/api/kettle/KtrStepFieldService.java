package com.cattle.service.api.kettle;

import com.cattle.entity.kettle.KtrField;

import java.util.List;

public interface KtrStepFieldService {

    public List<KtrField> selectFieldListByStepId(Integer stepId);

    public KtrField selectFieldListById(Integer fieldId);

    public int save(KtrField fieldInfo);

}
