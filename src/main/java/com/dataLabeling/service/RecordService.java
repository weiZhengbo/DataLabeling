package com.dataLabeling.service;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RecordService {
    void findAll(PageBean<RecordClass> pb, int clickwordId);

    List<RecordClass> findAllClasses(int appId);

    List<RecordClass> findMatchClasses(int appId, String keyword);

    List<RecordInfo> findRecordsByIds(ArrayList<Integer> rids);

    Boolean addClassTag(Integer rid, Integer sid);

    Boolean removeRecordClass(Integer rid);

    Boolean deleteRecordClass(Integer sid);

    void addnewRecordClass(RecordClass recordClass);

    void addRecordBatch(ArrayList<RecordInfo> chatInfoparts, Integer appId);

    List<Map<String,Object>> queryInfoDatas(Integer appId, String downType, String choosedIds);
}
