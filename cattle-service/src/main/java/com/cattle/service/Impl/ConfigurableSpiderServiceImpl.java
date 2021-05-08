package com.cattle.service.Impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.cattle.service.api.ConfigurableSpiderService;
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
import java.util.Set;

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
                createTable(tableName, statement, fields.keySet());
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
     * @param columns
     */
    private void createTable(String tableName, Statement statement, Set<String> columns) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "\n");
        StringBuilder fieldDefs = new StringBuilder("( ");
        fieldDefs.append("`").append("id").append("`").append(" BIGINT(20) NOT NULL AUTO_INCREMENT ").append(",\n");
        columns.forEach(k -> {
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
        createTable(tableName,getConnection().createStatement(),fields.keySet());
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

    public String buildPrepareSql(Set<String> columns,String tableName){
        StringBuilder fieldsStr = new StringBuilder();
        StringBuilder valStr = new StringBuilder();
        columns.forEach(k -> {
            fieldsStr.append("`").append(k).append("`").append(",");
            valStr.append("?").append(",");
        });
        fieldsStr.append("`").append(COMMON_FIELD).append("`,");
        fieldsStr.append("`").append("create_time").append("`,");
        fieldsStr.append("`").append("modify_time").append("`");
        valStr.append("?,?,?");

        StringBuilder sql = new StringBuilder("INSERT INTO `" + tableName + "` (" + fieldsStr + ") VALUES ");
        sql.append("(").append(valStr).append(")");

        return sql.toString();
    }

    public void doPrepareSaveData(List<LinkedHashMap<String, String>> datas, String tableName,Set<String> columns, String uuid) throws SQLException, ClassNotFoundException {
        //插入数据
        String sql = buildPrepareSql(columns,tableName);
        log.info("doPrepareSaveData sql={}", sql);

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        connection.setAutoCommit(false);
        addBatchValue(datas,uuid,statement,columns);
        try {
            statement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            if(e.getMessage().indexOf("doesn't exist") > 0){
                try {
                    log.info("{} doesn't exist,do create table method",tableName);
                    createTable(tableName, statement, columns);
                } catch (Exception ex) {
                    log.error("doSaveData createTable error", ex);
                    throw ex;
                }
                //没有表提交错误会导致数据被清空，需要重新添加
                addBatchValue(datas,uuid,statement,columns);
                //再执行一遍
                statement.executeBatch();
                connection.commit();
            }else{
                log.error("doSaveData error", e);
                throw e;
            }
        } finally {
            statement.close();
            connection.close();
        }
    }

    /**
     * 批量添加
     * @param datas
     * @param uuid
     * @param statement
     * @param columns
     * @throws SQLException
     */
    private void addBatchValue(List<LinkedHashMap<String, String>> datas,String uuid,PreparedStatement statement,Set<String> columns) throws SQLException {
        for(LinkedHashMap<String,String> data : datas){
            int index = 1;
            //有一种情况，页面内的数据非全匹配，所以设置默认值
            for(String key : columns){
                String val = data.getOrDefault(key,"");
                statement.setString(index,val);
                index ++;
            }
            statement.setString(index++,uuid);
            statement.setString(index++,LocalDateTimeUtil.formatNormal(LocalDateTime.now()));
            statement.setString(index++,LocalDateTimeUtil.formatNormal(LocalDateTime.now()));
            statement.addBatch();
        }
    }

    public void setDataSource(DruidDataSource dataSource){
        this.dataSource = dataSource;
    }

}
