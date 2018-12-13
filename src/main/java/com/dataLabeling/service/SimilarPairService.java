package com.dataLabeling.service;


import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.QueryVO;
import com.dataLabeling.entity.SimilarRecord;

import java.io.File;
import java.util.List;

public interface SimilarPairService {
    Boolean fileUpload(File f, Integer appId);

    PageBean<SimilarRecord> findAll(QueryVO vo);

    Boolean updateSync(List<Integer> recordData,Integer flag);

    Boolean updateState(SimilarRecord similarRecord);

    Boolean updateFlag(List<Integer> recordData, int flag);

    List<SimilarRecord> download(Integer appId, Integer dataType);
}
