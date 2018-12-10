package com.dataLabeling.entity;

public class SimilarRecord {

    private Integer id;

    private Integer isSimilar;

    private String type;

    private String visit_ques;

    private String match_ques;

    private Integer isValid;

    private Integer flag;

    private Integer appId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsSimilar() {
        return isSimilar;
    }

    public void setIsSimilar(Integer isSimilar) {
        this.isSimilar = isSimilar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisit_ques() {
        return visit_ques;
    }

    public void setVisit_ques(String visit_ques) {
        this.visit_ques = visit_ques;
    }

    public String getMatch_ques() {
        return match_ques;
    }

    public void setMatch_ques(String match_ques) {
        this.match_ques = match_ques;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}
