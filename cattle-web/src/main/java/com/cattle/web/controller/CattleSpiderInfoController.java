package com.cattle.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cattle.common.util.ResponseUtil;
import com.cattle.component.spider.parse.HtmlCleanerParse;
import com.cattle.component.spider.parse.XpathParse;
import com.cattle.component.spider.parse.XsoupParse;
import com.cattle.entity.CattleSpiderInfo;
import com.cattle.entity.module.SpiderTestModule;
import com.cattle.service.api.CattleSpiderInfoService;
import com.cattle.web.util.HttpUtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.selector.Html;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spider")
public class CattleSpiderInfoController{

    @Autowired
    private CattleSpiderInfoService spiderInfoService;

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1",required = false) Integer offset,
                       @RequestParam(defaultValue = "10",required = false) Integer limit,
                       @RequestParam(required = false) String queryJson){
        Map<String,Object> queryMap = null;
        if(queryJson != null){
            queryMap = JSONObject.parseObject(queryJson, HashMap.class);
        }
        IPage page = spiderInfoService.selectPageList(offset,limit,queryMap);
        return ResponseUtil.success(page);
    }

    @PostMapping("/add")
    public Object add(@RequestBody CattleSpiderInfo cattleSpiderInfo){
        QueryWrapper<CattleSpiderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("table_name",cattleSpiderInfo.getTableName());
        if(spiderInfoService.getOne(queryWrapper) != null){
            return ResponseUtil.fail("已经存在的数据库表信息");
        }
        cattleSpiderInfo.setModifyTime(new Date());
        if(spiderInfoService.save(cattleSpiderInfo)){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("add error");
    }

    @PostMapping("/edit")
    public Object edit(@NotNull @RequestBody CattleSpiderInfo cattleSpiderInfo){
        if(spiderInfoService.updateById(cattleSpiderInfo)){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("edit error");
    }

    @GetMapping("/detail")
    public Object detail(@NotNull Integer id){
        CattleSpiderInfo cattleSpiderInfo = spiderInfoService.getById(id);
        return ResponseUtil.success(cattleSpiderInfo);
    }

    @DeleteMapping("/remove")
    public Object remove(@NotNull Integer id){
        if(spiderInfoService.removeById(id)){
            return ResponseUtil.success("");
        }
        return ResponseUtil.fail("不存在的Id");
    }

    /**
     * 测试列表页和字段xpath
     * @param debugType  list or field
     * @param spiderTestModule
     * @return
     */
    @PostMapping("/test/{debugType}")
    public Object debugListRegexUrl(@PathVariable String debugType,@RequestBody SpiderTestModule spiderTestModule){
        String entryUrl = spiderTestModule.getEntryUrl();
        if(StrUtil.isBlank(entryUrl)){
            return ResponseUtil.fail("入口页为空！");
        }
        try {
            Html baseHtml = HttpUtl.downLoad(entryUrl);
            XpathParse xpathParse = getXpathParse(spiderTestModule.getXPathSelection());
            switch (debugType){
                case "listRegex":
                    return ResponseUtil.success(baseHtml.links().regex(spiderTestModule.getListRegex()).all());
                case "listField":
                    return ResponseUtil.success(xpathParse.xpath(baseHtml,spiderTestModule.getListFieldXPath()));
                case "contentXpath":
                    return ResponseUtil.success(xpathParse.xpath(baseHtml,spiderTestModule.getContentXpath()));
                case "contentField" :
                    List<String> list = xpathParse.xpath(baseHtml,spiderTestModule.getContentXpath());
                    if(list.size() > 0){
                        Html contentHtml = HttpUtl.downLoad(list.get(0));
                        return ResponseUtil.success(xpathParse.xpath(contentHtml,spiderTestModule.getContentFieldXPath()));
                    }else {
                        return ResponseUtil.fail("不存在的正文页内容");
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseUtil.fail("调试错误！");
    }

    private XpathParse getXpathParse(int selectNum){
        if(selectNum == 0){
            return new HtmlCleanerParse();
        }else if(selectNum == 1){
            return new XsoupParse();
        }
        return null;
    }

}
