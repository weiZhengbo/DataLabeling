package com.dataLabeling.controller;

import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.impl.EntityTagService;
import com.dataLabeling.util.FileReadUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by wzb on 2018/10/29.
 */

@Controller
@RequestMapping("/entityTag")
public class EntityTagController {

    @Autowired
    private EntityTagService entityTagService;

    private RecordInfo recordInfo;

    @RequestMapping("/list")
    public String getList(){
        entityTagService.getAllProject();

        return "entityTag";
    }

    /*
     * 上传文件
     */
    @RequestMapping("/fileUpload")
    public String  fileUpload(@RequestParam("file") CommonsMultipartFile file, ModelMap map,@RequestParam("appFlag") Integer appFlag) throws IOException {
        DiskFileItem fi = (DiskFileItem)file.getFileItem();
        RecordInfo recordInfo = new RecordInfo();
        File f = fi.getStoreLocation();
        String[] list;
        String result=null;
        String fileName = file.getOriginalFilename();
        if(fileName.endsWith("txt")){
            result=FileReadUtil.readTxt(f);
        }else if(fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
            FileReadUtil.readExcel(f);
        }else{
            map.addAttribute("message","文件类型不正确！");
        }
        recordInfo.setAppFlag(appFlag);
        recordInfo.setFileName(fileName);
       list = result.split("[。?!]");
        for(String i : list){
            System.out.println(i);
        }
        entityTagService.saveFileContent(list,recordInfo);
        return "";
    }

    @RequestMapping("/noTaglist")
    public String noTaglist(@RequestParam("appFlag") Integer appFlag, ModelMap map){
        map.addAttribute("recordClassList",entityTagService.getNoTagList(appFlag));
        return "entityTagList";
    }

    @Test
        public  void main() throws Exception {
            String[] sentences = {
                    "我喜欢看徐克的电影  ",
                    "他喜欢周星驰的电影","Former first lady Nancy Reagan was taken to a." };//两句话
            NameFinderME finder = new NameFinderME(new TokenNameFinderModel(
                    new FileInputStream(new File("src/main/resources/nlpbin/en-ner-person.bin"))));//在http://opennlp.sourceforge.net/models-1.5/ 下载en-ner-person.bin
            Tokenizer tokenizer = SimpleTokenizer.INSTANCE;//初始化简单切词（也就是按空格切词）
            for (int si = 0; si < sentences.length; si++) {
                String[] tokens = tokenizer.tokenize(sentences[si]);//第一句话[Former, first, lady, Nancy, Reagan, was, taken, to, a, suburban, Los, Angeles, hospital, ", as, a, precaution, ", Sunday, after, a, fall, at, her, home, ,, an, aide, said, .]
                //第二句话[The, 86, -, year, -, old, Reagan, will, remain, overnight, for, observation, at, a, hospital, in, Santa, Monica, ,, California, ,, said, Joanne, Drake, ,, chief, of, staff, for, the, Reagan, Foundation, .]
                Span[] names = finder.find(tokens);
                displayNames(names, tokens);
            }
            finder.clearAdaptiveData();
        }

        public static void displayNames(Span[] names, String[] tokens) {
            for (int si = 0; si < names.length; si++) {
                StringBuilder cb = new StringBuilder();
                for (int ti = names[si].getStart(); ti < names[si].getEnd(); ti++) {
                    cb.append(tokens[ti]).append(" ");
                }
                System.out.println(cb.substring(0, cb.length() - 1));
                System.out.print("\tstart: " + names[si].getStart());
                System.out.println("\tend: " + names[si].getEnd());//输出实体的起止位置
                System.out.println("\ttype: " + names[si].getType());//输出实体的类型type
                System.out.println("\tprob: " + names[si].getProb());//输出实体的概率prob
            }
        }


    public RecordInfo getRecordInfo() {
        return recordInfo;
    }

    public void setRecordInfo(RecordInfo recordInfo) {
        this.recordInfo = recordInfo;
    }
}
