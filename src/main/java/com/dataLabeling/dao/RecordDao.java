package com.dataLabeling.dao;

import com.dataLabeling.entity.RecordClass;
import com.dataLabeling.entity.RecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecordDao {
    /**
     * 查询用户点击了的类别查询该类别下的所有条数
     * @param clickwordId
     * @param appId
     * @return
     */
    int selectCountClicked(@Param("clickwordId") int clickwordId, @Param("appId") Integer appId);

    /**
     * 查询已处理数据的条数
     * @param appId
     * @param noHandledWord
     * @return
     */
    int selectCountDealed( @Param("appId") Integer appId,@Param("noHandledWord") String[] noHandledWord);

    /**
     * 通过id查询recordclass
     * @param clickwordId
     * @return
     */
    RecordClass selectRecordClassById(int clickwordId);

    /**
     * 分页查询已处理的记录
     * @param appId
     * @param i
     * @param ps
     * @param noHandledWord
     * @return
     */
    List<RecordInfo> selectNoClickDealedList(@Param("appId") Integer appId, @Param("i") int i, @Param("ps") int ps,@Param("noHandledWord") String[] noHandledWord);

    /**
     * 分页查询某个类别下的记录
     * @param clickwordId
     * @param appId
     * @param i
     * @param ps
     * @return
     */
    List<RecordInfo> selectclickedList(@Param("clickwordId") int clickwordId, @Param("appId") Integer appId, @Param("i") int i, @Param("ps") int ps);

    /**
     * 查询未处理的记录
     * @param appId
     * @param ps
     * @return
     */
    List<RecordInfo> selectNotDealedList(@Param("appId") Integer appId, @Param("ps") int ps);

    /**
     * 查询出某个app下的所有recordclass
     * @param appId
     * @return
     */
    List<RecordClass> findAllClasses(int appId);

    /**
     * 查询出某个app下的匹配关键词的recordclass
     * @param appId
     * @param keyword
     * @return
     */
    List<RecordClass> findMatchClasses(@Param("appId") int appId, @Param("keyword") String[] keyword);

    /**
     * 根据id查询Record
     * @param id
     * @return
     */
    RecordInfo findRecordById(Integer id);

    /**
     * 为某个record更新recordclass
     * @param rid
     * @param sid
     */
    void addClassTag(@Param("rid") Integer rid, @Param("sid") Integer sid);

    /**
     * 移除某个record的class
     * @param rid
     */
    void removeRecordClass(Integer rid);

    /**
     * 删除某个recordclass
     * @param sid
     */
    void deleteRecordClass(Integer sid);

    /**
     * 删除某个recordclass之前需要将该类别下的记录类别删除
     * @param sid
     */
    void deleteBefore(Integer sid);

    /**
     * 添加新的recordclass
     * @param recordClass
     */
    void addnewRecordClass(RecordClass recordClass);

    /**
     * 批量加载record
     * @param chatInfoparts
     */
    void addRecordBatch(List<RecordInfo> chatInfoparts);

    /**
     * 通过recordclass id查询数据
     * @param val
     * @return
     */
    List<Map<String,Object>> selectDealedById(String val);

    /**
     * 查询某个app下面的所有Record数据
     * @param appId
     * @return
     */
    List<Map<String,Object>> selectAllData(Integer appId);

    /**
     * 查询某个app下面已经处理的record数据
     * @param appId
     * @return
     */
    List<Map<String,Object>> selectAllDealedData(Integer appId);

    /**
     * 通过recordclass id查询出下面所有的record
     * @param sid
     * @return
     */
    List<RecordInfo> selectRecordsBySid(Integer sid);

    /**
     * 判断sid是否是appId下面的
     * @param sid
     * @param appId
     * @return
     */
    int judgeSidIsInappId(@Param("sid") Integer sid, @Param("appId") Integer appId);
}
