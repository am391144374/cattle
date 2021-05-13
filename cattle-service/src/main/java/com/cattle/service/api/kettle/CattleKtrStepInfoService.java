package com.cattle.service.api.kettle;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.entity.kettle.CattleKtrStep;

import java.util.List;

public interface CattleKtrStepInfoService {

    List<CattleKtrStep> selectStepInfoByKtrInfoId(Integer ktrInfoId);

    IPage<CattleKtrStep> selectStepInfoByKtrInfoIdPage(Integer ktrInfoId, Integer offset, Integer limit);

    CattleKtrStep selectStepInfoById(Integer stepId);

    int insert(CattleKtrStep stepInfo);

    int updateById(CattleKtrStep cattleKtrStep);

    int delete(Integer stepId);

}
