package com.dataLabeling.controller;

import com.dataLabeling.entity.Application;
import com.dataLabeling.entity.QueryVO;
import com.dataLabeling.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/Application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    /**
     * 加载所有的application
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("/loadApplications")
    public String loadApplications(Integer pid, Model model){
        List<Application> applications = applicationService.loadApplications(pid);
        model.addAttribute("applications",applications);
        return "main";
    }

    /**
     * 新增application
     * @param application
     * @param model
     * @return
     */
    @RequestMapping("/addNewApplication")
    @ResponseBody
    public Application addNewApplication(Application application,Model model){
        applicationService.addNewApplication(application);
        return application;
    }

    /**
     * 管理所有的application
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("/manageApplications")
    public String manageApplications(Integer pid, Model model){
        List<Application> applications = applicationService.loadApplications(pid);
        model.addAttribute("applications",applications);
        return "manageApp";
    }

    @RequestMapping("/updateApp")
    @ResponseBody
    public String updateApp(Application application){
        applicationService.updateApplications(application);
        return "修改成功";
    }

    @RequestMapping("/deleteApp")
    @ResponseBody
    public String deleteApp(Integer id){
        applicationService.deleteApplications(id);
        return "删除成功";
    }

}
