package com.cattle.component.kettle.handler;

import com.cattle.common.context.ProcessContent;
import com.cattle.common.handler.ExecuteProcessHandler;
import com.cattle.component.kettle.KettleConfig;
import com.cattle.component.kettle.meta.DBMate;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.TransMeta;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * kettle 连接数据库修改
 */
public class DataBaseChangeHandler extends ExecuteProcessHandler {

    @Override
    public void executeContent(ProcessContent processContent) {
        TransMeta transMeta = (TransMeta) processContent.get("transMeta");
        KettleConfig kettleConfig = (KettleConfig) processContent.get("kettleConfig");
        Map<String,DBMate> dbList = kettleConfig.getDataBaseMetas();
        List<DatabaseMeta> list = transMeta.getDatabases();
        Optional.ofNullable(list).get().forEach(databaseMeta -> {
            if(dbList.containsKey(databaseMeta.getName())){
                DBMate dbMate = dbList.get(databaseMeta.getName());
                databaseMeta.setDBPort(dbMate.getPort());
                databaseMeta.setDBName(dbMate.getDatabase());
                databaseMeta.setHostname(dbMate.getHostName());
                databaseMeta.setUsername(dbMate.getUserName());
                databaseMeta.setPassword(dbMate.getPassword());
            }
        });
    }
}
