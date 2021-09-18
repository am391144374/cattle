package component.kettle.excel;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WriteExcel {

    private Map<String,ExcelReader> excelReaderMap = new ConcurrentHashMap<>();
    public static String newFileSuffix = "_new";

    public static void setNewFileSuffix(String newFileSuffix){
        WriteExcel.newFileSuffix = newFileSuffix;
    }

    public WriteExcel(String[] fileNames,int sheetIndex){
        buildExcelWriter(fileNames,sheetIndex);
    }

    public WriteExcel(String[] fileNames,String sheetIndex){
        buildExcelWriter(fileNames,sheetIndex);
    }

    public void buildExcelWriter(String[] fileNames,int sheetIndex){
        Arrays.stream(fileNames).forEach(fileName -> {
            File excelFile = new File(fileName);
            ExcelReader excelReader = new ExcelReader(excelFile,sheetIndex);
            excelReader.setIgnoreEmptyRow(true);
            if(excelReaderMap.containsKey(fileName)){
                excelReaderMap.remove(fileName);
            }
            excelReaderMap.put(fileName,excelReader);
        });
    }

    public void buildExcelWriter(String[] fileNames,String sheetName){
        Arrays.stream(fileNames).forEach(fileName -> {
            File excelFile = new File(fileName);
            ExcelReader excelReader = new ExcelReader(excelFile,sheetName);
            excelReader.setIgnoreEmptyRow(true);
            if(excelReaderMap.containsKey(fileName)){
                excelReaderMap.remove(fileName);
            }
            excelReaderMap.put(fileName,excelReader);
        });
    }

    public void writeHeadRow(int rowIndex, List<String> head){
        Optional.ofNullable(excelReaderMap).get().forEach((fileName, excelReader) -> {
            ExcelWriter excelWriter = excelReader.getWriter();
            excelWriter.setCurrentRow(rowIndex);
            excelWriter.writeHeadRow(head);
        });

    }

    public void flushAll(){
        Optional.ofNullable(excelReaderMap).get().forEach((fileName, excelReader) -> {
            ExcelWriter excelWriter = excelReader.getWriter();
            int lastIndex = fileName.lastIndexOf(".");
            String preFileName = fileName.substring(0,lastIndex);
            String sufFileName = fileName.substring(lastIndex);
            String newFileName = preFileName + newFileSuffix + sufFileName;
            excelWriter.setDestFile(new File(newFileName));
            excelWriter.flush();
        });
    }

    public void close(){
        Optional.ofNullable(excelReaderMap).get().forEach((k,excelRead) -> {
            excelRead.close();
        });
    }
}
