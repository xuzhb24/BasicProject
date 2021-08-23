package com.android.frame.mvc.AATest.server;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.convert.NewsFunction;
import com.android.frame.mvc.AATest.convert.WeatherFunction;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvc.AATest.entity.WeatherBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class ApiHelper {

    public static Observable<BaseResponse<WeatherBean>> getWeatherByQuery(String city) {
        return createService(Config.WEATHER_URL, true)
                .getWeatherByQuery(city)
                .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
                .map(new WeatherFunction())
                .compose(SchedulerUtil.ioToMain());
    }

    public static Observable<BaseListResponse<NewsListBean>> getWangYiNewsByField(int page, int count) {
        return createService(Config.NEWS_URL, false)
                .getWangYiNewsByField(page, count)
                .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
                .map(new NewsFunction())
                .compose(SchedulerUtil.ioToMain());
    }

    private static ApiService createService(String baseUrl, boolean cache) {
        return RetrofitFactory.getInstance().createService(
                ApiService.class, baseUrl, GsonConverterFactory.create(),
                null, 30, cache);
    }

}
