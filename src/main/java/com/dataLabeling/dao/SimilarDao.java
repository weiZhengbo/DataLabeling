package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordInfo;

import java.util.List;

public interface SimilarDao {
    /**
     * 查询出未处理并且含有关键词的record
     * @param appId
     * @param ps
     * @param noHandledWord
     * @return
     */
    List<RecordInfo> selectNotDealedAndSearchedList(Integer appId, int ps, String noHandledWord);
}
