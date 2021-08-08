package com.android.frame.mvvm.AATest.server

import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvc.AATest.entity.WeatherBean
import retrofit2.http.*

/**
 * Created by xuzhb on 2021/8/6
 * Desc:
 */
interface ApiService {

    //获取天气信息，@Query，GET请求
    @GET("weather_mini")
    suspend fun getWeatherByQuery(@Query("city") city: String): BaseResponse<WeatherBean>

    //获取网易新闻，@Field，POST请求
    @FormUrlEncoded
    @POST("getWangYiNews")
    suspend fun getWangYiNewsByField(@Field("page") page: Int, @Field("count") count: Int): BaseListResponse<NewsListBean>

}