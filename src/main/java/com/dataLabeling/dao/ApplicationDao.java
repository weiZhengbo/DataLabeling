package com.dataLabeling.dao;

import com.dataLabeling.entity.Application;

import java.util.List;

public interface ApplicationDao {
    public List<Application> loadApplications(Integer pid);
    public void addNewApplication(Application application);
}
