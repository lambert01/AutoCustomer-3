package com.autoCustomer.entity;

public class DeAcccountLevel {
    private Integer id;

    private Double accountmin;

    private Double accountmax;

    private String level;

    private String accounttype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAccountmin() {
        return accountmin;
    }

    public void setAccountmin(Double accountmin) {
        this.accountmin = accountmin;
    }

    public Double getAccountmax() {
        return accountmax;
    }

    public void setAccountmax(Double accountmax) {
        this.accountmax = accountmax;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype == null ? null : accounttype.trim();
    }
}