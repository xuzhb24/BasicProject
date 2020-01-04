package com.android.frame.mvp.AATest.activity.weather

import com.android.frame.http.AATest.ApiService
import com.android.frame.http.AATest.UrlConstant
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvp.AATest.ApiServiceMvp
import com.android.frame.mvp.AATest.UrlConstantMvp
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp
import com.android.frame.mvp.AATest.convert.WeatherFunction
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class WeatherModel {

    fun getWeather(city: String): Observable<BaseResponse<WeatherBeanMvp>> {
        return RetrofitFactory.instance.createService(ApiServiceMvp::class.java, UrlConstantMvp.WEATHER_URL)
            .getWeather(city)
            .map(WeatherFunction())
            .compose(SchedulerUtil.ioToMain())
    }

}