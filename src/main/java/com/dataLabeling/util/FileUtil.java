package com.dataLabeling.util;

import com.csvreader.CsvReader;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.entity.SimilarRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

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

    /**
     * 读取csv文件,解析详细对话
     * @param file
     * @return
     */
    public static List<SimilarRecord>  readCsv2(File file,Integer appId){
        List<SimilarRecord> list = new ArrayList<>();
        List<String> result = new ArrayList<String>();
        GetOriginalInfo getOriginalInfo = new GetOriginalInfo();
        String patternStr1 = "(?s)(访客问题>\\r\\n.*?)(\\r\\n\\r\\n|$)";
        Pattern r1 =  Pattern.compile(patternStr1);
        String patternStr2 = "(?s)访客问题>\\r\\n(.*?)\\r\\n(.*?)";
        Pattern r2 =  Pattern.compile(patternStr2);
        String patternStr3 = "(?s)机器人回答>\\r\\n(.*?)(\\r\\n|$)";
        Pattern r3 =  Pattern.compile(patternStr3);
        String patternStr4 = "(?s)匹配问题：(.*?)匹配答案";
        Pattern r4 =  Pattern.compile(patternStr4);
        String patternStr5 = "(?s)相关问题>\\r\\n(.*?)评价情况";
        Pattern r5 =  Pattern.compile(patternStr5);
        String patternStr6 = "(?s)理解问题>(.*?)(\\r\\n\\r\\n|$)";
        Pattern r6 =  Pattern.compile(patternStr6);
        String patternStr7 = "(?s)问题：(.*?)(\\r\\n|$)";
        Pattern r7 =  Pattern.compile(patternStr7);
        String patternStr8 = "(?s)所属类目>\\r\\n(.*?)\\r\\n(.*?)";
        Pattern r8   =  Pattern.compile(patternStr8);
        // 创建CSV读对象
        CsvReader csvReader = null;
        Matcher m = null;
        Matcher m1 = null;
        try {
            csvReader = new CsvReader(file.getAbsolutePath(), ',', Charset.forName("GBK"));
            // 读表头
            csvReader.readHeaders();
            String[] titles = csvReader.getRawRecord().split(",");
            //获取访客文本详细列
            Integer originalDetailInfoColumn = getOriginalInfo.GetColumn(titles,"(.*)对话详细(.*)");
            while (csvReader.readRecord()){
                ArrayList<String> chat1 = null;
                ArrayList<String> chat2 = null;
                String detailInfo = csvReader.get(originalDetailInfoColumn);
                if (detailInfo.matches(patternStr1)){
                    m = r1.matcher(detailInfo);
                    while (m.find()) {
                        chat2 = new ArrayList<>();
                        String content = m.group(1);
                        m1 = r2.matcher(content);
                        if (!m1.find()) continue;
                        String sentence1 = m1.group(1).trim();
                        if (CommonUtils.isDigit(sentence1)){
                            int qusNo = Integer.parseInt(sentence1);
                            if (chat1!=null&&chat1.size()>=qusNo&&qusNo>=1){
                                sentence1 = chat1.get(qusNo-1);
                            }
                        }
                        sentence1 = TextFiltering.filter(sentence1);
                        sentence1 = TextFiltering.filter1(sentence1);
                        if ("".equals(sentence1)){
                            continue;
                        }
                        int isValide = sentence1.length()>=4?1:0;
                        m1 = r3.matcher(content);
                        if (!m1.find()) continue;
                        String machineAnswer = m1.group(1);
                        if ("理解问题>".equals(machineAnswer)){
                            //理解问题 -- 机器人回答为空|所属类目为空
                            m1 = r6.matcher(content);
                            if (!m1.find()) continue;
                            String understandQus = m1.group(1);
                            m1 = r7.matcher(understandQus);
                            while (m1.find()){
                                String matchQus = TextFiltering.filter1(m1.group(1));
                                SimilarRecord similarRecord = new SimilarRecord();
                                similarRecord.setAppId(appId);
                                similarRecord.setFlag(0);
                                similarRecord.setIsSimilar(0);
                                similarRecord.setIsValid(isValide);
                                similarRecord.setVisit_ques(sentence1);
                                similarRecord.setMatch_ques(matchQus);
                                similarRecord.setSync(0);
                                similarRecord.setType("");
                                similarRecord.setMd5(CommonUtils.getMD5Str(sentence1+matchQus)+appId);
                                list.add(similarRecord);
                                chat2.add(m1.group(1));
                            }
                        }else {
                            //相关问题
                            m1 = r8.matcher(content);
                            if (!m1.find()) continue;
                            String clazz = m1.group(1);
                            m1 = r4.matcher(machineAnswer);
                            if (!m1.find()) continue;
                            String matchQus = TextFiltering.filter1(m1.group(1));

                            SimilarRecord similarRecord = new SimilarRecord();
                            similarRecord.setAppId(appId);
                            similarRecord.setFlag(0);
                            similarRecord.setIsSimilar(1);
                            similarRecord.setIsValid(isValide);
                            similarRecord.setVisit_ques(sentence1);
                            similarRecord.setMatch_ques(matchQus);
                            similarRecord.setSync(0);
                            similarRecord.setType(clazz);
                            similarRecord.setMd5(CommonUtils.getMD5Str(sentence1+matchQus)+appId);
                            list.add(similarRecord);

                            m1 = r5.matcher(content);
                            if (m1.find()){
                                String relatedQus = m1.group(1);
                                m1 = r7.matcher(relatedQus);
                                while (m1.find()){
                                    matchQus = TextFiltering.filter1(m1.group(1));
                                    similarRecord = new SimilarRecord();
                                    similarRecord.setAppId(appId);
                                    similarRecord.setFlag(0);
                                    similarRecord.setIsSimilar(0);
                                    similarRecord.setIsValid(isValide);
                                    similarRecord.setVisit_ques(sentence1);
                                    similarRecord.setMatch_ques(matchQus);
                                    similarRecord.setSync(0);
                                    similarRecord.setType(clazz);
                                    similarRecord.setMd5(CommonUtils.getMD5Str(sentence1+matchQus)+appId);
                                    list.add(similarRecord);
                                    chat2.add(m1.group(1));
                                }
                            }
                        }
                        chat1 = chat2;
                    }
                }else {
                    throw new RuntimeException("文件解析失败");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("文件读取失败");
        }finally {
            try {
                csvReader.close();
            }catch (Exception e){
            }
        }
        return list;
    }

    public static File exportTxt(List<RecordInfo> list) throws IOException {
        String filenameTemp = "D:\\实体标注结果.txt";
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            //如果文件不存在则创建
            filename.createNewFile();
        }
        // 文件路径
        File file = new File(filenameTemp);
        PrintWriter out = new PrintWriter(filenameTemp, "UTF-8");
        for(RecordInfo recordInfo:list){
            System.out.println(recordInfo.getResultCode());
            for(int i = 0 ;i < recordInfo.getChatRecord().length();i++){
                String[] str = recordInfo.getResultCode().split(" ");
                System.out.println(recordInfo.getChatRecord().charAt(i));
                out.print(recordInfo.getChatRecord().charAt(i)+"\t");
                System.out.println(str[i]);
                out.print(str[i]+"\r\n"); // \r\n即为换行
            }
            out.print("\r\n");
        }

        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件
        return file;
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        File file = new File("C:\\Users\\live800\\Downloads\\chat-content1.csv");
        List<SimilarRecord> similarRecords = FileUtil.readCsv2(file, 8);
        System.out.println(System.currentTimeMillis()-l);
        File writename = new File("C:\\Users\\live800\\Downloads\\output.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
        BufferedWriter out = null;
        try {
            writename.createNewFile();
            out = new BufferedWriter(new FileWriter(writename));
            for (int i=0;i<similarRecords.size();i++){
                SimilarRecord similarRecord = similarRecords.get(i);
                if (similarRecord.getIsValid()==1){
                    out.write(similarRecord.getIsSimilar()+"\t"+i+"\t"+i+"\t"+similarRecord.getVisit_ques().replaceAll("\n","")+"\t"+similarRecord.getMatch_ques().replaceAll("\n","")+"\r\n"); // \r\n即为换行
                }
            }
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File exportSimilarPairTxt(List<SimilarRecord> similarRecords) throws IOException {
        File filename = File.createTempFile("标注结果","txt");
        if (!filename.exists()) {
            //如果文件不存在则创建
            filename.createNewFile();
        }
        PrintWriter out = new PrintWriter(filename, "UTF-8");
        for(SimilarRecord similarRecord:similarRecords){
            out.println(similarRecord.getId()+"\t"+similarRecord.getType()+"\t"+similarRecord.getVisit_ques()+"\t"+similarRecord.getMatch_ques()+"\t"+similarRecord.getIsSimilar());
        }
        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件
        return filename;
    }
}
