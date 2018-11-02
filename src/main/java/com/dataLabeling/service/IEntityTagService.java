package com.dataLabeling.service;

import com.dataLabeling.entity.RecordInfo;

import java.util.List;

public interface IEntityTagService {
	/**
	 * 获取所有项目
	 * @return
	 */
	public List<String> getAllProject();


	public void saveFileContent(String[] list, RecordInfo recordInfo);

    public List<RecordInfo> getNoTagList(Integer appId);

    void saveTagInfo(RecordInfo recordInfo);
}
