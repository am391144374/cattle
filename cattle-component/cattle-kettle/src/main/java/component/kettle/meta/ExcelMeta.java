package component.kettle.meta;

import lombok.Data;

/**
 * @author lsj
 * excel导入模块 文件元数据
 */
@Data
public class ExcelMeta extends BaseMeta{

//    private int writeHeadRowIndex;
    /** 文件路径 */
    private String[] fileName;
    /** sheet名 */
    private String[] sheetName;
    /** 开始行 */
    private int[] startRow;
    /** 开始列 */
    private int[] startCol;

}
