package com.dataLabeling.service.impl;


import com.dataLabeling.dao.EntityTagMapper;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.IEntityTagService;

import com.dataLabeling.util.CommonUtils;
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
	public void saveFileContent(List<String> list, RecordInfo recordInfo) {
		for(String str: list) {
			if("".equals(str)){
				continue;
			}
			String md5 = null;
			try {
				md5 = CommonUtils.getMD5Str(str);
			} catch (Exception e) {
				throw new RuntimeException("MD5加密错误");
			}
			int count = entityTagMapper.getCountByMd5(md5+recordInfo.getAppId(),recordInfo.getAppId());
			if(count==0) {
				StringBuffer resultCode = new StringBuffer();
				recordInfo.setChatRecord(str);
				recordInfo.setResultRecord(str);
				recordInfo.setRecordMd5(md5+recordInfo.getAppId());
				for(int i=0; i<str.length();i++){
					resultCode.append("O");
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
	public List<RecordInfo> getNoTagList(Integer appId, Integer dataType) {
		return entityTagMapper.getNoTagList(appId,dataType);
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
