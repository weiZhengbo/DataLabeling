package com.dataLabeling.service.impl;

import com.dataLabeling.dao.SimilarPairDao;
import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.QueryVO;
import com.dataLabeling.entity.SimilarRecord;
import com.dataLabeling.service.SimilarPairService;
import com.dataLabeling.util.CommonConstant;
import com.dataLabeling.util.CommonUtils;
import com.dataLabeling.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimilarPairServiceImpl implements SimilarPairService {

    @Autowired
    private SimilarPairDao similarPairDao;

    @Override
    public Boolean fileUpload(File f, Integer appId) {
        try {
            List<SimilarRecord> similarRecords = FileUtil.readCsv2(f, appId);
            //一次批处理插入3万条数据
            List<List<SimilarRecord>> lists = CommonUtils.splitList(similarRecords, 30000);
            for (int i=0;i<lists.size();i++){
                similarPairDao.batchInsert(lists.get(i));
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public PageBean<SimilarRecord> findAll(QueryVO vo) {
        PageBean<SimilarRecord> pb= new PageBean<>();
        int pc = CommonUtils.getInt(vo.getPc());
        int ps =10;
        String dataType = CommonUtils.getOther(vo.getDataType());
        if (dataType.isEmpty()) {
            dataType = CommonConstant.DATATYPE_NOTDEAL;
        }
        int appId = CommonUtils.getOtherParam(vo.getAppId());
        pb.setAppId(appId);
        pb.setPc(pc);
        pb.setDataType(dataType);
        pb.setPs(ps);
        int tr = -1;
        List<SimilarRecord> similarRecords = null;
        if (dataType.equals(CommonConstant.DATATYPE_ALL)){
            tr = similarPairDao.selectRecordNum(appId,0);
            similarRecords= similarPairDao.selectRecord(appId,(pc-1)*ps,ps,0);
        }else if (dataType.equals(CommonConstant.DATATYPE_DEALED)){
            tr = similarPairDao.selectRecordNum(appId,1);
            similarRecords= similarPairDao.selectRecord(appId,(pc-1)*ps,ps,1);
        }else if(dataType.equals(CommonConstant.DATATYPE_NOTDEAL)){
            similarRecords= similarPairDao.selectRecord(appId,(pc-1)*ps,ps,2);
            List<Integer> recordIds = new ArrayList<>();
            for (SimilarRecord similarRecord:similarRecords){
                recordIds.add(similarRecord.getId());
            }
            if (recordIds.size()!=0){
                similarPairDao.updateSync(recordIds,1);
            }
        }
        pb.setTr(tr);
        pb.setTClasses(similarRecords);
        return pb;
    }

    @Override
    public Boolean updateSync(List<Integer> recordData,Integer flag) {
        if (recordData.size()!=0)
            similarPairDao.updateSync(recordData,flag);
        return true;
    }

    @Override
    public Boolean updateState(SimilarRecord similarRecord) {
        //更新isSimilar和flag
        similarPairDao.updateIsSimilar(similarRecord);
        return true;
    }

    @Override
    public Boolean updateFlag(List<Integer> recordData, int flag) {
        similarPairDao.updateFlag(recordData,flag);
        return true;
    }

    @Override
    public List<SimilarRecord> download(Integer appId, Integer dataType) {
        List<SimilarRecord> rs = null;
        if (dataType == 1)
            rs =  similarPairDao.selectAllDealedData(appId);
        return rs;
    }
}
