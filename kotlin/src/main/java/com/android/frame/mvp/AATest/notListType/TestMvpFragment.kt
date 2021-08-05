package com.android.frame.mvp.AATest.notListType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentTestMvcBinding
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvp.BaseFragment
import com.android.util.JsonUtil
import com.google.gson.Gson

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpFragment : BaseFragment<FragmentTestMvcBinding, TestMvpView, TestMvpPresenter>(), TestMvpView {

    companion object {
        fun newInstance() = TestMvpFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
    }

    override fun getPresenter() = TestMvpPresenter()

    override fun showWeatherInfo(bean: WeatherBean?) {
        val tip = "下拉刷新获取更多城市天气\n\n"
        if (bean != null) {  //获取数据成功
            binding.tv.text = tip + JsonUtil.formatJson(Gson().toJson(bean))
        } else {  //获取数据失败
            binding.tv.text = tip
        }
    }

}