package com.cattle.kettle;

import cn.hutool.core.thread.NamedThreadFactory;
import com.cattle.ResultHelper;
import com.cattle.kettle.config.KettleConfig;
import com.cattle.kettle.step.ConstantStep;
import com.cattle.kettle.step.ExcelInputStep;
import com.cattle.kettle.step.SelectValuesStep;
import com.cattle.kettle.step.StepTypeInterface;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * 执行队列
 * @author lsj
 */
public class KettleRunner implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LinkedBlockingQueue<KettleConfig> queue = new LinkedBlockingQueue<>(10000);
    private boolean runStatus = true;
    /** 脚本执行线程池 */
    private ExecutorService executorService;

    public KettleRunner(){
        executorService = new ThreadPoolExecutor(2,6,15, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),new NamedThreadFactory("kettle job running",false));
    }

    @Override
    public void run() {
        try {
            KettleConfigInit.init();
        } catch (KettleException e) {
            e.printStackTrace();
        }
        while (runStatus){
            try {
                KettleConfig kettleConfig = queue.take();
                executorService.execute(() -> {
                    /** --脚本步骤执行器*/
                    long start = System.currentTimeMillis();
                    StepProcessHandler stepProcessHandler = new StepProcessHandler();
                    ExcelInputStep excelInputStep = new ExcelInputStep(kettleConfig.getExcelMeta(), kettleConfig.getFieldMeta(StepTypeInterface.FieldBelongType.SELECT_VALUE)
                            ,"Excel输入");
                    stepProcessHandler.addStepMeta(excelInputStep);
                    SelectValuesStep selectValuesStep = new SelectValuesStep(kettleConfig.getFieldMeta(StepTypeInterface.FieldBelongType.SELECT_VALUE),"字段选择");
                    stepProcessHandler.addStepMeta(selectValuesStep);
                    ConstantStep constantStep = new ConstantStep(kettleConfig.getFieldMeta(StepTypeInterface.FieldBelongType.CONSTANT),"设置自定义字段", kettleConfig.getBatchId());
                    stepProcessHandler.addStepMeta(constantStep);
                    /** -------------*/

                    /** excel 处理 */
                    ExcelHeadFormatHandler excelHeadFormatHandler = new ExcelHeadFormatHandler(kettleConfig.getExcelMeta(), kettleConfig.getFieldMeta(StepTypeInterface.FieldBelongType.SELECT_VALUE), kettleConfig.getWriteHeadRowIndex());
                    /** ---------- */

                    KettleScript kettleScript = KettleScript.create()
                                                            .addScriptFile(kettleConfig.getScriptFile())
//                                                            .addLastProcess(excelHeadFormatHandler)
                                                            .addLastProcess(stepProcessHandler);

                    kettleScript.execute(kettleConfig.getBatchId());

                    if(ResultHelper.isSuccess(kettleConfig.getBatchId())){
                        logger.debug("执行成功！");
                    }else{
                        logger.error("执行失败！请查看日志");
                        List<String> error = ResultHelper.getException(kettleConfig.getBatchId());
                        error.forEach(str -> logger.error(Thread.currentThread().getName() + " ## error :{}",str));
                    }
                    logger.debug(Thread.currentThread().getName() + " ## 处理完成:{}秒",(System.currentTimeMillis() - start) / 1000);

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }

    public void setRunStatus(boolean status){
        runStatus = status;
    }

    public void putJob(KettleConfig kettleConfig) throws InterruptedException {
        queue.put(kettleConfig);
    }

    public void monitor(){
        Thread t = new Thread(() -> {
            while (true){
                logger.debug("当前队列长度：{}",queue.size());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
