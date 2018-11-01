package com.dataLabeling.controller;

import com.dataLabeling.service.RecordService;
import com.dataLabeling.util.Store;
import com.dataLabeling.util.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/DownLoadZipFileController")
public class DownLoadZipFileController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private HttpServletRequest req; //自动注入request

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/downLoadZipFile")
    public Boolean downLoadZipFile(Integer appId){
        String storedataDir = req.getServletContext().getRealPath("/WEB-INF/storedata/"+appId+"/infodir/");
        String downType = req.getParameter("downType");
        if (downType.equals("1")){
            String choosedIds = req.getParameter("choosedIds");
            Store.storeInfo(storedataDir,appId,downType,choosedIds,recordService);
            String zipName = appId+"-partData-recordInfo.zip";
            response.setHeader("Content-Disposition","attachment; filename="+zipName);
        }else if(downType.equals("2")){
            String zipName = appId+"-isHandled-recordInfo.zip";
            response.setHeader("Content-Disposition","attachment; filename="+zipName);
            Store.storeInfo(storedataDir,appId,downType,"",recordService);
        }else if (downType.equals("3")){
            String zipName = appId+"-AllData-recordInfo.zip";
            response.setHeader("Content-Disposition","attachment; filename="+zipName);
            Store.storeInfo(storedataDir,appId,downType,"",recordService);
        }
        File dir = new File(storedataDir);
        response.setContentType("multipart/form-data");
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(response.getOutputStream());
            for (File file:dir.listFiles()){
                ZipUtils.doCompress(file.getPath(), out);
                response.flushBuffer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
