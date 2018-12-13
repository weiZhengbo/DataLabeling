package com.dataLabeling.controller;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.QueryVO;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.SimilarRecord;
import com.dataLabeling.service.SimilarPairService;
import com.dataLabeling.util.CommonUtils;
import com.dataLabeling.util.FileUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/SimilarPair")
public class SimilarPairController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private SimilarPairService similarPairService;

    @RequestMapping("/fileUpload")
    @ResponseBody
    public Boolean  fileUpload(@RequestParam(value="file", required = false) CommonsMultipartFile file,  @RequestParam("appId") Integer appId) throws IOException {
        DiskFileItem fi = (DiskFileItem)file.getFileItem();
        File f = fi.getStoreLocation();
        Boolean b = similarPairService.fileUpload(f,appId);
        return b;
    }


    @RequestMapping("findAll")
    public String findAll(ModelMap map, QueryVO vo){
        PageBean<SimilarRecord> pb = similarPairService.findAll(vo);
        map.addAttribute("pb",pb);
        return "listSimilarPair";
    }

    @RequestMapping("updateSync")
    @ResponseBody
    public Boolean updateSync(@RequestParam("list") List<Integer> recordData){
        return similarPairService.updateSync(recordData,0);
    }

    @RequestMapping("updateState")
    @ResponseBody
    public Boolean updateState(SimilarRecord similarRecord){
        return similarPairService.updateState(similarRecord);
    }

    @RequestMapping("updateFlag")
    @ResponseBody
    public Boolean updateFlag(@RequestParam("list") List<Integer> recordData){
        return similarPairService.updateFlag(recordData,1);
    }

    @RequestMapping("download")
    @ResponseBody
    public Boolean download(@RequestParam("appId") Integer appId,@RequestParam("dataType")Integer dataType ) throws IOException {
        List<SimilarRecord> similarRecords = similarPairService.download(appId,dataType);
        File file = FileUtil.exportSimilarPairTxt(similarRecords);
        String fileName =  "标注结果.txt";
        CommonUtils.downLoadFile(fileName,file,response);
        return true;
    }
}
