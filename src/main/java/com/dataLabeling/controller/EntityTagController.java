package com.dataLabeling.controller;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.service.impl.EntityTagService;
import com.dataLabeling.util.FileReadUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<byte[]> exportResult(Integer appId,HttpServletRequest request) throws IOException {
        List<RecordInfo> list = entityTagService.getNoTagList(appId, 0);
        //通过查询结果生成txt文件
        File file = FileReadUtil.exportTxt(list,request);
        HttpHeaders headers = new HttpHeaders();
        String fileName = URLEncoder.encode(file.getName(), "UTF-8");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }
}
