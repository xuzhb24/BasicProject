package com.android.frame.mvvm.AATest.notListType

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.basicproject.databinding.ActivityTestMvcBinding
import com.android.frame.mvvm.BaseActivity
import com.android.util.JsonUtil
import com.google.gson.Gson
import kotlin.random.Random

/**
 * Created by xuzhb on 2021/8/6
 * Desc:
 */
class TestMvvmActivity : BaseActivity<ActivityTestMvcBinding, TestMvvmActivityViewModel>() {

    private var mCity = ""
    private val mCities = mutableListOf(
        "深圳", "福州", "北京", "广州", "上海", "厦门",
        "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    )

    override fun handleView(savedInstanceState: Bundle?) {
        mTitleBar?.titleText = "基类Activity(MVVM)"
    }

    override fun initViewModelObserver() {
        super.initViewModelObserver()
        viewModel.successData.observe(this, Observer {
            val tip = "下拉刷新获取更多城市天气\n\n"
            binding.tv.text = tip + JsonUtil.formatJson(Gson().toJson(it))
        })
        viewModel.errorData.observe(this, Observer {
            val tip = "下拉刷新获取更多城市天气\n\n"
            binding.tv.text = tip + "获取\"" + mCity + "\"天气情况失败"
            showToast(it.message)
        })
    }

    override fun initListener() {
    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    override fun refreshData() {
        mCity = mCities[Random.nextInt(mCities.size)]
        viewModel.showWeatherInfo(mCity)
    }

}