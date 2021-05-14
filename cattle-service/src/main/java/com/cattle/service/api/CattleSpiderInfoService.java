package com.cattle.service.api;

import com.cattle.entity.CattleSpiderInfo;

public interface CattleSpiderInfoService {

    CattleSpiderInfo selectById(Integer spiderId);

    int insert(CattleSpiderInfo spiderInfo);

    int updateById(CattleSpiderInfo spiderInfo);

    int delete(Integer spiderId);

}
