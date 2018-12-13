package com.dataLabeling.service.impl;

import com.dataLabeling.dao.RecordDao;
import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.RecordService;
import com.dataLabeling.util.CommonConstant;
import com.dataLabeling.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordDao recordDao;

    @Override
    public void findAll(PageBean<RecordClass> pb, int clickwordId) {
        int tr = -1;
        String noHandledWord[] =  "".split("\\|");
        if (pb.getDataType().equals(CommonConstant.DATATYPE_DEALED)){
            if (clickwordId==-1){
                tr = recordDao.selectCountDealed(pb.getAppId(), noHandledWord);
            }else {
                tr = recordDao.selectCountClicked(clickwordId,pb.getAppId());
            }
        }else {
                tr = recordDao.selectCountClicked(clickwordId,pb.getAppId());
        }
        pb.setTr(tr);

        RecordClass recordClass = recordDao.selectRecordClassById(clickwordId);
        pb.setTClass(recordClass);

        List<RecordInfo> beanList = null;
        if (pb.getDataType().equals(CommonConstant.DATATYPE_DEALED)){
            if (clickwordId==-1){
                beanList = recordDao.selectNoClickDealedList(pb.getAppId(),(pb.getPc()-1)*pb.getPs(),pb.getPs(), noHandledWord);
            }else {
                beanList = recordDao.selectclickedList(clickwordId,pb.getAppId(),(pb.getPc()-1)*pb.getPs(),pb.getPs());
            }
        }else {
            beanList = recordDao.selectNotDealedList(pb.getAppId(),pb.getPs());
        }
        pb.setBeanListUp(beanList);

        if (clickwordId==-1||(clickwordId!=-1&&pb.getDataType().equals(CommonConstant.DATATYPE_DEALED))){
            pb.setBeanListDown(null);
        }else {
            beanList = recordDao.selectclickedList(clickwordId,pb.getAppId(),(pb.getPc()-1)*pb.getPs1(),pb.getPs1());
            pb.setBeanListDown(beanList);
        }
    }

    @Override
    public List<RecordClass> findAllClasses(int appId) {
        return recordDao.findAllClasses(appId);
    }

    @Override
    public List<RecordClass> findMatchClasses(int appId, String keyword) {
        String word[] =  keyword.split("\\|");
        return recordDao.findMatchClasses(appId,word);
    }

    @Override
    public List<RecordInfo> findRecordsByIds(ArrayList<Integer> rids) {
        List<RecordInfo> recordInfos = new ArrayList<>();
        for (Integer id:rids){
            recordInfos.add(recordDao.findRecordById(id));
        }
        return recordInfos;
    }

    @Override
    public List<RecordInfo> findRecordsById(Integer sid) {
        return recordDao.selectRecordsBySid(sid);
    }

    @Override
    public Boolean addClassTag(Integer rid, Integer sid, Integer appId) {
        int num = recordDao.judgeSidIsInappId(sid,appId);
        if (num==0){
            return false;
        }
        recordDao.addClassTag(rid, sid);
        return true;
    }

    @Override
    public Boolean removeRecordClass(Integer rid) {
        recordDao.removeRecordClass(rid);
        return true;
    }

    @Override
    public Boolean deleteRecordClass(Integer sid) {
        recordDao.deleteBefore(sid);
        recordDao.deleteRecordClass(sid);
        return true;
    }

    @Override
    public void addnewRecordClass(RecordClass recordClass) {
        recordDao.addnewRecordClass(recordClass);
    }

    @Override
    public void addRecordBatch(List<RecordInfo> chatInfoparts, Integer appId) {
        for (RecordInfo recordInfo:chatInfoparts){
            recordInfo.setAppId(appId);
            try {
                recordInfo.setRecordMd5(CommonUtils.getMD5Str(recordInfo.getChatRecord())+appId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        recordDao.addRecordBatch(chatInfoparts);
    }

    @Override
    public List<Map<String, Object>> queryInfoDatas(Integer appId, String downType, String choosedIds) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if ("1".equals(downType)){
            String[] ids = choosedIds.split(",");
            for (String val:ids){
                if (val.equals("")){
                    continue;
                }
                List<Map<String, Object>> tempMapList=recordDao.selectDealedById(val);
                mapList.addAll(tempMapList);
            }
        }else if ("2".equals(downType)){
            mapList= recordDao.selectAllDealedData(appId);
        }else if ("3".equals(downType)){
            mapList=recordDao.selectAllData(appId);
        }
        return mapList;
    }

    @Override
    public String selectRecordClassById(Integer sid) {
        RecordClass recordClass = recordDao.selectRecordClassById(sid);
        return recordClass.getRecordClass();
    }

    @Override
    public List<String> querySimilarDatas(Integer appId, String downType, String keyword, String clickwordId) {
        List<String> list= new ArrayList<>();
        List<Integer> allSimilarId =  new ArrayList<>();
        if ("1".equals(downType)){
            allSimilarId.add(Integer.parseInt(clickwordId));
        }else if ("2".equals(downType)){
            List<RecordClass> matchClasses = findMatchClasses(appId, keyword);
            for (RecordClass recordClass:matchClasses){
                allSimilarId.add(recordClass.getId());
            }
        }else if ("3".equals(downType)){
            List<RecordClass> allClasses = findAllClasses(appId);
            for (RecordClass recordClass:allClasses){
                allSimilarId.add(recordClass.getId());
            }
        }
        for (Integer sid:allSimilarId){
            List<RecordInfo> similarClassRecord = findRecordsById(sid);
            for (int i=0;i<similarClassRecord.size();i++){
                for (int j=i+1;j<similarClassRecord.size();j++){
                    list.add("1\t"+similarClassRecord.get(i).getChatRecord()+"\t"+similarClassRecord.get(j).getChatRecord());
                }
            }
        }
        return list;
    }
}
