package com.dataLabeling.controller;

import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.util.UploadFileProcess;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/UploadHandleController")
public class UploadHandleController {


    @Autowired
    private RecordService recordService;

    /**
     * 上传文件
     * @param appId
     * @return
     */
    @RequestMapping("/uploadFile")
    @ResponseBody
    public Boolean uploadFile(@RequestParam(value="file", required = false) CommonsMultipartFile file,  @RequestParam("appId") Integer appId){
        try{
            DiskFileItem fi = (DiskFileItem)file.getFileItem();
            File f = fi.getStoreLocation();
            List<List<RecordInfo>> recordInfoList = UploadFileProcess.handle(f.getAbsolutePath(),file.getOriginalFilename());
            for (int i=0;i<recordInfoList.size();i++){
                recordService.addRecordBatch(recordInfoList.get(i),appId);
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
