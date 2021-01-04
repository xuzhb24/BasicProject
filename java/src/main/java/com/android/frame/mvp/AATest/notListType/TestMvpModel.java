package com.android.frame.mvp.AATest.notListType;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.convert.WeatherFunction;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvc.AATest.server.ApiService;
import com.android.frame.mvc.AATest.server.Config;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpModel {

    public Observable<BaseResponse<WeatherBean>> getWeatherInfo(String city) {
        return RetrofitFactory.getInstance().createService(ApiService.class, Config.WEATHER_URL)
                .getWeatherByQuery(city)
                .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
                .map(new WeatherFunction())
                .compose(SchedulerUtil.ioToMain());
    }

}
