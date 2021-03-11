package com.cattle;


import cn.hutool.core.thread.ThreadUtil;
import com.cattle.common.context.ProcessContext;
import com.cattle.common.model.ScriptDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lsj
 * 脚本最小执行类
 */
public class CattleRun {

    private ExecutorService executorPool = Executors.newFixedThreadPool(10, ThreadUtil.newNamedThreadFactory("cattle run -",false));
    private LinkedBlockingQueue<ScriptDto> queue = new LinkedBlockingQueue<>(3000);
    private boolean runFlag = true;

    public static CattleRun create(){
        return new CattleRun();
    }

    public void run() throws InterruptedException {
        while (runFlag){
            ScriptDto scriptDto = queue.take();
            ProcessContext context = new ProcessContext();
            context.setBatchId(scriptDto.getBatchId());
            switch (scriptDto.getScriptType()){
                case KETTLE:
                    break;
                case SPIDER:
                    break;
            }
        }
    }

    public void putJob(ScriptDto scriptDto){
        queue.add(scriptDto);
    }


}
