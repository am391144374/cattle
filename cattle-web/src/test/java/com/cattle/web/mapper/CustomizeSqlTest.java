package com.cattle.web.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cattle.mapper.CustomizeSqlMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class CustomizeSqlTest {

    @Resource
    private CustomizeSqlMapper customizeSqlMapper;

    @Test
    public void test(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("date","03-28");
        List<Map<String, Object>> list = customizeSqlMapper.selectAll("cs_sydw",queryWrapper);
        log.info("query num : {}",list.size());

        UpdateWrapper updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id","1");
        updateWrapper.set("rate",1);
        log.info("update sql : {}",updateWrapper.getSqlSet());
        int num = customizeSqlMapper.update("spider_movie_info",updateWrapper.getSqlSet(),updateWrapper);
        log.info("update num : {}",num);

        Map<String, Object> map = customizeSqlMapper.selectOne("cs_sydw",queryWrapper);
        map.forEach((k,v) -> {
            log.info("select one key : {}  value : {}",k,v);
        });
    }

}
