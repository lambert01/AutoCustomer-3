package com.autoCustomer.entity;

public class DeTagList {
    private Integer id;

    private String dimensionkey;

    private String tagname;

    private String accounttype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDimensionkey() {
        return dimensionkey;
    }

    public void setDimensionkey(String dimensionkey) {
        this.dimensionkey = dimensionkey == null ? null : dimensionkey.trim();
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname == null ? null : tagname.trim();
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype == null ? null : accounttype.trim();
    }
}