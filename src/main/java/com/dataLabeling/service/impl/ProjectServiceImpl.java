package com.dataLabeling.service.impl;

import com.dataLabeling.dao.ProjectDao;
import com.dataLabeling.entity.Project;
import com.dataLabeling.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public List<Project> loadAllProject() {
        List<Project> projects = projectDao.loadAllProject();
        return projects;
    }

    @Override
    public void addnewProject(Project project) {
        projectDao.addnewProject(project);
    }
}
