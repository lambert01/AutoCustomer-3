package com.autoCustomer.entity;

public class DeProducts {
    private Integer productid;

    private String productname;

    private Double productprice;

    private Integer productquantity;

    private Double amountdiscount;

    private String accounttype;

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public Double getProductprice() {
        return productprice;
    }

    public void setProductprice(Double productprice) {
        this.productprice = productprice;
    }

    public Integer getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(Integer productquantity) {
        this.productquantity = productquantity;
    }

    public Double getAmountdiscount() {
        return amountdiscount;
    }

    public void setAmountdiscount(Double amountdiscount) {
        this.amountdiscount = amountdiscount;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype == null ? null : accounttype.trim();
    }
}