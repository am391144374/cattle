package com.cattle.service.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.entity.CattleKtrStep;

import java.util.List;

public interface CattleKtrStepInfoService {

    List<CattleKtrStep> selectStepInfoByKtrInfoId(Integer ktrInfoId);

    IPage<CattleKtrStep> selectStepInfoByKtrInfoIdPage(Integer ktrInfoId, Integer offset, Integer limit);

    CattleKtrStep selectStepInfoById(Integer stepId);

    int insert(CattleKtrStep stepInfo);

    int updateById(CattleKtrStep cattleKtrStep);

    int delete(Integer stepId);

    CattleKtrStep selectOne(QueryWrapper<CattleKtrStep> queryWrapper);

    List<CattleKtrStep> selectList(QueryWrapper<CattleKtrStep> queryWrapper);

}
