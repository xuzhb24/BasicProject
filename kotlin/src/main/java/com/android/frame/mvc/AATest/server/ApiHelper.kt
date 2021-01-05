package com.android.frame.mvc.AATest.server

import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.convert.NewsFunction
import com.android.frame.mvc.AATest.convert.WeatherFunction
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvc.AATest.entity.WeatherBean
import io.reactivex.Observable
import okhttp3.Interceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by xuzhb on 2020/7/29
 * Desc:
 */
object ApiHelper {

    fun getWeatherByQuery(city: String): Observable<BaseResponse<WeatherBean>> {
        return createService(Config.WEATHER_URL)
            .getWeatherByQuery(city)
            .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
            .map(WeatherFunction())
            .compose(SchedulerUtil.ioToMain())
    }

    fun getWangYiNewsByField(page: Int, count: Int): Observable<BaseListResponse<NewsListBean>> {
        return createService(Config.NEWS_URL)
            .getWangYiNewsByField(page, count)
            .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
            .map(NewsFunction())
            .compose(SchedulerUtil.ioToMain())
    }

    fun createService(
        baseUrl: String,
        factory: Converter.Factory = GsonConverterFactory.create(),
        interceptor: Interceptor? = null,
        timeout: Long = 30L,    //默认超时时长
        cache: Boolean = false  //是否进行缓存
    ): ApiService {
        return RetrofitFactory.instance.createService(
            ApiService::class.java, baseUrl,
            factory, interceptor, timeout, cache
        )
    }

}