package com.cattle.util;

import cn.hutool.core.bean.BeanUtil;
import com.cattle.component.spider.SpiderConfig;
import com.cattle.entity.spider.SpiderConfigurable;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Test
    public void notIN(){
        DateTimeFormatter inFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.format(inFormat));
//        String column_name = "city,region,tbis_count-在统孵化器数量(个),resident_companies_count-孵化器内企业总数(个),incubatees_count-在孵企业数(个),incubatees_employees_count-在孵企业从业人员数(个),incubatees_venture_amount-当年获得风险投资额(千元),incubatees_rd_input-在孵企业R&D投入(千元),table_name,type,year,id,batch_id,insert_time";
//        String excel_column = "city$15#String,region$50#String,tbis_count-在统孵化器数量(个)$15#Number,resident_companies_count-孵化器内企业总数(个)$15#Number,incubatees_count-在孵企业数(个)$15#Number,incubatees_employees_count-在孵企业从业人员数(个)$15#Number,incubatees_venture_amount-当年获得风险投资额(千元)$15#Number,incubatees_rd_input-在孵企业R&D投入(千元)$15#Number,table_name$20#String,type$10#String,year$-1#Integer,id$32#String,batch_id$32#String,insert_time$-1#Date";
//        String tableName = "test_table";
//        Map<String,String> excelField = new HashMap<>();
//        List<String> tableList = Arrays.asList(column_name.split(","));
//        for(String column : excel_column.split(",")){
//            String[] fieldAtt = column.split("\\$");
//            String fieldName = fieldAtt[0].split("-")[0];
//            if(!tableList.contains(fieldName)){
//                excelField.put(fieldAtt[0],fieldAtt[1]);
//            }
//        }
//        if(excelField.size() > 0){
//            StringBuffer sb = new StringBuffer();
//            for(String k : excelField.keySet()){
//                String v = excelField.get(k).toString();
//                String[] attr = v.split("#");
//                String typeDesc = attr[1];
//                int length = Integer.parseInt(attr[0]);
//                transform(sb,tableName,k,typeDesc,length);
//            }
//            System.out.println(sb.toString());
//        }

    }

    private void transform(StringBuffer sb,String tableName,String fieldName,String typeDesc,int length){
        String[] fieldArr = fieldName.split("-");
        String comment = fieldArr.length >= 2 ? fieldArr[1] : "";
        sb.append("alter table ")
                .append(tableName)
                .append(" add column ")
                .append(" `"+fieldArr[0] + "` ");
        if(length == -1){
            length = 10;
        }
        switch (typeDesc){
            case "String" :
                sb.append("varchar(" + length +") NULL COMMENT '" + comment + "'");
                break;
            case "Number" :
                sb.append("decimal(" + length+ ",2) NULL COMMENT '" + comment + "'");
                break;
            case "Integer":
                sb.append("int(" + length + ") NULL COMMENT '" + comment + "'");
                break;
            case "Date":
                sb.append("datetime NULL COMMENT '" + comment + "'");
                break;
        }
        sb.append(" ;");
    }

}
