package com.android.frame.mvp.AATest.notListType

import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestMvcBinding
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvp.BaseActivity
import com.android.util.JsonUtil
import com.google.gson.Gson

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpActivity : BaseActivity<ActivityTestMvcBinding, TestMvpView, TestMvpPresenter>(), TestMvpView {

    override fun handleView(savedInstanceState: Bundle?) {
        mTitleBar?.titleText = "基类Activity(MVP)"
    }

    override fun initListener() {
    }

    override fun getPresenter() = TestMvpPresenter()

    override fun showWeatherInfo(city: String, bean: WeatherBean?) {
        val tip = "下拉刷新获取更多城市天气\n\n"
        if (bean != null) {  //获取数据成功
            binding.tv.text = tip + JsonUtil.formatJson(Gson().toJson(bean))
        } else {  //获取数据失败
            binding.tv.text = tip + "获取\"" + city + "\"天气情况失败"
        }
    }
}