package com.dataLabeling.util;

import com.csvreader.CsvReader;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.service.impl.RecordServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVReader{
    public void read(File file,Integer appId,RecordService recordService){
        ArrayList<RecordInfo> chatInfoparts = new ArrayList<>();
        String filePath = file.getAbsolutePath();
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));

            // 读表头
            csvReader.readHeaders();
            String[] titles = csvReader.getRawRecord().split(",");
            GetOriginalInfo getOriginalInfo = new GetOriginalInfo();

            //获取原始ID列("记录ID"或者"对话ID")
            Integer oringinalIDColumn =  getOriginalInfo.GetColumn(titles,"(.*)(对话|记录)ID(.*)");
            //获取原始时间戳列
            Integer originalTimeStampColumn = getOriginalInfo.GetColumn(titles,"(.*)开始时间(.*)");
            //获取访客文本详细列
            Integer originalDetailInfoColumn = getOriginalInfo.GetColumn(titles,"(.*)对话详细(.*)");
            //获取访客姓名列
            Integer originalCustomerColumn = getOriginalInfo.GetColumn(titles,"(.*)访客姓名(.*)");

            //遍历每行，提取数据
            while (csvReader.readRecord()){
                //获取原始ID
                Integer originalId = -1;
                if (oringinalIDColumn==-1){
                    originalId=-1;
                }else {
                    String oringinalIDColumnStr = csvReader.get(oringinalIDColumn);
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
                    String originalTimeStampStr = csvReader.get(originalTimeStampColumn);
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
                String detailInfo = csvReader.get(originalDetailInfoColumn);
                String customerName="";
                if (originalCustomerColumn!=-1){
                    customerName = csvReader.get(originalCustomerColumn);
                }
                ArrayList<RecordInfo> recordInfos = getOriginalInfo.dealDataDetail(detailInfo, file, originalId, date, customerName);
                chatInfoparts.addAll(recordInfos);
                if (chatInfoparts.size()>10000){
                    recordService.addRecordBatch(chatInfoparts,appId);
                    chatInfoparts.clear();
                }
            }
            recordService.addRecordBatch(chatInfoparts,appId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}