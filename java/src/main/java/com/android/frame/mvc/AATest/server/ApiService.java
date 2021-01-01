package com.android.frame.mvc.AATest.server;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public interface ApiService {

    //获取天气信息，@Query，GET请求
    @GET("weather_mini")
    Observable<ResponseBody> getWeatherByQuery(@Query("city") String city);

    //获取网易新闻，@Field，POST请求
    @FormUrlEncoded
    @POST("getWangYiNews")
    Observable<ResponseBody> getWangYiNewsByField(@Field("page") int page, @Field("count") int count);

}
