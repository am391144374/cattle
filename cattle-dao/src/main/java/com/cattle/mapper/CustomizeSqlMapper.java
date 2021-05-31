package com.cattle.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 如何使用
 * @see com.cattle.web.mapper.CustomizeSqlTest
 * 自定义sql查询
 */
public interface CustomizeSqlMapper  {

    @Select("select * from ${tableName} ${ew.customSqlSegment}")
    List<Map<String,Object>> selectAll(@Param(value = "tableName") String tableName
            ,@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     *
     * @param tableName
     * @param setSql
     * @param wrapper
     * @return
     */
    @Update("update ${tableName} set ${setSql} ${ew.customSqlSegment}")
    int update(@Param(value = "tableName") String tableName,@Param(value = "setSql") String setSql
            ,@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select * from ${tableName} ${ew.customSqlSegment} limit 1")
    Map<String,Object> selectOne(@Param(value = "tableName") String tableName
            ,@Param(Constants.WRAPPER) Wrapper wrapper);

}
