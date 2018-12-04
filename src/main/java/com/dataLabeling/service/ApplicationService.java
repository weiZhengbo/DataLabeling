package com.dataLabeling.service;

import com.dataLabeling.entity.Application;

import java.util.List;

public interface ApplicationService {
    /**
     * 加载project下面的所有application
     * @param pid
     * @return
     */
    public List<Application> loadApplications(Integer pid);

    /**
     * 添加新的application
     * @param application
     */
    public void addNewApplication(Application application);

    public void updateApplications(Application application);

    void deleteApplications(Integer id);
}
