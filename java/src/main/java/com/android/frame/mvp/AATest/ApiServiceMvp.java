package com.android.frame.mvp.AATest;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.ResponseBody;
import retrofit2.http.*;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public interface ApiServiceMvp {

    @GET("weather_mini")
    Observable<ResponseBody> getWeather(@Query("city") String city);

    @FormUrlEncoded
    @POST("getWangYiNews")
    Observer<ResponseBody> getNews(@Field("page") String page, @Field("count") String count);

}
