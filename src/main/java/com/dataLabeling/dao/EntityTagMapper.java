package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordInfo;

import java.util.List;


public interface EntityTagMapper {

	List<String> getList();

	public void saveFileContent(RecordInfo recordInfo);

    public List<RecordInfo> getNoTagList(Integer appFlag);

	public int getCountByMd5(String md5, Integer appFlag);
}
