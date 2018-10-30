package com.dataLabeling.entity;

/**
 * 该类为访客记录相似类别标签
 */
public class SimilarClass {
    private Integer id;
    private String  similarClass;

    @Override
    public String toString() {
        return "SimilarClass{" +
                "id=" + id +
                ", similarClass='" + similarClass + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSimilarClass() {
        return similarClass;
    }

    public void setSimilarClass(String similarClass) {
        this.similarClass = similarClass;
    }
}
