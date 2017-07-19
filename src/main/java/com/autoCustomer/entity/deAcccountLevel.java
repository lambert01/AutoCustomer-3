package com.autoCustomer.entity;

public class deAcccountLevel {
    private Integer id;

    private Double accountmin;

    private Double accountmax;

    private String level;

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
}