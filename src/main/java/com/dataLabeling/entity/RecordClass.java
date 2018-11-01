package com.dataLabeling.entity;

/**
 * recordclass实体类
 */
public class RecordClass {
    private Integer id;
    private String  recordClass;
    private Integer appId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecordClass() {
        return recordClass;
    }

    public void setRecordClass(String recordClass) {
        this.recordClass = recordClass;
    }


    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}
