package com.cattle.service.Impl.kettle;

import com.cattle.entity.kettle.CattleKtrInfo;
import com.cattle.mapper.kettle.CattleKtrInfoMapper;
import com.cattle.service.api.kettle.CattleKtrInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CattleKtrInfoServiceImpl implements CattleKtrInfoService {

    @Resource
    private CattleKtrInfoMapper ktrInfoMapper;

    @Override
    public CattleKtrInfo selectById(Integer id) {
        return ktrInfoMapper.selectById(id);
    }
}
