package com.dataLabeling.service.impl;

import com.dataLabeling.dao.RecordDao;
import com.dataLabeling.dao.SimilarDao;
import com.dataLabeling.entity.PageBean;
import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import com.dataLabeling.service.SimilarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimilarServiceImpl implements SimilarService {

    @Autowired
    private RecordDao recordDao;
    @Autowired
    private SimilarDao similarDao;

    @Override
    public void findAll(PageBean<RecordClass> pb, int clickwordId) {
        int tr = -1;
        String noHandledWord[] =  pb.getNoHandledWord().split("\\|");
        if (pb.getDataType().equals("dealed")){
            if (clickwordId==-1){
                tr = recordDao.selectCountDealed(pb.getAppId(),noHandledWord);
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
        if (pb.getDataType().equals("dealed")){
            if (clickwordId==-1){
                beanList = recordDao.selectNoClickDealedList(pb.getAppId(),(pb.getPc()-1)*pb.getPs(),pb.getPs(),noHandledWord);
            }else {
                beanList = recordDao.selectclickedList(clickwordId,pb.getAppId(),(pb.getPc()-1)*pb.getPs(),pb.getPs());
            }
        }else {
            if (pb.getNoHandledWord()==null||pb.getNoHandledWord().equals("")){
                beanList = recordDao.selectNotDealedList(pb.getAppId(),pb.getPs());
            }else {
                beanList = similarDao.selectNotDealedAndSearchedList(pb.getAppId(),pb.getPs(),noHandledWord);
            }
        }
        pb.setBeanListUp(beanList);

        if (clickwordId==-1||(clickwordId!=-1&&pb.getDataType().equals("dealed"))){
            pb.setBeanListDown(null);
        }else {
            beanList = recordDao.selectclickedList(clickwordId,pb.getAppId(),(pb.getPc()-1)*pb.getPs1(),pb.getPs1());
            pb.setBeanListDown(beanList);
        }
    }
}
