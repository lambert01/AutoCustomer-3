package com.autoCustomer.entity;

public class DeList {
    private Integer listid;

    private String listname;

    private String accounttype;

    public Integer getListid() {
        return listid;
    }

    public void setListid(Integer listid) {
        this.listid = listid;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname == null ? null : listname.trim();
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype == null ? null : accounttype.trim();
    }
}