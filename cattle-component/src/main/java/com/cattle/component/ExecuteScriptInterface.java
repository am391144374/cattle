package com.cattle.component;

import com.cattle.entity.CattleJob;

public interface ExecuteScriptInterface extends Runnable {

    void buildConfig(CattleJob job);

    void setCattleJob(CattleJob job);
}
