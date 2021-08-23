package com.android.frame.mvvm.AATest.server;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvvm.AATest.convert.NewsConverterFactory;
import com.android.frame.mvvm.AATest.convert.WeatherConverterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import retrofit2.Converter;

/**
 * Created by xuzhb on 2021/8/10
 * Desc:
 */
public class ApiHelper {

    public static Observable<BaseResponse<WeatherBean>> getWeatherByQuery(String city) {
        return createService(Config.WEATHER_URL, WeatherConverterFactory.create(), true)
                .getWeatherByQuery(city)
                .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
                .compose(SchedulerUtil.ioToMain());
    }

    public static Observable<BaseListResponse<NewsListBean>> getWangYiNewsByField(int page, int count) {
        return createService(Config.NEWS_URL, NewsConverterFactory.create(), false)
                .getWangYiNewsByField(page, count)
                .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
                .compose(SchedulerUtil.ioToMain());
    }

    private static ApiService createService(String baseUrl, Converter.Factory factory, boolean cache) {
        return RetrofitFactory.getInstance().createService(
                ApiService.class, baseUrl, factory,
                null, 30, cache);
    }

}
