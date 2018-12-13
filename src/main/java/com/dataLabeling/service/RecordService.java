package com.dataLabeling.service;

import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RecordService {
    //请求数据
    void findAll(PageBean<RecordClass> pb, int clickwordId);

    //查找出所有recordclass
    List<RecordClass> findAllClasses(int appId);

    //查找出匹配关键词的recordclass
    List<RecordClass> findMatchClasses(int appId, String keyword);

    //根据recordid查询出所有的record
    List<RecordInfo> findRecordsByIds(ArrayList<Integer> rids);

    //根据recordclassid查询该类下的所有record
    List<RecordInfo> findRecordsById(Integer sid);

    //为record添加recordclass
    Boolean addClassTag(Integer rid, Integer sid, Integer appId);

    //移除record的class
    Boolean removeRecordClass(Integer rid);

    //删除recordclass
    Boolean deleteRecordClass(Integer sid);

    //添加新的recordclass
    void addnewRecordClass(RecordClass recordClass);

    //批量添加record
    void addRecordBatch(List<RecordInfo> chatInfoparts, Integer appId);

    //查询record数据
    List<Map<String,Object>> queryInfoDatas(Integer appId, String downType, String choosedIds);

    String selectRecordClassById(Integer sid);

    List<String> querySimilarDatas(Integer appId, String downType, String keyword, String clickwordId);
}
