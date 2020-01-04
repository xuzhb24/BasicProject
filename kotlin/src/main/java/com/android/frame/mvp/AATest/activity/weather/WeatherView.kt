package com.android.frame.mvp.AATest.activity.weather

import com.android.frame.mvp.AATest.bean.WeatherBeanMvp
import com.android.frame.mvp.IBaseView

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
interface WeatherView : IBaseView {

    //展示天气信息
    fun showData(bean: WeatherBeanMvp)

}