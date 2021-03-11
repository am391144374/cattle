package com.cattle.service.api.spider;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 2019/8/30 15:11
 * yechangjun
 * 通用配置爬虫保存爬取的数据
 */
public interface ConfigurableSpiderService{
    /**
     * 保存爬取的数据
     *
     * @param datas 数据对象，每个map对应一个数据
     * @param uuid 任务id
     */
    void saveData(List<LinkedHashMap<String, String>> datas, String tableName,LinkedHashMap<String, String> fields, String uuid);

    String buildPrepareSql(LinkedHashMap<String, String> field,String tableName);

    /**
     * 预处理批量提交，提高数据效率
     * @param datas
     * @param tableName
     * @param fields
     * @param uuid
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    void doPrepareSaveData(List<LinkedHashMap<String, String>> datas, String tableName,LinkedHashMap<String, String> fields, String uuid) throws SQLException, ClassNotFoundException;
}
