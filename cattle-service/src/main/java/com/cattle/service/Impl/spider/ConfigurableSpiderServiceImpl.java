package com.cattle.service.Impl.spider;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattle.service.api.spider.ConfigurableSpiderService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 爬虫执行类
 * @author lsj
 * @date 2021/03/10
 */
@Slf4j
@Service
public class ConfigurableSpiderServiceImpl implements ConfigurableSpiderService {

    private static final String COMMON_FIELD = "batch_id";

    @Autowired
    private DruidDataSource dataSource;

    /**
     * 保存爬取的数据
     */
    @Override
    public void saveData(List<LinkedHashMap<String, String>> datas, String tableName,LinkedHashMap<String, String> fields, String uuid) {
        log.info("start saveData");
        if (datas == null || datas.isEmpty()) {
            log.info("datas empty");
            return;
        }
        //判断表是否存在
        try {
            doSaveData(datas,tableName,fields, uuid);
        } catch (Exception e) {
            log.error("saveData error", e);
        }

    }

    @Override
    public LinkedHashMap<String, String> parseFields(String fieldsJson) {
        if (Strings.isNullOrEmpty(fieldsJson)) {
            return new LinkedHashMap<>();
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        JSONArray fields = JSON.parseArray(fieldsJson);
        for (Object fieldObj : fields) {
            JSONObject field = (JSONObject) fieldObj;
            map.put(field.get("key").toString(), field.get("value").toString());
        }
        return map;
    }

    private void doSaveData(List<LinkedHashMap<String, String>> datas, String tableName,LinkedHashMap<String, String> fields, String uuid) throws SQLException, ClassNotFoundException {
        //插入数据
        String sql = getSql(datas, tableName, fields,  uuid);
        log.info("doSaveData sql={}", sql);

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        try {
            statement.execute(sql);
        } catch (Exception e) {
            log.error("doSaveData error", e);
            try {
                //可能是因为表不存在 创建表试试
                createTable(tableName, statement, fields);
            } catch (Exception ex) {
                //其他线程可能已经创建了这个表
                log.error("doSaveData createTable error", ex);
            }
            //再执行一遍
            statement.execute(sql);
        } finally {
            statement.close();
            connection.close();
        }
    }

    /**
     * CREATE TABLE job
     (
     uuid CHAR(32) NOT NULL COMMENT '主键uuid',
     name VARCHAR(30) NULL DEFAULT NULL COMMENT '任务名',
     PRIMARY KEY (uuid)
     );
     * @param tableName
     * @param statement
     * @param fields
     */
    private void createTable(String tableName, Statement statement, LinkedHashMap<String, String> fields) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "\n");
        StringBuilder fieldDefs = new StringBuilder("( ");
        fieldDefs.append("`").append("id").append("`").append(" BIGINT(20) NOT NULL AUTO_INCREMENT ").append(",\n");
        fields.forEach((k, v) -> {
            StringBuilder fieldDef = new StringBuilder();
            //TODO 可能会出现默认字段长度不够
            fieldDef.append("`").append(k).append("`").append(" text ").append(",\n");
            fieldDefs.append(fieldDef);
        });
        fieldDefs.append("`").append(COMMON_FIELD).append("`").append(" CHAR(64) ").append(",\n");
        fieldDefs.append("`").append("create_time").append("`").append(" DATETIME ").append(",\n");
        fieldDefs.append("`").append("modify_time").append("`").append(" DATETIME ").append(",\n");
        fieldDefs.append("PRIMARY KEY (id) \n");
        fieldDefs.append(")");

        sql.append(fieldDefs);
        log.info("createTable sql={}", sql);
        statement.executeUpdate(sql.toString());
    }

    private void createTable(String tableName, LinkedHashMap<String, String> fields) throws SQLException, ClassNotFoundException {
        createTable(tableName,getConnection().createStatement(),fields);
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        // 创建连接
        return dataSource.getConnection();
    }

    private String getSql(List<LinkedHashMap<String, String>> datas, String tableName, LinkedHashMap<String, String> fields, String uuid) {
        StringBuilder fieldsStr = new StringBuilder();
        fields.forEach((k, v) -> {
            fieldsStr.append("`").append(k).append("`").append(",");
        });
        fieldsStr.append("`").append(COMMON_FIELD).append("`");//任务id字段
        fieldsStr.append(",`").append("create_time").append("`");
        fieldsStr.append(",`").append("modify_time").append("`");

        StringBuilder sql = new StringBuilder("INSERT INTO `" + tableName + "` (" + fieldsStr + ") VALUES ");

        StringBuilder values = new StringBuilder();
        datas.forEach((data) -> {
            StringBuilder sqlValue = new StringBuilder();
            data.forEach((k, v) -> {
                sqlValue.append("'").append(v).append("'").append(",");
            });
            sqlValue.append("'").append(uuid).append("'");
            sqlValue.append(",'").append(LocalDateTimeUtil.formatNormal(LocalDateTime.now())).append("'");
            sqlValue.append(",'").append(LocalDateTimeUtil.formatNormal(LocalDateTime.now())).append("'");
            values.append(",(").append(sqlValue).append(" )");
        });

        sql.append(values.substring(1, values.length()));

        return sql.toString();
    }

    public String buildPrepareSql(LinkedHashMap<String, String> field,String tableName){
        field.put(COMMON_FIELD,"");
        field.put("create_time","");
        field.put("modify_time","");
        StringBuilder fieldsStr = new StringBuilder();
        StringBuilder valStr = new StringBuilder();
        field.forEach((k, v) -> {
            fieldsStr.append("`").append(k).append("`").append(",");
            valStr.append("?").append(",");
        });

        StringBuilder sql = new StringBuilder("INSERT INTO `" + tableName + "` (" + fieldsStr.substring(0,fieldsStr.length() - 1) + ") VALUES ");
        sql.append("(").append(valStr.substring(0,valStr.length() - 1)).append(")");

        return sql.toString();
    }

    public void doPrepareSaveData(List<LinkedHashMap<String, String>> datas, String tableName,LinkedHashMap<String, String> fields, String uuid) throws SQLException, ClassNotFoundException {
        //插入数据
        String sql = buildPrepareSql(fields,tableName);
        log.info("doPrepareSaveData sql={}", sql);

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        for(LinkedHashMap<String,String> data : datas){
            int index = 1;
            for(String key : data.keySet()){
                String val = data.get(key);
                statement.setString(index,val);
                index ++;
            }
            statement.addBatch();
        }
        try {
            statement.executeBatch();
        } catch (Exception e) {
            log.error("doSaveData error", e);
            try {
                //可能是因为表不存在 创建表试试
                createTable(tableName, statement, fields);
            } catch (Exception ex) {
                //其他线程可能已经创建了这个表
                log.error("doSaveData createTable error", ex);
            }
            //再执行一遍
            statement.executeBatch();
        } finally {
            statement.close();
            connection.close();
        }
    }

}
