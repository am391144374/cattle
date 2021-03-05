package com.ktr;

import cn.hutool.core.thread.NamedThreadFactory;
import com.ktr.config.KettleJob;
import com.ktr.handler.ExcelHeadFormatHandler;
import com.ktr.handler.StepProcessHandler;
import com.ktr.handler.step.ConstantStep;
import com.ktr.handler.step.ExcelInputStep;
import com.ktr.handler.step.SelectValuesStep;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 执行队列
 * @author lsj
 */
public class KettleRunner implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LinkedBlockingQueue<KettleJob> queue = new LinkedBlockingQueue<>(10000);
    private boolean runStatus = true;
    /** 脚本执行线程池 */
    private ExecutorService executorService;

    public KettleRunner(){
        executorService = new ThreadPoolExecutor(2,6,15, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),new NamedThreadFactory("kettle job running",false));
//        executorService = Executors.newSingleThreadExecutor();
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
                KettleJob kettleJob = queue.take();
                executorService.execute(() -> {
                    /** --脚本步骤执行器*/
                    long start = System.currentTimeMillis();
                    StepProcessHandler stepProcessHandler = new StepProcessHandler();
                    ExcelInputStep excelInputStep = new ExcelInputStep(kettleJob.getExcelMeta(),kettleJob.getFieldMeta(KettleJob.FieldBelongType.SELECT_VALUE)
                            ,"Excel输入");
                    stepProcessHandler.addStepMeta(excelInputStep);
                    SelectValuesStep selectValuesStep = new SelectValuesStep(kettleJob.getFieldMeta(KettleJob.FieldBelongType.SELECT_VALUE),"字段选择");
                    stepProcessHandler.addStepMeta(selectValuesStep);
                    ConstantStep constantStep = new ConstantStep(kettleJob.getFieldMeta(KettleJob.FieldBelongType.CONSTANT),"设置自定义字段");
                    stepProcessHandler.addStepMeta(constantStep);
                    /** -------------*/

                    /** excel 处理 */
                    ExcelHeadFormatHandler excelHeadFormatHandler = new ExcelHeadFormatHandler(kettleJob.getExcelMeta(),kettleJob.getFieldMeta(KettleJob.FieldBelongType.SELECT_VALUE),kettleJob.getWriteHeadRowIndex());
                    /** ---------- */

                    com.ktr.KettleScript kettleScript = com.ktr.KettleScript.create()
                                                            .addScriptFile(kettleJob.getScriptFile())
//                                                            .addLastProcess(excelHeadFormatHandler)
                                                            .addLastProcess(stepProcessHandler);

                    Map<String,Object> result = kettleScript.execute(kettleJob.getBatchId());

                    if(ResultHelper.isSuccess(result)){
                        logger.debug("执行成功！");
                    }else{
                        logger.error("执行失败！请查看日志");
                        List<String> error = ResultHelper.getException(result);
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

    public void putJob(KettleJob kettleJob) throws InterruptedException {
        queue.put(kettleJob);
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
