package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimilarDao {
    /**
     * 查询出未处理并且含有关键词的record
     * @param appId
     * @param ps
     * @param noHandledWord
     * @return
     */
    List<RecordInfo> selectNotDealedAndSearchedList(@Param("appId") Integer appId, @Param("ps") int ps, @Param("noHandledWord") String noHandledWord);
}
