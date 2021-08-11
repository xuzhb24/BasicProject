package com.android.frame.mvvm.extra.LiveDataEntity;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:接口请求失败结果
 * 接口请求失败分为两种情况：一种是接口自己返回失败结果，此时isException为false，
 * 另一种是接口访问异常，如网络无连接导致无法访问，此时isException为true
 */
public class ErrorResponse<T> {

    private boolean isException;  //访问接口是否发生异常，true时exception不为空，false时response不为空
    private String message;
    private Throwable exception;
    private T response;

    public ErrorResponse(boolean isException, String message, Throwable exception, T response) {
        this.isException = isException;
        this.message = message;
        this.exception = exception;
        this.response = response;
    }

    public boolean isException() {
        return isException;
    }

    public void setException(boolean exception) {
        isException = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

}
