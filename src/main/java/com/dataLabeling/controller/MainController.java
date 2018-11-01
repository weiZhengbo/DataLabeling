package com.dataLabeling.controller;

import com.dataLabeling.entity.Project;
import com.dataLabeling.service.ProjectService;
import com.dataLabeling.service.impl.SpeechTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by wzb on 2018/10/29.
 */

@Controller
public class MainController {

    @Autowired
    private ProjectService projectService;

    /**
     * 首页，展示所有项目
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String init(Model model){
        List<Project> projects = projectService.loadAllProject();
        model.addAttribute("projectList",projects);
        return "index";
    }

}
