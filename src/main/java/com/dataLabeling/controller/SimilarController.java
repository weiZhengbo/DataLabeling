package com.dataLabeling.controller;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.QueryVO;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.service.SimilarService;
import com.dataLabeling.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/SimilarController")
public class SimilarController {

    @Autowired
    private HttpServletRequest req; //自动注入request

    @Autowired
    private SimilarService similarService;

    @Autowired
    private RecordService recordService;

    /**
     * 请求数据，用于页面展示
     * @param vo
     * @param model
     * @return
     */
    @RequestMapping("/findAll")
    public String findAll(QueryVO vo, Model model){
        PageBean<RecordClass> pb = new PageBean<>();
        int pc = CommonUtils.getInt(vo.getPc());
        String keyword = CommonUtils.getOther(vo.getKeyword());
        String dataType = CommonUtils.getOther(vo.getDataType());
        if (dataType.equals("")) {
            dataType = "notdeal";
        }
        int appId = CommonUtils.getOtherParam(vo.getAppId());
        int clickwordId = CommonUtils.getOtherParam(vo.getClickwordId());
        String refresh = CommonUtils.getOther(vo.getRefresh());
        if (refresh.equals("")){
            refresh="yes";
        }
        String noHandledWord = CommonUtils.getOther(vo.getNoHandledWord());
        int ps = 10;
        int ps1 = 5;
        pb.setPc(pc);
        pb.setKeyword(keyword);
        pb.setDataType(dataType);
        pb.setAppId(appId);
        pb.setPs(ps);
        pb.setPs1(ps1);
        pb.setNoHandledWord(noHandledWord);
        similarService.findAll(pb,clickwordId);


        if (keyword.equals("")) {
            List<RecordClass> recordClasses = recordService.findAllClasses(appId);
            pb.setTClasses(recordClasses);
        } else {
            pb.setKeyword(keyword);
            List<RecordClass> recordClasses = recordService.findMatchClasses(appId, keyword);
            pb.setTClasses(recordClasses);
        }

        if (refresh.equals("yes")){
            HashMap<Integer,Object> mp = (HashMap<Integer, Object>) req.getSession().getAttribute("SSData");
            if (mp==null){
                mp = new HashMap<>();
            }
            if (pb.getBeanListUp()==null||pb.getBeanListUp().size()==0){
                mp.put(pb.getAppId(),pb.getBeanListUp());
                req.getSession().setAttribute("SSData",mp);
            }else {
                mp.put(pb.getAppId(),pb.getBeanListUp());
                req.getSession().setAttribute("SSData",mp);
            }
        }else if (refresh.equals("no")){
            HashMap<Integer,Object> mp = (HashMap<Integer, Object>) req.getSession().getAttribute("SSData");
            if (mp==null){
                mp = new HashMap<>();
            }
            if (!mp.containsKey(pb.getAppId())){
                if (pb.getBeanListUp()==null||pb.getBeanListUp().size()==0){

                }else {
                    mp.put(pb.getAppId(),pb.getBeanListUp());
                    req.getSession().setAttribute("SSData",mp);
                }
            }else {
                List<RecordInfo> recordInfos = (List<RecordInfo>) mp.get(pb.getAppId());
                ArrayList<Integer> rids= new ArrayList<>();
                for (RecordInfo recordInfo:recordInfos){
                    rids.add(recordInfo.getId());
                }
                List<RecordInfo> records = recordService.findRecordsByIds(rids);
                mp.put(pb.getAppId(),records);
                req.getSession().setAttribute("SSData",mp);
            }
        }
        model.addAttribute("pb", pb);
        return "listSimilar";
    }
}
