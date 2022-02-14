package com.android.frame.mvvm.AATest.notListType

import androidx.lifecycle.MutableLiveData
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvvm.AATest.server.ApiHelper
import com.android.frame.mvvm.BaseViewModel
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse

/**
 * Created by xuzhb on 2021/8/8
 * Desc:
 */
class TestMvvmFragmentViewModel : BaseViewModel() {

    val mSuccessData1 = MutableLiveData<BaseResponse<WeatherBean>>()
    val mErrorData1 = MutableLiveData<ErrorResponse<BaseResponse<WeatherBean>>>()

    val mSuccessData2 = MutableLiveData<BaseResponse<WeatherBean>>()
    val mErrorData2 = MutableLiveData<ErrorResponse<BaseResponse<WeatherBean>>>()

    fun showWeatherInfo(city: String) {  //主接口或者页面最后一个接口请求，由它来控制加载完成的逻辑
        launch({ ApiHelper.getWeatherByQuery(city) }, mSuccessData1, mErrorData1, false, true, "处理中")
    }

    fun saveWeatherInfo(city: String) {  //次要的接口，假定这个接口只保存数据
        //不显示加载状态，不显示加载弹窗，不处理加载完成的逻辑（needLoadFinish设为false，因为这个接口不参与UI界面的显示，让主接口处理加载状态
        launch({ ApiHelper.getWeatherByQuery(city) }, mSuccessData2, mErrorData2, false, false, needLoadFinish = false)
    }

}