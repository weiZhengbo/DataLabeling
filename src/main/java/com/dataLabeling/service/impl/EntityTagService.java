package com.dataLabeling.service.impl;


import com.dataLabeling.dao.EntityTagMapper;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.IEntityTagService;
import com.dataLabeling.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
		List<RecordInfo> recordInfoList = new ArrayList<RecordInfo>();
		for(String str: list) {
			RecordInfo recordInfo2 = new RecordInfo();
			recordInfo2.setAppId(recordInfo.getAppId());
			recordInfo2.setFileName(recordInfo.getFileName());
			if("".equals(str)){
				continue;
			}
			String md5 = null;
			try {
				md5 = CommonUtils.getMD5Str(str);
			} catch (Exception e) {
				new RuntimeException("md5获取失败");
			}
			int count = entityTagMapper.getCountByMd5(md5+recordInfo2.getAppId(),recordInfo2.getAppId());
			if(count==0) {
				StringBuffer resultCode = new StringBuffer();
				recordInfo2.setChatRecord(str);
				recordInfo2.setResultRecord(str);
				recordInfo2.setRecordMd5(md5+recordInfo2.getAppId());
				for(int i=0; i<str.length();i++){
					resultCode.append("O");
					if(i!=str.length()-1){
						resultCode.append(" " );
					}
				}
				recordInfo2.setResultCode(resultCode.toString());
				recordInfoList.add(recordInfo2);
			}
		}
		entityTagMapper.saveFileContent(recordInfoList);
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
