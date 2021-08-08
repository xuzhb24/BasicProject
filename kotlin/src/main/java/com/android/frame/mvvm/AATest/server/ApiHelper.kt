package com.android.frame.mvvm.AATest.server

import com.android.frame.http.RetrofitFactory
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvvm.AATest.convert.NewsConverterFactory
import com.android.frame.mvvm.AATest.convert.WeatherConverterFactory
import okhttp3.Interceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by xuzhb on 2021/8/6
 * Desc:
 */
object ApiHelper {

    suspend fun getWeatherByQuery(city: String): BaseResponse<WeatherBean> {
        return createService(Config.WEATHER_URL, WeatherConverterFactory.create()).getWeatherByQuery(city)
    }

    suspend fun getWangYiNewsByField(page: Int, count: Int): BaseListResponse<NewsListBean> {
        return createService(Config.NEWS_URL, NewsConverterFactory.create()).getWangYiNewsByField(page, count)
    }

    private fun createService(
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