package com.android.frame.mvp.AATest

import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.AATest.bean.WeatherBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
interface ApiServiceMvp {

    @GET("weather_mini")
    fun getWeather(@Query("city") city: String): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("getWangYiNews")
    fun getNews(@Field("page") page: String, @Field("count") count: String): Observable<ResponseBody>


}