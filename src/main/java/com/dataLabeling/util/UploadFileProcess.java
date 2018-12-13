package com.dataLabeling.util;

import com.csvreader.CsvReader;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//上传和处理文件工具类
public class UploadFileProcess {
    public static List<List<RecordInfo>> handle(String filePath, String originalName){
        File file = new File(filePath);
        List<List<RecordInfo>> result = null;
        String suffix = originalName.substring(originalName.lastIndexOf(".") + 1);
        if (suffix.equals("csv")){
            result = UploadFileProcess.readCsv(file,originalName);
        }else if (suffix.equals("xls")){
            result = UploadFileProcess.readExcel(file,originalName);
        }
        return result;
    }

    //读excel处理文件
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static List<List<RecordInfo>> readExcel(File file, String originalName) {
        List<List<RecordInfo>> result = new ArrayList<>();
        try {
            ArrayList<RecordInfo> chatInfoparts = new ArrayList<>();
            InputStream is = new FileInputStream(file.getAbsolutePath());
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);

            //获取原始ID列("记录ID"或者"对话ID")
            Integer oringinalIDColumn =  -1;
            //获取原始时间戳列
            Integer originalTimeStampColumn = -1;
            //获取访客文本详细列
            Integer originalDetailInfoColumn = -1;
            //获取访客姓名列
            Integer originalCustomerColumn = -1;

            for (int j = 0; j < sheet.getColumns(); j++) {
                String cellinfo = sheet.getCell(j, 0).getContents();
                if (cellinfo.matches("(.*)(对话|记录)ID(.*)")){
                    oringinalIDColumn=j;
                }else if (cellinfo.matches("(.*)开始时间(.*)")){
                    originalTimeStampColumn=j;
                }else if (cellinfo.matches("(.*)对话详细(.*)")){
                    originalDetailInfoColumn=j;
                }else if (cellinfo.matches("(.*)访客姓名(.*)")){
                    originalCustomerColumn=j;
                }
            }

            // 遍历所有行 sheet.getRows()
            for (int i = 1; i < sheet.getRows(); i++) {
                //获取原始ID
                Integer originalId = -1;
                if (oringinalIDColumn!=-1){
                    String oringinalIDColumnStr = sheet.getCell(oringinalIDColumn,i).getContents();
                    if (oringinalIDColumnStr==null||oringinalIDColumnStr.equals("")){
                        originalId=-1;
                    }else {
                        originalId = Integer.parseInt(oringinalIDColumnStr);
                    }
                }

                //获取原始时间戳
                Date date = null;
                DateFormat formater1= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                if (originalTimeStampColumn!=-1){
                    String originalTimeStampStr = sheet.getCell(originalTimeStampColumn,i).getContents();
                    if (originalTimeStampStr==null||originalTimeStampStr.equals("")){
                        date=null;
                    }else {
                        try {
                            date = formater1.parse(originalTimeStampStr);
                        } catch (ParseException e) {
                        }
                    }
                }

                //获取访客文本详细
                String detailInfo = sheet.getCell(originalDetailInfoColumn,i).getContents();
                String customerName="";
                if (originalCustomerColumn!=-1){
                    customerName = sheet.getCell(originalCustomerColumn,i).getContents();
                }

                ArrayList<RecordInfo> recordInfos =GetOriginalInfo.dealDataDetail(detailInfo,originalName, originalId, date, customerName);
                chatInfoparts.addAll(recordInfos);
                if (chatInfoparts.size()>10000){
                    result.add(chatInfoparts);
                    chatInfoparts.clear();
                }
            }
            result.add(chatInfoparts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    //读csv文件进行处理
    public static List<List<RecordInfo>> readCsv(File file,String originalName){
        List<List<RecordInfo>> result = new ArrayList<>();
        ArrayList<RecordInfo> chatInfoparts = new ArrayList<>();
        String filePath = file.getAbsolutePath();
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));

            // 读表头
            csvReader.readHeaders();
            String[] titles = csvReader.getRawRecord().split(",");

            //获取原始ID列("记录ID"或者"对话ID")
            Integer oringinalIDColumn =  GetOriginalInfo.GetColumn(titles,"(.*)(对话|记录)ID(.*)");
            //获取原始时间戳列
            Integer originalTimeStampColumn = GetOriginalInfo.GetColumn(titles,"(.*)开始时间(.*)");
            //获取访客文本详细列
            Integer originalDetailInfoColumn = GetOriginalInfo.GetColumn(titles,"(.*)对话详细(.*)");
            //获取访客姓名列
            Integer originalCustomerColumn = GetOriginalInfo.GetColumn(titles,"(.*)访客姓名(.*)");

            //遍历每行，提取数据
            while (csvReader.readRecord()){
                //获取原始ID
                Integer originalId = -1;
                if (oringinalIDColumn==-1){
                    originalId=-1;
                }else {
                    String oringinalIDColumnStr = csvReader.get(oringinalIDColumn);
                    if (oringinalIDColumnStr==null||oringinalIDColumnStr.isEmpty()){
                        originalId=-1;
                    }else {
                        originalId = Integer.parseInt(oringinalIDColumnStr);
                    }
                }

                //获取原始时间戳
                Date date = null;
                DateFormat formater1= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                if (originalTimeStampColumn!=-1){
                    String originalTimeStampStr = csvReader.get(originalTimeStampColumn);
                    if (originalTimeStampStr==null||originalTimeStampStr.isEmpty()){
                        date=null;
                    }else {
                        try {
                            date = formater1.parse(originalTimeStampStr);
                        } catch (ParseException e) {
                        }
                    }
                }

                //获取访客文本详细
                String detailInfo = csvReader.get(originalDetailInfoColumn);
                String customerName="";
                if (originalCustomerColumn!=-1){
                    customerName = csvReader.get(originalCustomerColumn);
                }
                ArrayList<RecordInfo> recordInfos = GetOriginalInfo.dealDataDetail(detailInfo, originalName, originalId, date, customerName);
                chatInfoparts.addAll(recordInfos);
                if (chatInfoparts.size()>10000){
                    result.add(chatInfoparts);
                    chatInfoparts.clear();
                }
            }
            result.add(chatInfoparts);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
