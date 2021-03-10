package com.cattle.context;

import java.util.HashMap;

/**
 * process执行上下文
 * @author lsj
 */
public class ProcessContext extends HashMap<String,Object> {

    /** 批次ID */
    private long batchId;

    private final String CONNECTION_NAME = "yearbook";

    public String getConnectionName(){
        return CONNECTION_NAME;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }
}
