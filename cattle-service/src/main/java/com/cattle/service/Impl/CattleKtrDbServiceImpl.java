package com.cattle.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cattle.common.entity.CattleKtrDb;
import com.cattle.mapper.CattleKtrDbMapper;
import com.cattle.service.api.CattleKtrDbService;
import org.springframework.stereotype.Service;

@Service
public class CattleKtrDbServiceImpl extends ServiceImpl<CattleKtrDbMapper, CattleKtrDb> implements CattleKtrDbService {
}
