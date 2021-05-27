package com.cattle.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cattle.common.entity.*;
import com.cattle.mapper.JobMapper;
import com.cattle.service.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;
    @Autowired
    private CattleKtrInfoService ktrInfoService;
    @Autowired
    private CattleKtrStepInfoService stepInfoService;
    @Autowired
    private CattleKtrStepFieldService stepFieldService;
    @Autowired
    private CattleSpiderInfoService spiderInfoService;

    /**
     * 查询job信息
     * @param jobId
     * @return
     */
    public CattleJob selectById(Integer jobId){
        return jobMapper.selectById(jobId);
    }

    /**
     * 构建执行job
     * @param jobId
     * @return
     */
    public CattleJob buildExecuteJobInfo(Integer jobId){
        CattleJob jobInfo = selectById(jobId);
        if(jobInfo == null){
            return null;
        }
        switch (jobInfo.getScriptType()){
            case "spider":
                CattleSpiderInfo spiderInfo = spiderInfoService.getById(jobInfo.getRelateId());
                jobInfo.setSpiderInfo(spiderInfo);
                break;
            case "kettle":
                CattleKtrStep stepInfo = stepInfoService.selectStepInfoById(jobInfo.getRelateId());
                List<CattleKtrField> stepFields = stepFieldService.selectFieldListByStepId(stepInfo.getStepId());
                CattleKtrInfo cattleKtrInfo = ktrInfoService.selectById(stepInfo.getKtrInfoId());
                stepInfo.setFieldList(stepFields);
                stepInfo.setKtrInfo(cattleKtrInfo);
                jobInfo.setStepInfo(stepInfo);
                break;
        }
        return jobInfo;
    }

    @Override
    public IPage<CattleJob> selectPage(Integer offset, Integer limit, Map<String, Object> queryMap) {
        Page<CattleJob> page = new Page(offset,limit);
        QueryWrapper<CattleJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Optional.ofNullable(queryMap).orElse(new HashMap<>()).forEach((column, value) -> {
            if(StrUtil.isNotBlank(value.toString())){
                queryWrapper.eq(StrUtil.toUnderlineCase(column),value);
            }
        });
        return jobMapper.selectPage(page,queryWrapper);
    }

    @Override
    public int insert(CattleJob cattleJob) {
        return jobMapper.insert(cattleJob);
    }

    @Override
    public int updateById(CattleJob cattleJob) {
        return jobMapper.updateById(cattleJob);
    }

    @Override
    public int delete(Integer jobId) {
        return jobMapper.deleteById(jobId);
    }

}
