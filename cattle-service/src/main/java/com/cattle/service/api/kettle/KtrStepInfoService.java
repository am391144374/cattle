package com.cattle.service.api.kettle;

import com.cattle.entity.kettle.KtrStepInfo;

import java.util.List;

public interface KtrStepInfoService {

    public List<KtrStepInfo> selectStepInfoByJobId(Integer jobId);

    public KtrStepInfo selectStepInfoById(Integer stepId);

    public int save(KtrStepInfo stepInfo);

}
