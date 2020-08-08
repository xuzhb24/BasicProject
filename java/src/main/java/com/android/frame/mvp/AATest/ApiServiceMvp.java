package com.android.frame.mvp.AATest;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public interface ApiServiceMvp {

    @GET("weather_mini")
    Observable<ResponseBody> getWeather(@Query("city") String city);

    @GET("getWangYiNews")
    Observable<ResponseBody> getNews(@Query("page") String page, @Query("count") String count);

}
