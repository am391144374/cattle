package com.cattle.service.Impl.spider;

import com.cattle.service.api.spider.ConfigurableSpiderService;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import java.util.LinkedHashMap;

@Slf4j
public class ConfigurableSpiderServiceImplTest extends TestCase {

    public void testBuildPrepareSql() {
        ConfigurableSpiderService spiderService = new ConfigurableSpiderServiceImpl();
        LinkedHashMap<String,String> field = new LinkedHashMap<>();
        field.put("name","");
        field.put("age","");
        log.info(spiderService.buildPrepareSql(field,"table"));
    }
}