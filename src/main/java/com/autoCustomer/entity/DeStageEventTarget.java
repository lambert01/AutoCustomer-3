package com.autoCustomer.entity;

public class DeStageEventTarget {
    private Integer eventid;

    private String event;

    private String eventname;

    private Integer stageid;

    private String targetid;

    private String targetname;

    private Integer isrelated;

    private Integer ismultiselect;

    private String accounttype;

    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event == null ? null : event.trim();
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname == null ? null : eventname.trim();
    }

    public Integer getStageid() {
        return stageid;
    }

    public void setStageid(Integer stageid) {
        this.stageid = stageid;
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

    public Integer getIsrelated() {
        return isrelated;
    }

    public void setIsrelated(Integer isrelated) {
        this.isrelated = isrelated;
    }

    public Integer getIsmultiselect() {
        return ismultiselect;
    }

    public void setIsmultiselect(Integer ismultiselect) {
        this.ismultiselect = ismultiselect;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype == null ? null : accounttype.trim();
    }
}