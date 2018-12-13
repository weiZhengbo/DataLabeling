package com.dataLabeling.util;

import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.service.impl.RecordServiceImpl;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExcelReader {

    //读excel处理文件
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public void readExcel(File file,String originalName ,Integer appId,RecordService recordService) {
        try {
            ArrayList<RecordInfo> chatInfoparts = new ArrayList<>();
            GetOriginalInfo getOriginalInfo = new GetOriginalInfo();

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

            ArrayList<RecordInfo> recordInfos = getOriginalInfo.dealDataDetail(detailInfo,originalName, originalId, date, customerName);
            chatInfoparts.addAll(recordInfos);
            if (chatInfoparts.size()>10000){
                recordService.addRecordBatch(chatInfoparts,appId);
                chatInfoparts.clear();
            }
          }
            recordService.addRecordBatch(chatInfoparts,appId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
