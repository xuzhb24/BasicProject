package com.android.frame.http.Interceptor;

import com.android.util.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuzhb on 2020/8/8
 * Desc:限制最多重试次数的拦截器
 */
public class MaxRetryInterceptor implements Interceptor {

    private static final String TAG = "MaxRetryInterceptor";

    private int maxRetryCount;   //最大重试次数
    private int retryCount = 1;

    public MaxRetryInterceptor(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);  //默认请求1次
        LogUtil.w(TAG, "retry count:" + retryCount);
        while (!response.isSuccessful() && retryCount < maxRetryCount) {
            retryCount++;
            LogUtil.w(TAG, "retry count:" + retryCount);
            response = chain.proceed(request);  //重试
        }
        return response;
    }
}
