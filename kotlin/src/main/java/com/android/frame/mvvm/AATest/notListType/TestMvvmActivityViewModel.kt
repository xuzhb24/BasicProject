package com.android.frame.mvvm.AATest.notListType

import com.android.basicproject.databinding.ActivityTestMvcBinding
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvvm.AATest.server.ApiHelper
import com.android.frame.mvvm.BaseViewModelWithData
import com.android.util.JsonUtil
import com.google.gson.Gson

/**
 * Created by xuzhb on 2021/8/6
 * Desc:BaseViewModelWithData的使用，简化了很多，BaseViewModel的使用参见TestMvvmFragmentViewModel
 */
class TestMvvmActivityViewModel : BaseViewModelWithData<BaseResponse<WeatherBean>, ActivityTestMvcBinding>() {

    private var mCity = ""

    fun showWeatherInfo(city: String) {
        mCity = city
        launchMain({ ApiHelper.getWeatherByQuery(city) })
    }

    override fun onSuccess(response: BaseResponse<WeatherBean>) {
        val tip = "下拉刷新获取更多城市天气\n\n"
        binding.tv.text = tip + JsonUtil.formatJson(Gson().toJson(response))
    }

    override fun onFailure(message: String, isException: Boolean, exception: Throwable?, response: BaseResponse<WeatherBean>?) {
        super.onFailure(message, isException, exception, response)
        val tip = "下拉刷新获取更多城市天气\n\n"
        binding.tv.text = tip + "获取\"" + mCity + "\"天气情况失败"
    }

}