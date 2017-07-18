package com.autoCustomer.entity;

public class DeStageOrder {
    private Integer id;

    private Integer stageid;

    private String stagename;

    private Integer hasorder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStageid() {
        return stageid;
    }

    public void setStageid(Integer stageid) {
        this.stageid = stageid;
    }

    public String getStagename() {
        return stagename;
    }

    public void setStagename(String stagename) {
        this.stagename = stagename == null ? null : stagename.trim();
    }

    public Integer getHasorder() {
        return hasorder;
    }

    public void setHasorder(Integer hasorder) {
        this.hasorder = hasorder;
    }
}