package com.cattle.service.api;

import com.cattle.entity.kettle.KtrJobInfo;

public interface KtrJobService {

    KtrJobInfo selectById(Integer jobId);

    KtrJobInfo buildExecuteJobInfo(Integer jobId);

}
