package com.android.frame.http.AATest

import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.AATest.bean.WeatherBean
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by xuzhb on 2019/8/8
 * Desc:免费的公共接口 https://blog.csdn.net/rosener/article/details/81699698
 */
interface ApiService {

    //简单的访问IP地址，Retrofit
    @GET("/")
    fun accessUrl(): Call<ResponseBody>

    //简单的访问IP地址，Retrofit + RxJava
    @GET("/")
    fun accessUrlRxJava(): Observable<ResponseBody>

    //接口地址：https://www.apiopen.top/api.html#c14353b903984e699c31c08f639baaff
    //获取天气信息，@Query，GET请求
    @GET("weatherApi")
    fun getWeatherByQuery(@Query("city") city: String): Observable<BaseResponse<WeatherBean>>

    //获取天气信息，@QueryMap，GET请求
    @GET("weatherApi")
    fun getWeatherByQueryMap(@QueryMap map: HashMap<String, Any>): Observable<BaseResponse<WeatherBean>>

    //获取天气信息，@Field，POST请求
    @FormUrlEncoded
    @POST("weatherApi")
    fun getWeatherByField(@Field("city") city: String): Observable<BaseResponse<WeatherBean>>

    //获取天气信息，@FieldMap，POST请求
    @FormUrlEncoded
    @POST("weatherApi")
    fun getWeatherByFieldMap(@FieldMap map: HashMap<String, Any>): Observable<BaseResponse<WeatherBean>>

    //接口地址：https://www.apiopen.top/api.html#4c502eec73ce429fb1c4a7f519360d24
    //获取网易新闻，@Body，POST请求
    @POST("getWangYiNews")
    fun getWangYiNewsByBody(@Body requestBody: RequestBody): Observable<BaseListResponse<NewsListBean>>

}