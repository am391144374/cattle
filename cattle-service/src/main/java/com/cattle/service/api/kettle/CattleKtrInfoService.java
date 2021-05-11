package com.cattle.service.api.kettle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.entity.kettle.CattleKtrInfo;

import java.util.Map;

public interface CattleKtrInfoService {

    CattleKtrInfo selectById(Integer id);

    IPage<CattleKtrInfo> selectPage(Integer offset, Integer limit, Map<String,Object> queryMap);
}
