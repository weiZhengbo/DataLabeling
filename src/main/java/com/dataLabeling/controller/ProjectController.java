package com.dataLabeling.controller;

import com.dataLabeling.entity.Project;
import com.dataLabeling.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    /**
     * 添加新的project
     * @param project
     * @return
     */
    @RequestMapping("/addnewProject")
    @ResponseBody
    public Integer addnewProject(Project project){
        projectService.addnewProject(project);
        return project.getId();
    }
}
