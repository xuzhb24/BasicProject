package com.android.widget.RecyclerView.AATest.entity;

import java.io.Serializable;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class MonthBean implements Serializable {

    private String month;
    private String count;

    public MonthBean(String month, String count) {
        this.month = month;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
