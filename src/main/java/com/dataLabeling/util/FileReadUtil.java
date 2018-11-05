package com.dataLabeling.util;

import com.dataLabeling.entity.RecordInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static File exportTxt(List<RecordInfo> list, HttpServletRequest request) throws IOException {

        String filenameTemp = "D:\\实体标注结果.txt";
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            //如果文件不存在则创建
            filename.createNewFile();
        }
        // 文件路径
        File file = new File(filenameTemp);

        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for(RecordInfo recordInfo:list){
            for(int i = 0 ;i < recordInfo.getChatRecord().length();i++){
                String[] str = recordInfo.getResultCode().split(" ");
                System.out.println(recordInfo.getChatRecord().charAt(i));
                out.write(recordInfo.getChatRecord().charAt(i)+"\t");
                out.write(str[i]+"\r\n"); // \r\n即为换行
            }
            out.write("\r\n");
        }

        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件

        return file;
    }
}
