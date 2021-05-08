package com.cattle.service.api.kettle;

import com.cattle.entity.kettle.CattleKtrField;

import java.util.List;

public interface KtrStepFieldService {

    public List<CattleKtrField> selectFieldListByStepId(Integer stepId);

    public CattleKtrField selectFieldListById(Integer fieldId);

    public int save(CattleKtrField fieldInfo);

}
