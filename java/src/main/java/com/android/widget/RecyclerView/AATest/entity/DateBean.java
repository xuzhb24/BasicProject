package com.android.widget.RecyclerView.AATest.entity;

import java.io.Serializable;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class DateBean implements Serializable {

    private String date;

    public DateBean(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
