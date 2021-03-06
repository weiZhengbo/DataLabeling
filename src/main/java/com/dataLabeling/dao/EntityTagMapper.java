package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EntityTagMapper {

	List<String> getList();

	public void saveFileContent(@Param("recordInfoList") List<RecordInfo> recordInfoList);

    public List<RecordInfo> getNoTagList(@Param("appId") Integer appId,@Param("dataType") Integer dataType);

	public int getCountByMd5(@Param("md5") String md5,@Param("appId") Integer appId);

    void saveTagInfo(RecordInfo recordInfo);

}
