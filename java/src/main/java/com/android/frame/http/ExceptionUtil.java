package com.android.frame.http;

import android.text.TextUtils;

import com.android.util.NetworkUtil;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:异常处理类
 */
public class ExceptionUtil {

    public static String convertExceptopn(Throwable t) {
        if (t instanceof UnknownHostException) {
            return "网络不可用";
        }
        if (!TextUtils.isEmpty(t.getMessage()) && t.getMessage().contains("HTTP 504 Unsatisfiable Request (only-if-cached)")
                && !NetworkUtil.isConnected()) {
            return "网络不可用";  //这种情况主要是修改某些接口设置了缓存而且缓存失效后的提示信息
        }
        if (t instanceof SocketTimeoutException) {
            return "请求网络超时";
        }
        if (t instanceof HttpException) {
            return convertStatusCode((HttpException) t);
        }
        if (t instanceof ParseException || t instanceof JSONException) {
            return "数据解析错误";
        }
        return "未知错误，" + t.getMessage();
    }

    private static String convertStatusCode(HttpException e) {
        String errorMsg = "";
        switch (e.code()) {
            case 400:
                errorMsg = "错误请求";
                break;
            case 401:
                errorMsg = "未授权";
                break;
            case 403:
                errorMsg = "服务器拒绝请求";
                break;
            case 404:
                errorMsg = "请求的资源不存在";
                break;
            case 408:
                errorMsg = "请求超时";
                break;
            case 500:
                errorMsg = "服务器内部错误";
                break;
            case 502:
                errorMsg = "错误网关";
                break;
            case 503:
                errorMsg = "服务器暂不可用";
                break;
            case 504:
                errorMsg = "网关超时";
                break;
        }
        if (TextUtils.isEmpty(errorMsg)) {
            if (500 <= e.code() && e.code() <= 600) {
                errorMsg = "服务器处理请求出错";
            } else if (400 <= e.code() && e.code() <= 499) {
                errorMsg = "服务器无法处理请求";
            } else if (300 <= e.code() && e.code() <= 399) {
                errorMsg = "请求被重定向到其他页面";
            } else {
                errorMsg = "未知错误";
            }
        }
        return errorMsg + "[" + e.code() + "]";
    }

}
