package com.android.frame.http;

import com.android.base.BaseApplication;
import com.android.frame.http.Interceptor.HttpCacheInterceptor;
import com.android.frame.http.Interceptor.HttpLogInterceptor;
import com.android.java.BuildConfig;
import com.android.util.FileUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:Retrofit配置类：创建Service
 */
public class RetrofitFactory {

    private RetrofitFactory() {

    }

    //静态内部类单例模式
    private static class SingleTonHolder {
        private static final RetrofitFactory holder = new RetrofitFactory();
    }

    //单例对象
    public static RetrofitFactory getInstance() {
        return SingleTonHolder.holder;
    }

    public <T> T createService(Class<T> clazz, String baseUrl) {
        return createService(clazz, baseUrl, GsonConverterFactory.create(), null, 30, false);
    }

    public <T> T createService(Class<T> clazz, String baseUrl, boolean cache) {
        return createService(clazz, baseUrl, GsonConverterFactory.create(), null, 30, cache);
    }

    public <T> T createService(Class<T> clazz, String baseUrl, long timeout, boolean cache) {
        return createService(clazz, baseUrl, GsonConverterFactory.create(), null, timeout, cache);
    }

    public <T> T createService(Class<T> clazz, String baseUrl, Converter.Factory factory, Interceptor interceptor, long timeout, boolean cache) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory);
        if (interceptor != null) {
            builder.client(getOkHttpBuilder(timeout, cache).addInterceptor(interceptor).build());
        } else {
            builder.client(getOkHttpBuilder(timeout, cache).build());
        }
        return builder.build().create(clazz);
    }

    /**
     * 设置OkHttpClient.Builder
     *
     * @param timeout 接口超时时长
     * @param cache   是否进行缓存
     */
    private OkHttpClient.Builder getOkHttpBuilder(long timeout, boolean cache) {
        //添加一个log拦截器,打印所有的log
//        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogInterceptor());
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)  //设置出现错误进行重新连接
                .cookieJar(CookieJarManage.getInstance());  //cookie持久化
        if (cache) {
            File cacheDir = new File(BaseApplication.getInstance().getCacheDir(), "httpCache");  //设置缓存路径
            long cacheSize = 10 * 1024 * 1024;  //设置缓存大小为10M
            builder.cache(new Cache(cacheDir, cacheSize))
                    .addInterceptor(new HttpCacheInterceptor());
        }
        if (BuildConfig.DEBUG) {
            builder.sslSocketFactory(SSLSocketFactoryUtil.getPassAnySSLSocketFactory())
                    .hostnameVerifier((hostname, session) -> true)  //不校验服务器返回的信息
                    .addInterceptor(httpLoggingInterceptor);  //打印日志，以便调试
        }
        return builder;
    }

    //清除缓存
    public boolean clearCache() {
        File cacheDir = new File(BaseApplication.getInstance().getCacheDir(), "httpCache");
        return FileUtil.deleteDirectory(cacheDir);
    }

}
