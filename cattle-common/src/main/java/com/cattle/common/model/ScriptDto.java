package com.cattle.common.model;

import com.cattle.common.enums.ScriptType;

/**
 * 脚本执行配置类
 */
public class ScriptDto {

    private long batchId;
    private ScriptType scriptType;

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public ScriptType getScriptType() {
        return scriptType;
    }

    public void setScriptType(ScriptType scriptType) {
        this.scriptType = scriptType;
    }
}
