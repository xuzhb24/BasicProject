package com.android.frame.mvp.AATest.notListType

import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.convert.WeatherFunction
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvc.AATest.server.ApiService
import com.android.frame.mvc.AATest.server.Config
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpModel {

    fun getWeatherInfo(city: String): Observable<BaseResponse<WeatherBean>> {
        return RetrofitFactory.instance.createService(ApiService::class.java, Config.WEATHER_URL)
            .getWeatherByQuery(city)
            .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
            .map(WeatherFunction())
            .compose(SchedulerUtil.ioToMain())
    }

}