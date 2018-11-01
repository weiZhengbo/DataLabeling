package com.dataLabeling.controller;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.QueryVO;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.service.SimilarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/SimilarController")
public class SimilarController {

    @Autowired
    private HttpServletRequest req; //自动注入request

    @Autowired
    private SimilarService service;

    @RequestMapping("/findAll")
    public String findAll(QueryVO vo, Model model){
        PageBean<RecordClass> pb = new PageBean<>();
        int pc = getInt(vo.getPc());
        String keyword = getOther(vo.getKeyword());
        String dataType = getOther(vo.getDataType());
        if (dataType.equals("")) {
            dataType = "notdeal";
        }
        int appId = getOtherParam(vo.getAppId());
        int clickwordId = getOtherParam(vo.getClickwordId());
        String refresh = getOther(vo.getRefresh());
        if (refresh.equals("")){
            refresh="yes";
        }
        int ps = 10;
        int ps1 = 5;
        pb.setPc(pc);
        pb.setKeyword(keyword);
        pb.setDataType(dataType);
        pb.setAppId(appId);
        pb.setPs(ps);
        pb.setPs1(ps1);
        return "";
    }




    private int getInt(String pc) {
        if(pc != null && !pc.trim().isEmpty()&& !pc.equals("null")) {
            return Integer.parseInt(pc);
        }
        return 1;
    }

    private int getOtherParam(String pctype) {
        if(pctype != null && !pctype.trim().isEmpty()&&!pctype.equals("null")) {
            return Integer.parseInt(pctype);
        }
        return -1;
    }

    private String getOther(String type){
        if (type!=null && !type.trim().isEmpty()&&!type.equals("null")){
            return type;
        }
        return "";
    }
}
