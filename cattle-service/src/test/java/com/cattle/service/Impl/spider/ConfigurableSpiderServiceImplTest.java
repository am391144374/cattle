package com.cattle.service.Impl.spider;

import com.cattle.component.spider.service.ConfigurableSpiderService;
import com.cattle.component.spider.service.Impl.ConfigurableSpiderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import java.util.LinkedHashMap;

@Slf4j
public class ConfigurableSpiderServiceImplTest {

    public void testBuildPrepareSql() {
        ConfigurableSpiderService spiderService = new ConfigurableSpiderServiceImpl();
        LinkedHashMap<String,String> field = new LinkedHashMap<>();
        field.put("name","");
        field.put("age","");
        log.info(spiderService.buildPrepareSql(field.keySet(),"table"));
    }
}