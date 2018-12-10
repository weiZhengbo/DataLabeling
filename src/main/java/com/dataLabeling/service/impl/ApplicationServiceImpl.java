package com.dataLabeling.service.impl;

import com.dataLabeling.dao.ApplicationDao;
import com.dataLabeling.entity.Application;
import com.dataLabeling.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationDao applicationDao;

    @Override
    public List<Application> loadApplications(Integer pid) {
        List<Application> applications = applicationDao.loadApplications(pid);
        return applications;
    }

    @Override
    public void addNewApplication(Application application) {
        applicationDao.addNewApplication(application);
    }

    @Override
    public void updateApplications(Application application) {
        applicationDao.updateApplications(application);
    }

    @Override
    public void deleteApplications(Integer id) {
        applicationDao.deleteApplications(id);
        applicationDao.deleteInfo(id);
        applicationDao.deleteClass(id);
    }
}
