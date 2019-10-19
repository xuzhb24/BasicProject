package com.android.frame.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by xuzhb on 2019/8/8
 * Desc:Retrofit配置类：创建Service
 */
class RetrofitFactory {

    companion object {
        //单例对象
        val instance = SingleTonHolder.holder
    }

    //静态内部类单例模式
    private object SingleTonHolder {
        val holder = RetrofitFactory()
    }

    fun <T> createService(
        clazz: Class<T>,
        baseUrl: String,
        factory: Converter.Factory = GsonConverterFactory.create(),
        interceptor: Interceptor? = null,
        timeout: Long = 30L  //默认超时时长
    ): T {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(factory)
        if (interceptor != null) {
            builder.client(getOkHttpBuilder(timeout).addInterceptor(interceptor).build())
        } else {
            builder.client(getOkHttpBuilder(timeout).build())
        }
        return builder.build().create(clazz)
    }

    private fun getOkHttpBuilder(timeout: Long): OkHttpClient.Builder {
        //添加一个log拦截器,打印所有的log
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger())
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
            .cookieJar(CookieJarManage.instance)  //cookie持久化
            .sslSocketFactory(SSLSocketFactoryUtil.getPassAnySSLSocketFactory())  //不校验证书
            .hostnameVerifier(object : HostnameVerifier {
                override fun verify(p0: String?, p1: SSLSession?): Boolean = true
            })  //不校验服务器返回的信息
            .addInterceptor(httpLoggingInterceptor) //打印日志，以便调试
        return builder
    }

}