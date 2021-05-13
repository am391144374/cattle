package com.cattle.service.api.kettle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.entity.kettle.CattleKtrField;

import java.util.List;

public interface CattleKtrStepFieldService {

    List<CattleKtrField> selectFieldListByStepId(Integer stepId);

    CattleKtrField selectFieldById(Integer fieldId);

    int insert(CattleKtrField fieldInfo);

    IPage<CattleKtrField> selectFieldListPageByStepId(Integer stepId,Integer offset,Integer limit);

    int updateById(CattleKtrField cattleKtrField);

    int delete(Integer stepId);

}
