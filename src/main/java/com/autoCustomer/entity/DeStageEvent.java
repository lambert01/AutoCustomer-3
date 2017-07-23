package com.autoCustomer.entity;

public class DeStageEvent {
    private Integer eventid;

    private String event;

    private String targetid;

    private String targetname;

    private Integer stageid;


    private Integer isrelated;

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

    public String getTargetid() {
        return targetid;
    }

    public void setTargetid(String targetid) {
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


    public Integer getIsrelated() {
        return isrelated;
    }

    public void setIsrelated(Integer isrelated) {
        this.isrelated = isrelated;
    }
}