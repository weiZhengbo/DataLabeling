package com.dataLabeling.controller;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.service.impl.EntityTagService;
import com.dataLabeling.util.CommonUtils;
import com.dataLabeling.util.FileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wzb on 2018/10/29.
 */

@Controller
@RequestMapping("/entityTag")
public class EntityTagController {

    @Autowired
    private EntityTagService entityTagService;

    @Autowired
    private RecordService recordService;
    /*
     * 上传文件
     */
    @RequestMapping("/fileUpload")
    @ResponseBody
    public String  fileUpload(@RequestParam(value="file", required = false) CommonsMultipartFile file, ModelMap map,@RequestParam("appId") Integer appId) throws IOException {
        DiskFileItem fi = (DiskFileItem)file.getFileItem();
        RecordInfo recordInfo = new RecordInfo();
        File f = fi.getStoreLocation();
        List<String>  list=new ArrayList<String>();
        String fileName = file.getOriginalFilename();
        if(fileName.endsWith("txt")){
            list= FileUtil.readTxt(f);
        }else if(fileName.endsWith("csv")){
            list = FileUtil.readCsv(f);
        }else if(fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
            list = FileUtil.readExcel(f,fileName);
        }else{
            return "文件类型不正确！";
        }
        if (list == null) {
            return "上传文件无内容";
        }
        recordInfo.setAppId(appId);
        recordInfo.setFileName(fileName);
       //list = result.split("[。?!！？]");
        entityTagService.saveFileContent(list,recordInfo);
        return "上传成功";
    }

    @RequestMapping("/getTaglist")
    public String noTaglist(@RequestParam("appId")Integer appId, @RequestParam("dataType")Integer dataType, ModelMap map,Integer pc){

        pc = pc == null?1:pc;
        dataType = dataType == null?-1:dataType;
        PageHelper.startPage(pc, 10);
        PageBean<RecordClass> pb = new PageBean<>();
        pb.setPc(pc);
        PageInfo<RecordInfo> pageInfo = new PageInfo<RecordInfo>(entityTagService.getNoTagList(appId,dataType));
        pb.setTr((int)pageInfo.getTotal());
        pb.setPs(10);
        map.addAttribute("pageResource",pageInfo);
        map.addAttribute("classList",recordService.findAllClasses(appId));
        map.addAttribute("appId",appId);
        map.addAttribute("dataType",dataType);
        map.addAttribute("pb",pb);
        return "entityTagList";
    }


    @RequestMapping("/addnewRecordClass")
    @ResponseBody
    public RecordClass addnewRecordClass(RecordClass recordClass){
        recordService.addnewRecordClass(recordClass);
        return recordClass;
    }

    @RequestMapping("/saveTagInfo")
    @ResponseBody
    public String saveTagInfo(RecordInfo recordInfo){
        entityTagService.saveTagInfo(recordInfo);
        return "标记成功";
    }

    /**
     * 删除recordclass的
     * @param sid
     * @return
     */
    @RequestMapping("/deleteRecordClass")
    @ResponseBody
    public Boolean deleteRecordClass(Integer sid){
        return recordService.deleteRecordClass(sid);
    }


    @RequestMapping("/exportResult")
    @ResponseBody
    public Boolean exportResult(Integer appId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<RecordInfo> list = entityTagService.getNoTagList(appId, 0);
        //通过查询结果生成txt文件
        File file = FileUtil.exportTxt(list);
        String fileName = "实体标注结果.txt";
        CommonUtils.downLoadFile(fileName,file,response);
        return true;
    }

}
