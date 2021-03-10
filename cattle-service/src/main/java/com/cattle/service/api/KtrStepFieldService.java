package com.cattle.service.api;

import com.cattle.entity.kettle.KtrStepField;

import java.util.List;

public interface KtrStepFieldService {

    public List<KtrStepField> selectFieldListByStepId(Integer stepId);

    public KtrStepField selectFieldListById(Integer fieldId);

    public int save(KtrStepField fieldInfo);

}
