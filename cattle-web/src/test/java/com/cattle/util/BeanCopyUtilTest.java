package com.cattle.util;

import cn.hutool.core.bean.BeanUtil;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.entity.spider.SpiderConfigurable;
import org.junit.jupiter.api.Test;

public class BeanCopyUtilTest {

    @Test
    public void beanCopy(){
        SpiderConfigurable configurable = new SpiderConfigurable();
        configurable.setTableName("hz_lp");
        configurable.setSpiderName("杭州楼盘");
        configurable.setListRegex("https://hz\\.newhouse\\.fang\\.com/house/s/b\\d+");
        configurable.setEntryUrl("https://hz.newhouse.fang.com/house/s/b91");
        configurable.setFieldsJson("[{\"index\":0,\"key\":\"name\",\"value\":\"//div[@class='nlc_details']//div[@class='nlcd_name']//a/text()\"},{\"index\":1,\"key\":\"address\",\"value\":\"//div[@class='address']//a/text()\"},{\"index\":2,\"key\":\"price\",\"value\":\"//div[@class='nhouse_price']/*[1]/text()\"}]");
        configurable.setXPathSelection(0);
        configurable.setThreadNum(2);

        SpiderConfig spiderConfig = new SpiderConfig();
        BeanUtil.copyProperties(configurable,spiderConfig,true);
        System.out.println(spiderConfig.toString());
    }

}
