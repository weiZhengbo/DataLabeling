package com.dataLabeling.dao;

import com.dataLabeling.entity.SimilarRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimilarPairDao {

    void batchInsert(@Param("similarRecords") List<SimilarRecord> similarRecords);

    int selectRecordNum( @Param("appId") int appId,@Param("flag") int flag);

    List<SimilarRecord> selectRecord(@Param("appId") int appId, @Param("start")int start,@Param("num") int num, @Param("flag")int flag);

    void updateSync(@Param("similarRecords") List<Integer> similarRecords,@Param("flag") Integer flag);

    void updateIsSimilar(SimilarRecord similarRecord);

    void updateFlag(@Param("Ids")List<Integer> Ids,@Param("flag")int flag);

    List<SimilarRecord> selectAllDealedData(Integer appId);
}
