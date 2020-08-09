package com.android.frame.mvp.AATest

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
interface ApiServiceMvp {

    @GET("weather_mini")
    fun getWeather(@Query("city") city: String): Observable<ResponseBody>

    @GET("getWangYiNews")
    fun getNews(@Query("page") page: String, @Query("count") count: String): Observable<ResponseBody>


}