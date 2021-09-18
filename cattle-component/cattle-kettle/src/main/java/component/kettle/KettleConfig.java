package component.kettle;

import component.kettle.meta.DBMate;
import component.kettle.meta.ExcelMeta;
import component.kettle.meta.FieldMeta;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class KettleConfig{

    private long batchId;

    /**
     * 脚本执行方式
     * normal-普通模式   即只执行脚本，不对脚本进行改造
     * edit-编辑模式     即对脚本进行编辑，然后再执行
     */
    private String processType;
    /**
     * 脚本文件路径
     */
    private String scriptFile;

    private String jobName;

    private ExcelMeta excelMeta;
    private String tableName;


    /** 数据导入字段 */
    private List<FieldMeta> selectValueMap = new ArrayList<>();
    /** 新增变量字段 */
    private List<FieldMeta> constantMap = new ArrayList<>();
    /** 数据存储 */
    private Map<String /* dbName */, DBMate> dataBaseMetas;

    public void putDB(DBMate dbMate){
        if(dataBaseMetas == null){
            dataBaseMetas = new HashMap<>();
        }
        dataBaseMetas.put(dbMate.getDbName(),dbMate);
    }

}
