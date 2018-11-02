package com.dataLabeling.controller;

import com.dataLabeling.service.RecordService;
import com.dataLabeling.util.Store;
import com.dataLabeling.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    /**
     * 下载分类标注的数据
     * 1--下载用户选择的类别的数据
     * 2--下载已经标注的数据
     * 3--下载全部的数据
     * @param appId
     * @return
     */
    @RequestMapping("/downLoadZipFile")
    @ResponseBody
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

    /**
     * 下载相似标注的数据
     * 1--下载用户点击的一个相似记录范本的数据
     * 2--下载用户搜索的一些相似记录范本数据
     * 3--下载所有已经标注了的数据
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downLoadsimilarFile")
    @ResponseBody
    public Boolean downLoadsimilarFile(HttpServletRequest request, HttpServletResponse response){
        int appId = Integer.parseInt(request.getParameter("appId"));
        String storedataDir = request.getServletContext().getRealPath("/WEB-INF/storedata/"+appId);
        String downType = request.getParameter("downType");
        if (downType.equals("1")){
            String clickwordId = request.getParameter("clickwordId");
            Store.storeSimilar(storedataDir,appId,downType,clickwordId,recordService);
        }else if(downType.equals("2")){
            String keyword = request.getParameter("keyword");
            Store.storeSimilar(storedataDir,appId,downType,keyword,recordService);
        }else if (downType.equals("3")){
            Store.storeSimilar(storedataDir,appId,downType,"",recordService);
        }
        String similarPath=storedataDir+"\\"+downType+"-similar.txt";
        OutputStream os  = null;
        try {
            os = response.getOutputStream();
            File file = new File(similarPath);
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+downType+"-similar.txt");
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(os);
        }
        return true;
    }
}
