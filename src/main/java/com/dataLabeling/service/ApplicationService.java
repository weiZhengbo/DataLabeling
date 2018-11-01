package com.dataLabeling.service;

import com.dataLabeling.entity.Application;

import java.util.List;

public interface ApplicationService {
    public List<Application> loadApplications(Integer pid);
    public void addNewApplication(Application application);
}
