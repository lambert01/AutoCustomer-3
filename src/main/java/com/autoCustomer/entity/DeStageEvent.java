package com.autoCustomer.entity;

public class DeStageEvent {
    private Integer eventid;

    private String event;

    private Integer targetid;

    private String targetname;

    private Integer stageid;

    private Integer ismust;

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

    public Integer getTargetid() {
        return targetid;
    }

    public void setTargetid(Integer targetid) {
        this.targetid = targetid;
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname == null ? null : targetname.trim();
    }

    public Integer getStageid() {
        return stageid;
    }

    public void setStageid(Integer stageid) {
        this.stageid = stageid;
    }

    public Integer getIsmust() {
        return ismust;
    }

    public void setIsmust(Integer ismust) {
        this.ismust = ismust;
    }
}