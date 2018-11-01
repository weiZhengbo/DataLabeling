package com.dataLabeling.dao;

import com.dataLabeling.entity.Project;

import java.util.List;

public interface ProjectDao {
    public List<Project> loadAllProject();
    public void addnewProject(Project project);
}
