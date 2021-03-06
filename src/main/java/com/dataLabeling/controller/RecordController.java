package com.dataLabeling.controller;

import com.dataLabeling.entity.*;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.util.CommonConstant;
import com.dataLabeling.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/RecordController")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private HttpServletRequest req; //自动注入request

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
        if (dataType.isEmpty()) {
            dataType = CommonConstant.DATATYPE_NOTDEAL;
        }
        int appId = CommonUtils.getOtherParam(vo.getAppId());
        int clickwordId = CommonUtils.getOtherParam(vo.getClickwordId());
        String refresh = CommonUtils.getOther(vo.getRefresh());
        if (refresh.isEmpty()){
            refresh=CommonConstant.REFRESH_YES;
        }
        int ps = 10;
        int ps1 = 5;
        pb.setPc(pc);
        pb.setKeyword(keyword);
        pb.setDataType(dataType);
        pb.setAppId(appId);
        pb.setPs(ps);
        pb.setPs1(ps1);
        recordService.findAll(pb,clickwordId);

        if (keyword.isEmpty()) {
            List<RecordClass> recordClasses = recordService.findAllClasses(appId);
            pb.setTClasses(recordClasses);
        } else {
            pb.setKeyword(keyword);
            List<RecordClass> recordClasses = recordService.findMatchClasses(appId, keyword);
            pb.setTClasses(recordClasses);
        }

        if (refresh.equals(CommonConstant.REFRESH_YES)){
            HashMap<Integer,Object> mp = (HashMap<Integer, Object>) req.getSession().getAttribute(CommonConstant.SESSION_NAME);
            if (mp==null){
                mp = new HashMap<>();
            }
            mp.put(pb.getAppId(),pb.getBeanListUp());
            req.getSession().setAttribute(CommonConstant.SESSION_NAME,mp);
            /*if (pb.getBeanListUp()==null||pb.getBeanListUp().size()==0){

            }else {
                mp.put(pb.getAppId(),pb.getBeanListUp());
                req.getSession().setAttribute("SSData",mp);
            }*/
        }else if (refresh.equals(CommonConstant.REFRESH_NO)){
            HashMap<Integer,Object> mp = (HashMap<Integer, Object>) req.getSession().getAttribute(CommonConstant.SESSION_NAME);
            if (mp==null){
                mp = new HashMap<>();
            }
            if (!mp.containsKey(pb.getAppId())){
                if (pb.getBeanListUp()==null||pb.getBeanListUp().size()==0){

                }else {
                    mp.put(pb.getAppId(),pb.getBeanListUp());
                    req.getSession().setAttribute(CommonConstant.SESSION_NAME,mp);
                }
            }else {
                List<RecordInfo> recordInfos = (List<RecordInfo>) mp.get(pb.getAppId());
                ArrayList<Integer> rids= new ArrayList<>();
                for (RecordInfo recordInfo:recordInfos){
                    rids.add(recordInfo.getId());
                }
                if (rids.size()==0){
                    mp.put(pb.getAppId(),pb.getBeanListUp());
                }else {
                    List<RecordInfo> records = recordService.findRecordsByIds(rids);
                    mp.put(pb.getAppId(),records);
                }
                req.getSession().setAttribute(CommonConstant.SESSION_NAME,mp);
            }
        }
        model.addAttribute("pb", pb);

        return "listRecord";
    }

    /**
     * 为某个record添加类别
     * @param rid
     * @param sid
     * @return
     */
    @RequestMapping("/addRecordClass")
    @ResponseBody
    public Boolean addRecordClass(Integer rid,Integer sid,Integer appId){
        Boolean b = recordService.addClassTag(rid, sid,appId);
        return b;
    }

    /**
     * 将某个record的类别移除
     * @param rid
     * @return
     */
    @RequestMapping("/removeRecordClass")
    @ResponseBody
    public Boolean removeRecordClass(Integer rid){
        return recordService.removeRecordClass(rid);
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

    /**
     * 添加新的recordClass
     * @param recordClass
     * @return
     */
    @RequestMapping("/addnewRecordClass")
    @ResponseBody
    public RecordClass addnewRecordClass(RecordClass recordClass){
        recordService.addnewRecordClass(recordClass);
        return recordClass;
    }

    @RequestMapping("/selectRecordClassById")
    @ResponseBody
    public String selectRecordClassById(Integer sid){
        String recordClass = recordService.selectRecordClassById(sid);
        return recordClass;
    }

}
