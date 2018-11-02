package com.dataLabeling.service;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;

public interface SimilarService {
    /**
     * 请求similar页面的数据，
     * @param pb
     * @param clickwordId
     */
    void findAll(PageBean<RecordClass> pb, int clickwordId);
}
