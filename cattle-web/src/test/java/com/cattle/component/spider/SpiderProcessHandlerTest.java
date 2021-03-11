package com.cattle.component.spider;

import com.cattle.service.Impl.spider.ConfigurableSpiderServiceImpl;
import com.cattle.service.api.spider.ConfigurableSpiderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@SpringBootTest
public class SpiderProcessHandlerTest {

    @Test
    public void testSpiderRun(){

    }

    @Test
    public void autoCreateTable(){
        ConfigurableSpiderService spiderService = new ConfigurableSpiderServiceImpl();
        List<LinkedHashMap<String, String>> datas = new ArrayList<>();
        LinkedHashMap<String,String> field = new LinkedHashMap<>();
        field.put("name","1");
        field.put("age","2");
        datas.add(field);
        datas.add(field);
        datas.add(field);
        datas.add(field);
        datas.add(field);
        datas.add(field);
        log.info(spiderService.buildPrepareSql(field,"table"));
        try {
            spiderService.doPrepareSaveData(datas,"test",field,"1234");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
