package com.android.frame.http.model;

import java.io.Serializable;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:bean基类，非列表类型
 */
public class BaseResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }

    public boolean isTokenOut() {  //登录失效
        return this.code == -111;  //这里假设code是-111
    }

    public String getMessage() {
        return msg;
    }

}
