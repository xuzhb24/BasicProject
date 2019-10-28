package com.android.frame.http.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:bean基类，列表类型
 */
public class BaseListResponse<T> implements Serializable {

    private int code;
    private String msg;
    private List<T> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }

}