package component.kettle;

import com.cattle.common.JobContextHelper;
import com.cattle.common.context.ProcessContent;
import com.cattle.common.entity.CattleJob;
import com.cattle.common.entity.CattleKtrField;
import com.cattle.common.entity.CattleKtrInfo;
import com.cattle.common.entity.CattleKtrStep;
import com.cattle.common.enums.JobStatus;
import com.cattle.common.plugin.ExecuteScriptInterface;
import com.cattle.common.plugin.ProcessScript;
import component.kettle.handler.ExecuteKettleScriptProcessHandler;
import component.kettle.handler.StepProcessHandler;
import component.kettle.meta.DBMate;
import component.kettle.meta.ExcelMeta;
import component.kettle.meta.FieldMeta;
import component.kettle.step.ConstantStep;
import component.kettle.step.ExcelInputStep;
import component.kettle.step.SelectValuesStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lsj
 * kettle 执行
 */
public class KettleScript extends ProcessScript implements ExecuteScriptInterface {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private KettleConfig kettleConfig;
    private CattleJob cattleJob;

    private void addExecuteKettleScriptActuator(){
        ExecuteKettleScriptProcessHandler kettleScriptProcessHandler = new ExecuteKettleScriptProcessHandler();
        addLastProcess(kettleScriptProcessHandler);
    }

    @Override
    public void run(){

        //编辑模式（自定义excel）
        if("edit".equals(kettleConfig.getProcessType())) {
            buildKettleEditProcess();
        }

        //最后加上 kettle脚本执行器
        addExecuteKettleScriptActuator();

        ProcessContent context = new ProcessContent();
        try {
            TransMeta transMeta = new TransMeta(kettleConfig.getScriptFile());
            //变更数据库连接
            if(kettleConfig.getDataBaseMetas() != null && !kettleConfig.getDataBaseMetas().isEmpty()){
                changeDataBase(transMeta);
            }
            transMeta.setName(kettleConfig.getJobName());
            context.setJobStatus(JobStatus.RUNNING);
            context.setScriptType(getScriptType());
            context.put("transMeta",transMeta);
            context.put("kettleConfig",kettleConfig);
            context.setJobId(cattleJob.getJobId());
            context.setJobName(kettleConfig.getJobName());
            context.setBatchId(kettleConfig.getBatchId());
            JobContextHelper.setJobContext(kettleConfig.getBatchId(),context);
            logger.info("{} start spider config:{}",Thread.currentThread().getName(),kettleConfig.toString());
            first.execute(context);
        } catch (KettleXMLException e) {
            e.printStackTrace();
            context.putError(this,e);
        } catch (KettleMissingPluginsException e) {
            e.printStackTrace();
            context.putError(this,e);
        }
    }

    private void changeDataBase(TransMeta transMeta){
        Map<String, DBMate> dbList = kettleConfig.getDataBaseMetas();
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

    public void buildKettleEditProcess(){

            /** --脚本步骤执行器*/
            StepProcessHandler stepProcessHandler = new StepProcessHandler();
            ExcelInputStep excelInputStep = new ExcelInputStep(kettleConfig);
            stepProcessHandler.addStepMeta(excelInputStep);
            SelectValuesStep selectValuesStep = new SelectValuesStep(kettleConfig);
            stepProcessHandler.addStepMeta(selectValuesStep);
            ConstantStep constantStep = new ConstantStep(kettleConfig);
            stepProcessHandler.addStepMeta(constantStep);
            /** -------------*/

            addLastProcess(stepProcessHandler);
    }

    public void buildConfig(CattleJob job){
        KettleConfig kettleConfig = new KettleConfig();
        CattleKtrStep stepInfo = job.getStepInfo();
        CattleKtrInfo scriptInfo = stepInfo.getKtrInfo();
        String stepType = stepInfo.getStepType();
        List<FieldMeta> fieldMetaList = new ArrayList<>();
        List<CattleKtrField> stepFields = stepInfo.getFieldList();
        stepFields.forEach(stepField -> {
            FieldMeta fieldMeta = FieldMeta.builder()
                    .comment(stepField.getComment())
                    .name(stepField.getFieldName())
                    .type(stepField.getFieldType())
                    .value(stepField.getDefaultValue()).build();
            if(fieldMeta.getType().equals("Number")){
                fieldMeta.setPrecision(stepField.getPrecision());
            }
            if(!fieldMeta.getType().equals("Integer")){
                fieldMeta.setLength(stepField.getLength());
            }
            fieldMetaList.add(fieldMeta);
        });
        switch (stepType){
            // excel导入
            case "excelImport":
                kettleConfig.setSelectValueMap(fieldMetaList);
                ExcelMeta excelMeta = new ExcelMeta();
                excelMeta.setFileName(stepInfo.parseFileList());
                excelMeta.setSheetName(new String[]{stepInfo.getSheetName()});
                excelMeta.setStartRow(new int[]{stepInfo.getStartRow()});
                excelMeta.setStartCol(new int[]{stepInfo.getStartCol()});
                kettleConfig.setExcelMeta(excelMeta);
                kettleConfig.setTableName(stepInfo.getTableName());
                break;
            //todo 目前新增变量不做设置
//                case "constant":
//                    kettleConfig.setConstantMap(fieldMetaList);
//                    break;
        }

        //数据库设置
        cattleJob.getDataBaseMetas().forEach((k,db) -> {
            DBMate dbMate = new DBMate();
            dbMate.setDbName(db.getName());
            dbMate.setDatabase(db.getDbDatabase());
            dbMate.setHostName(db.getHostName());
            dbMate.setPort(db.getPort().toString());
            dbMate.setUserName(db.getLoginName());
            dbMate.setPassword(db.getLoginPwd());

            kettleConfig.putDB(dbMate);
        });

        kettleConfig.setScriptFile(scriptInfo.getScriptFile());
        kettleConfig.setJobName(job.getJobName());
        kettleConfig.setBatchId(job.getBatchId());
        kettleConfig.setProcessType(scriptInfo.getProcessType());

        this.kettleConfig = kettleConfig;
    }

    @Override
    public void setCattleJob(CattleJob job) {
        this.cattleJob = job;
    }

    @Override
    public String getScriptType() {
        return "kettle";
    }

    @Override
    public void initEnv() {
        try {
            KettlePluginInit.init();
        } catch (KettleException e) {
            e.printStackTrace();
        }
    }
}
