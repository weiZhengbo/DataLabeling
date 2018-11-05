package com.dataLabeling.service.impl;


import com.dataLabeling.dao.EntityTagMapper;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.IEntityTagService;
import com.dataLabeling.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityTagService implements IEntityTagService {

	@Autowired
	EntityTagMapper entityTagMapper;

	public List<String> getAllProject() {

		return null;
	}

	@Override
	public void saveFileContent(String[] list, RecordInfo recordInfo) {
		for(String str: list) {
			if("".equals(str)){
				continue;
			}
			String md5 = CommonUtil.getMd5(str);
			int count = entityTagMapper.getCountByMd5(md5,recordInfo.getAppId());
			if(count==0) {
				StringBuffer resultCode = new StringBuffer();
				recordInfo.setChatRecord(str);
				recordInfo.setResultRecord(str);
				recordInfo.setRecordMd5(md5);
				for(int i=0; i<str.length();i++){
					resultCode.append("N");
					if(i!=str.length()-1){
						resultCode.append(" " );
					}
				}
				recordInfo.setResultCode(resultCode.toString());

				entityTagMapper.saveFileContent(recordInfo);
			}
		}
	}

	@Override
	public List<RecordInfo> getNoTagList(Integer appId) {
		return entityTagMapper.getNoTagList(appId);
	}

	/**
	 * 保存实体标记信息
	 * @param recordInfo
	 */
	@Override
	public void saveTagInfo(RecordInfo recordInfo) {
		entityTagMapper.saveTagInfo(recordInfo);
	}
}
