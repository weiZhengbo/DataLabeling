package com.dataLabeling.service;

import com.dataLabeling.entity.Project;

import java.util.List;

public interface ProjectService {
    /**
     * 加载所有的project
     * @return
     */
    public List<Project> loadAllProject();

    /**
     * 添加新的project
     * @param project
     */
    public void addnewProject(Project project);
}
