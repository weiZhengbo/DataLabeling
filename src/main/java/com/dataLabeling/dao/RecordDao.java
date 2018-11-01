package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;

import java.util.List;
import java.util.Map;

public interface RecordDao {
    int selectCountClicked(int clickwordId, Integer appId);

    int selectCountDealed(Integer appId);

    RecordClass selectRecordClassById(int clickwordId);

    List<RecordInfo> selectNoClickDealedList(Integer appId, int i, int ps);

    List<RecordInfo> selectclickedList(int clickwordId, Integer appId, int i, int ps);

    List<RecordInfo> selectNotDealedList(Integer appId, int ps);

    List<RecordClass> findAllClasses(int appId);

    List<RecordClass> findMatchClasses(int appId, String keyword);

    RecordInfo findRecordById(Integer id);

    void addClassTag(Integer rid, Integer sid);

    void removeRecordClass(Integer rid);

    void deleteRecordClass(Integer sid);

    void deleteBefore(Integer sid);

    void addnewRecordClass(RecordClass recordClass);

    void addRecordBatch(List<RecordInfo> chatInfoparts);

    List<Map<String,Object>> selectDealedById(String val);

    List<Map<String,Object>> selectAllData(Integer appId);

    List<Map<String,Object>> selectAllDealedData(Integer appId);
}
