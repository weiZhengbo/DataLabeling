package com.dataLabeling.util;


import com.dataLabeling.entity.RecordInfo;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于解析数据，提取原始id，原始时间戳，访客记录文本
 */
public class GetOriginalInfo {

    /**
     * 获取满足pattern的列数
     * @param titles
     * @return 如果有返回列数，否则返回-1
     */
    public Integer GetColumn(String[] titles,String pattern){
        for (int i=0;i<titles.length;i++){
            if (titles[i].matches(pattern)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 处理访客记录文本详细，返回访客文本记录对象列表
     * @param detailInfo
     * @param file
     * @param originalId
     * @param date
     * @param customerName
     * @return
     * @throws ParseException
     */
    public ArrayList<RecordInfo> dealDataDetail(String detailInfo, String originalName , Integer originalId, Date date,String customerName) throws ParseException {
        ArrayList<RecordInfo> chatInfoparts = new ArrayList<>();
        DateFormat formater1= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String patternStr1 = "(?s)访客问题>\\r\\n(.*?)\\r\\n(.*?)";
        String patternStr2 = "(?s)(.*?)访客.(\\d{4}-\\d{2}-\\d{2}.\\d{2}:\\d{2}:\\d{2})(.*?)\\n(.*?)\\n(.*?)";
        String patternStr3 = "(?s)对话开始\\d{4}(.*?)";
        Pattern r = null;
        Matcher m = null;
        if (detailInfo.matches(patternStr1)){
            //文本详细是以访客问题进行标识的
            r = Pattern.compile(patternStr1);
            m = r.matcher(detailInfo);
            while (m.find()) {
                String recordText = TextFiltering.filter(m.group(1));
                if (recordText.equals("")||recordText.equals(" ")){
                    continue;
                }
                RecordInfo recordInfo = new RecordInfo();
                recordInfo.setFileName(originalName);
                recordInfo.setChatRecord(recordText);
                recordInfo.setOriginId(originalId);
                recordInfo.setRecordTime(date);
                chatInfoparts.add(recordInfo);
            }
        }else if (detailInfo.matches(patternStr2)){
            //文本详细是以"访客"和"客服"身份对话的
            r = Pattern.compile(patternStr2);
            m = r.matcher(detailInfo);
            while (m.find()) {
                String recordText = TextFiltering.filter(m.group(4));
                if (recordText.equals("")||recordText.equals(" ")){
                    continue;
                }
                RecordInfo recordInfo = new RecordInfo();
                recordInfo.setFileName(originalName);
                recordInfo.setChatRecord(recordText);
                recordInfo.setOriginId(originalId);
                recordInfo.setRecordTime(formater1.parse(m.group(2)));
                chatInfoparts.add(recordInfo);
            }
        }else if (detailInfo.matches(patternStr3)){
            //文本详细是以访客名和客服名身份进行对话的
            //在这类文本中会有访客姓名的title，提取访客姓名进行正则匹配即可
            String patternStr4 = "(?s)(.*?)"+customerName+".(\\d{4}-\\d{2}-\\d{2}.\\d{2}:\\d{2}:\\d{2})(.*?)\\n(.*?)\\n(.*?)";
            r = Pattern.compile(patternStr4);
            m = r.matcher(detailInfo);
            while (m.find()) {
                String recordText = TextFiltering.filter(m.group(4));
                if (recordText.equals("")||recordText.equals(" ")){
                    continue;
                }
                RecordInfo recordInfo = new RecordInfo();
                recordInfo.setFileName(originalName);
                recordInfo.setChatRecord(recordText);
                recordInfo.setOriginId(originalId);
                recordInfo.setRecordTime(formater1.parse(m.group(2)));
                chatInfoparts.add(recordInfo);
            }
        }
        return chatInfoparts;
    }
}
