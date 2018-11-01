package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EntityTagMapper {

	List<String> getList();

	public void saveFileContent(RecordInfo recordInfo);

    public List<RecordInfo> getNoTagList(Integer appId);

	public int getCountByMd5(@Param("md5") String md5,@Param("appId") Integer appId);
}
