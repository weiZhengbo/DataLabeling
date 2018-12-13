package com.dataLabeling.util;

import com.dataLabeling.entity.RecordInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReadUtil {

    /**
     * 读取txt
     * @param file
     * @return
     */
    public static List<String>  readTxt(File file){

        List<String> result = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.add(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取excel
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> readExcel(File file,String fileName) throws IOException {
        List<String> result = new ArrayList<String>();
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        if ( fileName.endsWith("xls")  || fileName.endsWith("csv")){
            wb = new HSSFWorkbook(new FileInputStream(file));
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
                        result.add(cell.toString());
                    }
                //}
            }
        }
        return result;
    }

    /**
     * 读取csv文件
     * @param file
     * @return
     */
    public static List<String>  readCsv(File file){
        List<String> result = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String line = null;
            int num = 0;
                while((line = br.readLine())!=null) {//使用readLine方法，一次读一行
                    num ++;
                    String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                    result.add(item[0].toString());
                }
            br.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
    }

    public static File exportTxt(List<RecordInfo> list) throws IOException {

        File file = File.createTempFile("实体标注结果",".txt");
        // 文件路径
        PrintWriter out = new PrintWriter(file.getCanonicalPath(), "UTF-8");
        for(RecordInfo recordInfo:list){
            for(int i = 0 ;i < recordInfo.getChatRecord().length();i++){
                String[] str = recordInfo.getResultCode().split(" ");
                out.print(recordInfo.getChatRecord().charAt(i)+"\t");
                out.print(str[i]+"\r\n"); // \r\n即为换行
            }
            out.print("\r\n");
        }
        System.out.println(file.getAbsolutePath());
        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件
        return file;
    }
}
