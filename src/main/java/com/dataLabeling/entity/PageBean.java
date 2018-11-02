package com.dataLabeling.entity;

import java.util.List;

/**
 * 分页实体类
 */
public class PageBean<T> {
    private int pc;//上面表格当前页码 page code
    //    private int tp;//总页数 total page
    private int tr;//上面表格总记录数 total record
    private int ps;//上面表格每页记录数 page size
    private int ps1;//下面表格的每页记录数
    private List<RecordInfo> beanListUp;//上面表格当前页的记录
    private List<RecordInfo> beanListDown;//下面表格当前页的记录
    private T TClass;//用户点击的相似记录模板
    private List<T> TClasses;//所有相似记录模板
    private String keyword;//用户输入的搜索关键字
    private Integer appId;//应用id
    private String dataType;//已处理数据(dealed)还是未标注数据(notdeal)
    private String noHandledWord;//相似性 搜索的未处理的关键字

    public PageBean() {
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    /**
     * 计算TP
     * @return
     */
    public int getTp1() {
        int tp = tr/ps1;
        return tr%ps1==0?tp:tp+1;
    }

    public int getTp() {
        int tp = tr/ps;
        return tr%ps==0?tp:tp+1;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPs1() {
        return ps1;
    }

    public void setPs1(int ps1) {
        this.ps1 = ps1;
    }

    public List<RecordInfo> getBeanListUp() {
        return beanListUp;
    }

    public void setBeanListUp(List<RecordInfo> beanListUp) {
        this.beanListUp = beanListUp;
    }

    public List<RecordInfo> getBeanListDown() {
        return beanListDown;
    }

    public void setBeanListDown(List<RecordInfo> beanListDown) {
        this.beanListDown = beanListDown;
    }

    public T getTClass() {
        return TClass;
    }

    public void setTClass(T TClass) {
        this.TClass = TClass;
    }

    public List<T> getTClasses() {
        return TClasses;
    }

    public void setTClasses(List<T> TClasses) {
        this.TClasses = TClasses;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getNoHandledWord() {
        return noHandledWord;
    }

    public void setNoHandledWord(String noHandledWord) {
        this.noHandledWord = noHandledWord;
    }
}
