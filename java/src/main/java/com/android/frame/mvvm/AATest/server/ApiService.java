package com.android.frame.mvvm.AATest.server;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvc.AATest.entity.WeatherBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xuzhb on 2021/8/10
 * Desc:
 */
public interface ApiService {

    //获取天气信息，@Query，GET请求
    @GET("weather_mini")
    Observable<BaseResponse<WeatherBean>> getWeatherByQuery(@Query("city") String city);

    //获取网易新闻，@Field，POST请求
    @FormUrlEncoded
    @POST("getWangYiNews")
    Observable<BaseListResponse<NewsListBean>> getWangYiNewsByField(@Field("page") int page, @Field("count") int count);

}
