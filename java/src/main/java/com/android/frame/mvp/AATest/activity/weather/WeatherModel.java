package com.android.frame.mvp.AATest.activity.weather;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvp.AATest.ApiServiceMvp;
import com.android.frame.mvp.AATest.UrlConstantMvp;
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp;
import com.android.frame.mvp.AATest.convert.WeatherFunction;
import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class WeatherModel {

    public Observable<BaseResponse<WeatherBeanMvp>> getWeather(String city) {
        return RetrofitFactory.getInstance().createService(ApiServiceMvp.class, UrlConstantMvp.WEATHER_URL)
                .getWeather(city)
                .map(new WeatherFunction())
                .compose(SchedulerUtil.ioToMain());
    }

}
