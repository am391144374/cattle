package com.cattle.service.api.kettle;


import com.cattle.entity.kettle.CattleKtrStep;

import java.util.List;

public interface KtrStepInfoService {

    public List<CattleKtrStep> selectStepInfoByKtrId(Integer jobId);

    public CattleKtrStep selectStepInfoById(Integer stepId);

    public int save(CattleKtrStep stepInfo);

}
