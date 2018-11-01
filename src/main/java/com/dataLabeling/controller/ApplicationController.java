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

    @RequestMapping("/loadApplications")
    public String loadApplications(Integer pid, Model model){
        List<Application> applications = applicationService.loadApplications(pid);
        model.addAttribute("applications",applications);
        return "main";
    }

    @RequestMapping("/addNewApplication")
    @ResponseBody
    public Application addNewApplication(Application application,Model model){
        applicationService.addNewApplication(application);
        return application;
    }
}
