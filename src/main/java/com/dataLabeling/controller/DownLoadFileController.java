package com.dataLabeling.controller;

import com.dataLabeling.service.RecordService;
import com.dataLabeling.util.CommonUtils;
import com.dataLabeling.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/DownLoadZipFileController")
public class DownLoadFileController {
    @Autowired
    private RecordService recordService;

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
    public Boolean downLoadZipFile(Integer appId,String downType,String choosedIds) throws IOException {
        List<Map<String, Object>> mapList =recordService.queryInfoDatas(appId,downType,choosedIds);
        List<File> files = FileUtil.storeRecordInfoFile(mapList);
        String zipName = appId+"-"+downType+"-recordInfo.zip";
        CommonUtils.downLoadZipFile(zipName,files,response);
        return true;
    }

    /**
     * 下载相似标注的数据
     * 1--下载用户点击的一个相似记录范本的数据
     * 2--下载用户搜索的一些相似记录范本数据
     * 3--下载所有已经标注了的数据
     * @return
     */
    @RequestMapping("/downLoadsimilarFile")
    @ResponseBody
    public Boolean downLoadsimilarFile(Integer appId,String clickwordId,String keyword,String downType) throws IOException {
        List<String> list = recordService.querySimilarDatas(appId,downType,keyword,clickwordId);
        File file = FileUtil.storeSingleFile(list);
        String downFileName =downType+"-similar.txt";
        CommonUtils.downLoadFile(downFileName,file,response);
        return true;
    }
}
