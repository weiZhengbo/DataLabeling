package com.dataLabeling.entity;

/**
 * 项目实体类
 */
public class Project {
    private Integer id;//项目编号
    private String projectName;//项目名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
