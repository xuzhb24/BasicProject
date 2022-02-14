package com.android.frame.mvvm.AATest.notListType

import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvvm.AATest.server.ApiHelper
import com.android.frame.mvvm.BaseViewModelWithData

/**
 * Created by xuzhb on 2021/8/6
 * Desc:BaseViewModelWithData的使用，简化了很多，BaseViewModel的使用参见TestMvvmFragmentViewModel
 */
class TestMvvmActivityViewModel : BaseViewModelWithData<BaseResponse<WeatherBean>>() {

    fun showWeatherInfo(city: String) {
        launchMain({ ApiHelper.getWeatherByQuery(city) })
    }

}