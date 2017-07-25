package com.autoCustomer.entity;

public class DeEventTarget {
    private Integer id;

    private Integer eventid;

    private Integer secretid;

    private String targetid;

    private String targetname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    public Integer getSecretid() {
        return secretid;
    }

    public void setSecretid(Integer secretid) {
        this.secretid = secretid;
    }

    public String getTargetid() {
        return targetid;
    }

    public void setTargetid(String targetid) {
        this.targetid = targetid == null ? null : targetid.trim();
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname == null ? null : targetname.trim();
    }
}