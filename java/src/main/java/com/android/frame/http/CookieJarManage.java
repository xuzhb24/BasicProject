package com.android.frame.http;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:持久化Cookie管理
 */
public class CookieJarManage implements CookieJar {

    private Map<String, List<Cookie>> cookieStore = new HashMap<>();

    private CookieJarManage() {

    }

    //静态内部类单例模式
    private static class SingleTonHolder {
        private static CookieJarManage holder = new CookieJarManage();
    }

    //单例对象
    public static CookieJarManage getInstance() {
        return SingleTonHolder.holder;
    }

    //网路访问后将服务器返回的cookies和对应的url存储在cookieStore中
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);
    }

    //网路访问开始的时候，根据访问的url去查找cookie，然后将cokies放到请求头里面
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<>();
    }

}
