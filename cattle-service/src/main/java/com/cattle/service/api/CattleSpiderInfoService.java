package com.cattle.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cattle.common.entity.CattleSpiderInfo;

import java.util.Map;

public interface CattleSpiderInfoService extends IService<CattleSpiderInfo> {

    IPage<CattleSpiderInfo> selectPageList(Integer offset, Integer limit, Map<String,Object> queryParam);

}
