package com.android.frame.mvc.AATest.server

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by xuzhb on 2020/7/29
 * Desc:
 */
interface ApiService {

    //获取天气信息，@Query，GET请求
    @GET("weather_mini")
    fun getWeatherByQuery(@Query("city") city: String): Observable<ResponseBody>

    //获取网易新闻，@Field，POST请求
    @FormUrlEncoded
    @POST("getWangYiNews")
    fun getWangYiNewsByField(@Field("page") page: String, @Field("count") count: String): Observable<ResponseBody>

}