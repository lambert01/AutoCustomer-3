package com.autoCustomer.entity;

import java.util.Date;

public class Api {
	
	private Integer id;
	private Integer version;
	private String accessToken;
	private Date acctime;
	private String appid;
	private String sercet;
	private Date gaintime;
	private Date uptime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getAccess_token() {
		return accessToken;
	}
	public void setAccess_token(String accessToken) {
		this.accessToken = accessToken;
	}
	public Date getAcctime() {
		return acctime;
	}
	public void setAcctime(Date acctime) {
		this.acctime = acctime;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSercet() {
		return sercet;
	}
	public void setSercet(String sercet) {
		this.sercet = sercet;
	}
	public Date getGaintime() {
		return gaintime;
	}
	public void setGaintime(Date gaintime) {
		this.gaintime = gaintime;
	}
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	
	
}
