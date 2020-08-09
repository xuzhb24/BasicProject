package com.android.frame.http.interceptor;

import com.android.util.LogUtil;
import com.android.util.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuzhb on 2020/8/8
 * Desc:网络缓存拦截器，只支持GET请求
 * 参考：https://www.jianshu.com/p/7aa8f3443e05
 * https://www.jianshu.com/p/dbda0bb8d541
 * https://www.jianshu.com/p/ea2055db3dd3
 */
public class HttpCacheInterceptor implements Interceptor {

    private static final String TAG = "HttpCacheInterceptor";

    private int maxAge;    //缓存过期时间，单位是秒
    private int maxStale;  //缓存过期时间，在请求头设置有效，在响应头设置无效

    public HttpCacheInterceptor() {
        this.maxAge = 0;  //默认不使用缓存
        this.maxStale = 3 * 24 * 60 * 60;  //默认缓存3天
    }

    public HttpCacheInterceptor(int maxAge, int maxStale) {
        this.maxAge = maxAge;
        this.maxStale = maxStale;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean isConnected = NetworkUtil.isConnected();
        LogUtil.w(TAG, "网络是否连接：" + isConnected);
        if (!isConnected) {  //无网络的情况
            LogUtil.w(TAG, "从缓存获取数据，max-stale=" + maxStale);
            CacheControl control = new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(maxStale, TimeUnit.SECONDS)
                    .build();
            request = request.newBuilder()
                    .cacheControl(control)  //从缓存读取
                    .build();
        }
        Response response = chain.proceed(request);
        if (isConnected) {  //有网络的情况
            LogUtil.w(TAG, "max-age=" + maxAge);
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)  //如果想要不缓存，maxAge直接设置为0
                    .build();
        }
        return response;
    }
}