package com.cattle.kettle.meta;

import lombok.Builder;
import lombok.Data;

/**
 * @author lsj
 * excel导入模块 文件元数据
 */
@Data
@Builder
public class ExcelMeta {

    private String[] fileName;
    private String[] sheetName;
    private int[] startRow;
    private int[] startCol;

}
