package com.cattle.service.api.kettle;

import com.cattle.entity.kettle.CattleKtrInfo;

public interface CattleKtrInfoService {

    CattleKtrInfo selectById(Integer id);
}
