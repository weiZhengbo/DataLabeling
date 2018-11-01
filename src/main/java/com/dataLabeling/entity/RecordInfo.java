package com.dataLabeling.entity;

import java.util.Date;

/**
 * 该类为访客记录文本类
 */
public class RecordInfo {
    //访客记录文本id
    private Integer id;
    //原始文件中的id
    private Integer originId;
    //原始文件名
    private String fileName;
    //访客记录文本
    private String chatRecord;
    //访客记录文本类别
    private String recordClass;
    //访客记录相似类别
    private String similarClass;
    //原始时间戳
    private Date recordTime;

    private String recordMd5;

    private Integer appId;
    @Override
    public String toString() {
        return "RecordInfo{" +
                "id=" + id +
                ", originId=" + originId +
                ", fileName='" + fileName + '\'' +
                ", chatRecord='" + chatRecord + '\'' +
                ", recordClass='" + recordClass + '\'' +
                ", similayClass='" + similarClass + '\'' +
                ", recordTime=" + recordTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(String chatRecord) {
        this.chatRecord = chatRecord;
    }

    public String getRecordClass() {
        return recordClass;
    }

    public void setRecordClass(String recordClass) {
        this.recordClass = recordClass;
    }

    public String getSimilarClass() {
        return similarClass;
    }

    public void setSimilarClass(String similayClass) {
        this.similarClass = similayClass;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordMd5() {
        return recordMd5;
    }

    public void setRecordMd5(String recordMd5) {
        this.recordMd5 = recordMd5;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}