package com.dataLabeling.controller;

import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.service.impl.EntityTagService;
import com.dataLabeling.util.FileReadUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
        String[] list;
        String result=null;
        String fileName = file.getOriginalFilename();
        if(fileName.endsWith("txt")){
            result=FileReadUtil.readTxt(f);
        }else if(fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
            result = FileReadUtil.readExcel(f);
        }else{
            return "文件类型不正确！";
        }
        if (result == null || "".equals(result)) {
            return "上传文件无内容";
        }
        recordInfo.setAppId(appId);
        recordInfo.setFileName(fileName);
       list = result.split("[。?!！？]");
        entityTagService.saveFileContent(list,recordInfo);
        return "上传成功";
    }

    @RequestMapping("/noTaglist")
    public String noTaglist(@RequestParam("appId") Integer appId, ModelMap map,Integer pageNo,Integer pageSize){
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?5:pageSize;
        PageHelper.startPage(pageNo, pageSize);
        map.addAttribute("pageResource",new PageInfo<RecordInfo>(entityTagService.getNoTagList(appId)));
        map.addAttribute("classList",recordService.findAllClasses(appId));
        map.addAttribute("appId",appId);
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


    @RequestMapping("/exportResult.")
    public String exportResult(Integer appId){
        List<RecordInfo> list = entityTagService.getNoTagList(appId);
        FileReadUtil.exportTxt(list);
        return "导出成功";
    }
}
