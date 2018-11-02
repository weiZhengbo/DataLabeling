package com.dataLabeling.util;

import com.dataLabeling.entity.RecordInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class FileReadUtil {

    /**
     * 读取txt
     * @param file
     * @return
     */
    public static String readTxt(File file){

        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString().trim();
    }

    /**
     * 读取excel
     * @param file
     * @return
     * @throws IOException
     */
    public static String readExcel(File file) throws IOException {
        String fileName = file.getName();
        StringBuilder result = new StringBuilder();
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        if ( fileName.endsWith("xls")  || fileName.endsWith("csv")){
            FileInputStream fiStream = new FileInputStream(file);   //文件流对象
            wb = new HSSFWorkbook(fiStream);
        }else{
            wb = new XSSFWorkbook(new FileInputStream(file));
        }
        //开始解析
        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();

        for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                int firstCellIndex = row.getFirstCellNum();
                //int lastCellIndex = row.getLastCellNum();
             //   for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                    Cell cell = row.getCell(firstCellIndex);
                    if (cell != null) {
                        result.append(cell.toString());
                        System.out.print(cell.toString() + "    ");
                    }
                //}
            }
        }
        return result.toString();
    }

    public static String exportTxt(List<RecordInfo> list){

        return "";
    }
}
