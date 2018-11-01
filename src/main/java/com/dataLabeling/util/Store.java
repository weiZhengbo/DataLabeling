package com.dataLabeling.util;

import com.dataLabeling.dao.RecordDao;
import com.dataLabeling.service.RecordService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Store {
//    private static SimilarDao similarDao = new SimilarDao();

    /**
     * 存储info.txt文件
     * id，访客记录文本，类别标签
     * 字段分割符为||
     * 行分隔符为\r\n
     *
     * 存储detail.txt文件
     * id，原始id，访客记录文本，文件名，原始时间戳
     * 字段分割符为||
     * 行分隔符为\r\n
     */
    public static void storeInfo(String storedataDir, Integer appId, String downType, String choosedIds, RecordService recordService){
        File dir = new File(storedataDir);
        if (!dir.exists() && !dir.isDirectory()) {
            //创建目录
            dir.mkdirs();
        }
        String infoPath=storedataDir+"\\info.txt";
        String detailPath = storedataDir+"\\detail.txt";
        File file = new File(infoPath);
        if (file.exists()){
            file.delete();
        }
        file = new File(detailPath);
        if (file.exists()){
            file.delete();
        }

        try {
            PrintWriter infoWriter = new PrintWriter(infoPath, "UTF-8");
            PrintWriter detailWriter = new PrintWriter(detailPath, "UTF-8");
            List<Map<String, Object>> mapList = recordService.queryInfoDatas(appId,downType,choosedIds);
            for (Map l:mapList){
                String info = l.get("id")+"||"+l.get("chatRecord")+"||"+l.get("recordClass");
                String detail = l.get("id")+"||"+l.get("originId")+"||"+l.get("chatRecord")+"||"+l.get("fileName")+"||"+l.get("recordTime");
                infoWriter.println(info);
                detailWriter.println(detail);
            }
            infoWriter.flush();
            detailWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储similar.txt文件
     *
     */
    /*public static void storeSimilar(String storedataDir,Integer appId,String downType,String param){
        File dir = new File(storedataDir);
        if (!dir.exists() && !dir.isDirectory()) {
            //创建目录
            dir.mkdirs();
        }
        try {
            String similarPath=storedataDir+"\\"+downType+"-similar.txt";
            System.out.println(similarPath);
            File file = new File(similarPath);
            if (file.exists()){
                file.delete();
            }
            PrintWriter similarWriter = new PrintWriter(similarPath, "UTF-8");
            List<Integer> allSimilarId = null;
            if (downType.equals("1")){
               allSimilarId = new ArrayList<>();
               allSimilarId.add(Integer.parseInt(param));
            }else if (downType.equals("2")){
               allSimilarId = similarDao.findMatchSimilarId(appId,param);
            }else if (downType.equals("3")){
                allSimilarId = similarDao.findAllSimilarId(appId);
            }
            for (Integer sid:allSimilarId){
                List<RecordInfo> similarClassRecord = similarDao.getSimilarClassRecord(sid);
                for (int i=0;i<similarClassRecord.size();i++){
                    for (int j=i+1;j<similarClassRecord.size();j++){
                        similarWriter.println("1\t"+similarClassRecord.get(i).getChatRecord()+"\t"+similarClassRecord.get(j).getChatRecord());
                    }
                }
            }
            similarWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/

}
