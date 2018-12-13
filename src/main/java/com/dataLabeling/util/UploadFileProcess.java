package com.dataLabeling.util;

import com.dataLabeling.service.RecordService;

import java.io.File;

//上传和处理文件工具类
public class UploadFileProcess {
    private static ExcelReader excelReader = new ExcelReader();
    private static CSVReader csvReader = new CSVReader();
    public static void handle(String filePath, String originalName ,Integer appId, RecordService recordService){
        File file = new File(filePath);
        String suffix = originalName.substring(originalName.lastIndexOf(".") + 1);
        if (suffix.equals("csv")){
            csvReader.read(file,originalName,appId,recordService);
        }else if (suffix.equals("xls")){
            excelReader.readExcel(file,originalName,appId,recordService);
        }
    }
}
