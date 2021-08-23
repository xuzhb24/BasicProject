package com.android.frame.mvvm.AATest.notListType

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.basicproject.databinding.FragmentTestMvcBinding
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvvm.AATest.server.ApiHelper
import com.android.frame.mvvm.BaseViewModel
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse
import com.android.util.JsonUtil
import com.android.util.ToastUtil
import com.google.gson.Gson

/**
 * Created by xuzhb on 2021/8/8
 * Desc:
 */
class TestMvvmFragmentViewModel : BaseViewModel<FragmentTestMvcBinding>() {

    private val mSuccessData = MutableLiveData<BaseResponse<WeatherBean>>()
    private val mErrorData = MutableLiveData<ErrorResponse<BaseResponse<WeatherBean>>>()

    private val mSuccessData2 = MutableLiveData<BaseResponse<WeatherBean>>()
    private val mErrorData2 = MutableLiveData<ErrorResponse<BaseResponse<WeatherBean>>>()

    private var mCity = ""

    fun showWeatherInfo(city: String) {  //主接口或者页面最后一个接口请求，由它来控制加载完成的逻辑
        mCity = city
        launch({ ApiHelper.getWeatherByQuery(city) }, mSuccessData, mErrorData, false, true, "处理中")
    }

    fun saveWeatherInfo(city: String) {  //次要的接口，假定这个接口只保存数据
        //不显示加载状态，不显示加载弹窗，不处理加载完成的逻辑（needLoadFinish设为false，因为这个接口不参与UI界面的显示，让主接口处理加载状态
        launch({ ApiHelper.getWeatherByQuery(city) }, mSuccessData2, mErrorData2, false, false, needLoadFinish = false)
    }

    override fun observe(fragment: Fragment, owner: LifecycleOwner) {
        val tip = "下拉刷新获取更多城市天气\n\n"
        mSuccessData.observe(owner, Observer {
            binding.tv.text = tip + JsonUtil.formatJson(Gson().toJson(it))
        })
        mErrorData.observe(owner, Observer {
            ToastUtil.showToast(it.message, true)
            binding.tv.text = tip + "获取\"" + mCity + "\"天气情况失败"
        })
        mSuccessData2.observe(owner, Observer {
            println("TestMvvmFragment 请求成功，可以保存数据了，${Gson().toJson(it)}")
        })
        mErrorData2.observe(owner, Observer {
            println("TestMvvmFragment 请求失败，${it.message}")
        })
    }

}