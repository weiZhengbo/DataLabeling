package com.dataLabeling.dao;

import com.dataLabeling.entity.Application;

import java.util.List;

public interface ApplicationDao {
    /**
     * 查询project下的所有application
     * @param pid
     * @return
     */
    public List<Application> loadApplications(Integer pid);

    /**
     * 新增application
     * @param application
     */
    public void addNewApplication(Application application);
}
