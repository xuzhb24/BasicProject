package com.android.frame.mvp.AATest.activity.weather

import com.android.frame.http.model.BaseResponse
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp
import com.android.frame.mvp.BasePresenter
import com.android.frame.mvp.extra.CustomObserver

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class WeatherPresenter : BasePresenter<WeatherView>() {

    private val mModel by lazy { WeatherModel() }

    fun getWeatherInfo(city: String) {
        mView?.let {
            mModel.getWeather(city)
                .subscribe(object : CustomObserver<BaseResponse<WeatherBeanMvp>>(it) {
                    override fun onSuccess(response: BaseResponse<WeatherBeanMvp>) {
                        response.data?.let {
                            mView?.showData(it)
                        }
                    }
                })
        }
    }

}