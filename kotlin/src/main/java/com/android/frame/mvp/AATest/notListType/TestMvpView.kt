package com.android.frame.mvp.AATest.notListType

import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvp.IBaseView

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
interface TestMvpView : IBaseView {

    fun showWeatherInfo(city: String, bean: WeatherBean?)

}