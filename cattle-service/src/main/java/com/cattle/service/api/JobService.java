package com.cattle.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.entity.CattleJob;

import java.util.Map;

public interface JobService {

    CattleJob selectById(Integer jobId);

    CattleJob buildExecuteJobInfo(Integer jobId);

    IPage<CattleJob> selectPage(Integer offset, Integer limit, Map<String,Object> queryMap);

    int insert(CattleJob cattleJob);

    int updateById(CattleJob cattleJob);

    int delete(Integer jobId);
}
