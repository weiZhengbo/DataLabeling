package com.dataLabeling.entity;

/**
 * 请求条件实体类
 */
public class QueryVO {
    private String pc;//表格当前页码 page code
    private String noHandledWord;//相似性 搜索的关键字
    private String appId;//应用id
    private String clickwordId;//用户点击关键词的id
    private String dataType;//请求数据类型 是已处理还是未处理
    private String refresh;//是否刷新页面  yes或空-刷新数据 no-不刷新
    private String keyword;//用户搜索的关键词

    public QueryVO() {
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getNoHandledWord() {
        return noHandledWord;
    }

    public void setNoHandledWord(String noHandledWord) {
        this.noHandledWord = noHandledWord;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClickwordId() {
        return clickwordId;
    }

    public void setClickwordId(String clickwordId) {
        this.clickwordId = clickwordId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
