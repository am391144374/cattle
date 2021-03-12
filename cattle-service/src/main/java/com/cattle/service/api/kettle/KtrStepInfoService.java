package com.cattle.service.api.kettle;


import com.cattle.entity.kettle.KtrStep;

import java.util.List;

public interface KtrStepInfoService {

    public List<KtrStep> selectStepInfoByJobId(Integer jobId);

    public KtrStep selectStepInfoById(Integer stepId);

    public int save(KtrStep stepInfo);

}
