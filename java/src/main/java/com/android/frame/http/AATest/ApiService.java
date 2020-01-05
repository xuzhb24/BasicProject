package com.android.frame.http.AATest;

import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.frame.http.AATest.bean.WeatherBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:免费的公共接口 https://blog.csdn.net/rosener/article/details/81699698
 */
public interface ApiService {

    //简单的访问IP地址，Retrofit
    @GET("/")
    Call<ResponseBody> accessUrl();

    //简单的访问IP地址，Retrofit + RxJava
    @GET("/")
    Observable<ResponseBody> accessUrlRxJava();

    //获取天气信息，@Query，GET请求
    @GET("weather_mini")
    Observable<WeatherBean> getWeatherByQuery(@Query("city") String city);

    //获取天气信息，@QueryMap，GET请求
    @GET("weather_mini")
    Observable<WeatherBean> getWeatherByQueryMap(@QueryMap HashMap<String, Object> map);

    //接口地址：https://www.apiopen.top/api.html#4c502eec73ce429fb1c4a7f519360d24
    //获取网易新闻，@Field，POST请求
    @FormUrlEncoded
    @POST("getWangYiNews")
    Observable<NewsListBean> getWangYiNewsByField(@Field("page") String page, @Field("count") String count);

    //获取网易新闻，@FieldMap，POST请求
    @FormUrlEncoded
    @POST("getWangYiNews")
    Observable<NewsListBean> getWangYiNewsByFieldMap(@FieldMap HashMap<String, Object> map);

    //获取网易新闻，@Body，POST请求
    @POST("getWangYiNews")
    Observable<NewsListBean> getWangYiNewsByBody(@Body RequestBody requestBody);

}
