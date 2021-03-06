package com.qf.dto;

import com.qf.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

public class TProductCartDTO implements Serializable {

    private TProduct product;

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    private int count;

    private Date updateTime;


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
