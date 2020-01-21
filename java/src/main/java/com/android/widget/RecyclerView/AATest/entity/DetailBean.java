package com.android.widget.RecyclerView.AATest.entity;

import java.io.Serializable;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class DetailBean implements Serializable {

    private String detail;
    private String time;

    public DetailBean(String detail, String time) {
        this.detail = detail;
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
