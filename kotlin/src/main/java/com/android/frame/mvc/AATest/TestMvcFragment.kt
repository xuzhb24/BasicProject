package com.android.frame.mvc.AATest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentTestMvcBinding
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvc.AATest.server.ApiHelper
import com.android.frame.mvc.BaseFragment
import com.android.frame.mvc.IBaseView
import com.android.frame.mvc.extra.http.CustomObserver
import com.android.util.JsonUtil
import com.google.gson.Gson
import kotlin.random.Random

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
class TestMvcFragment : BaseFragment<FragmentTestMvcBinding>() {

    companion object {
        fun newInstance() = TestMvcFragment()
    }

    private val mCities = mutableListOf(
        "深圳", "福州", "北京", "广州", "上海", "厦门",
        "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    )

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTestMvcBinding.inflate(inflater, container, false)

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    override fun refreshData() {
        //模拟一个页面多个接口请求的情况，假设其中一个接口不参与UI显示的逻辑，只保存数据
        saveWeatherInfo(mCities[Random.nextInt(mCities.size)])  //假定这个接口只保存数据
        showWeatherInfo(mCities[Random.nextInt(mCities.size)])  //主接口，会将接口结果反馈给UI
    }

    //这个接口只保存数据，不参与UI逻辑
    private fun saveWeatherInfo(city: String) {
        ApiHelper.getWeatherByQuery(city)
            //不显示加载状态，不显示加载弹窗，不处理加载完成的逻辑（needLoadFinish设为false，因为这个接口不参与UI界面的显示，让主接口处理加载状态）
            .subscribe(object : CustomObserver<BaseResponse<WeatherBean>>(
                this,
                false,
                false,
                needLoadFinish = false
            ) {
                override fun onSuccess(response: BaseResponse<WeatherBean>) {
                    println("TestMvcFragment 请求成功，可以保存数据了，${Gson().toJson(response)}")
                }

                override fun onFailure(
                    view: IBaseView?,
                    message: String,
                    isError: Boolean,
                    t: Throwable?,
                    response: BaseResponse<WeatherBean>?
                ) {
                    //如果这个接口加载异常时不需要提示Toast，重写onFailure并去掉super即可，onFailure中也可以处理其他失败的逻辑
//                    super.onFailure(view, message, isError, t, response)
                    println("TestMvcFragment 请求失败，${message}")
                }

            })
    }

    private fun showWeatherInfo(city: String) {
        ApiHelper.getWeatherByQuery(city)
            .subscribe(object : CustomObserver<BaseResponse<WeatherBean>>(this, false, true) {
                override fun onSuccess(response: BaseResponse<WeatherBean>) {
                    binding.tv.text = JsonUtil.formatJson(Gson().toJson(response))
                }
            })
    }

}