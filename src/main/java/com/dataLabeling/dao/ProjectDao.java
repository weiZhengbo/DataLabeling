package com.dataLabeling.dao;

import com.dataLabeling.entity.Project;

import java.util.List;

public interface ProjectDao {
    /**
     * 查询所有的project
     * @return
     */
    public List<Project> loadAllProject();

    /**
     * 新增project
     * @param project
     */
    public void addnewProject(Project project);
}
