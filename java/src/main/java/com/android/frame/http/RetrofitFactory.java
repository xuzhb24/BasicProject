package com.android.frame.http;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.util.concurrent.TimeUnit;

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
        return createService(clazz, baseUrl, GsonConverterFactory.create(), null, 30);
    }

    public <T> T createService(Class<T> clazz, String baseUrl, long timeout) {
        return createService(clazz, baseUrl, GsonConverterFactory.create(), null, timeout);
    }

    public <T> T createService(Class<T> clazz, String baseUrl, Converter.Factory factory, Interceptor interceptor, long timeout) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory);
        if (interceptor != null) {
            builder.client(getOkHttpBuilder(timeout).addInterceptor(interceptor).build());
        } else {
            builder.client(getOkHttpBuilder(timeout).build());
        }
        return builder.build().create(clazz);
    }

    private OkHttpClient.Builder getOkHttpBuilder(long timeout) {
        //添加一个log拦截器,打印所有的log
//        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)  //设置出现错误进行重新连接
                .cookieJar(CookieJarManage.getInstance())  //cookie持久化
                .sslSocketFactory(SSLSocketFactoryUtil.getPassAnySSLSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })  //不校验服务器返回的信息
                .addInterceptor(httpLoggingInterceptor);  //打印日志，以便调试
        return builder;
    }

}
