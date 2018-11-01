package com.dataLabeling.service;

import com.dataLabeling.entity.Project;

import java.util.List;

public interface ProjectService {
    public List<Project> loadAllProject();
    public void addnewProject(Project project);
}
