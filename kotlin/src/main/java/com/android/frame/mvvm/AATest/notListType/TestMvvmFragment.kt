package com.android.frame.mvvm.AATest.notListType

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.basicproject.databinding.FragmentTestMvcBinding
import com.android.frame.mvvm.BaseFragment
import com.android.util.JsonUtil
import com.android.util.LogUtil
import com.google.gson.Gson
import kotlin.random.Random

/**
 * Created by xuzhb on 2021/8/8
 * Desc:
 */
class TestMvvmFragment : BaseFragment<FragmentTestMvcBinding, TestMvvmFragmentViewModel>() {

    companion object {
        private const val TAG = "TestMvvmFragment"
        fun newInstance() = TestMvvmFragment()
    }

    private var mCity = ""
    private val mCities = mutableListOf(
        "深圳", "福州", "北京", "广州", "上海", "厦门",
        "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    )

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initViewModelObserver() {
        super.initViewModelObserver()
        viewModel.mSuccessData1.observe(this, Observer {
            val tip = "下拉刷新获取更多城市天气\n\n"
            binding.tv.text = tip + JsonUtil.formatJson(Gson().toJson(it))
            LogUtil.i(TAG, "请求成功")
        })
        viewModel.mErrorData1.observe(this, Observer {
            val tip = "下拉刷新获取更多城市天气\n\n"
            binding.tv.text = tip + "获取\"" + mCity + "\"天气情况失败"
            LogUtil.i(TAG, "请求失败")
            showToast(it.message)
        })
        viewModel.mSuccessData2.observe(this, Observer {
            LogUtil.i(TAG, "保存成功，${Gson().toJson(it)}")
        })
        viewModel.mErrorData2.observe(this, Observer {
            LogUtil.i(TAG, "保存失败，${it.message}")
        })
    }

    override fun initListener() {
    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    override fun refreshData() {
        //模拟一个页面多个接口请求的情况，假设其中一个接口不参与UI显示的逻辑，只保存数据
        viewModel.saveWeatherInfo(mCities[Random.nextInt(mCities.size)])  //假定这个接口只保存数据
        mCity = mCities[Random.nextInt(mCities.size)]
        viewModel.showWeatherInfo(mCity)  //主接口，会将接口结果反馈给UI
    }

    override fun loadFinish(isError: Boolean) {
        LogUtil.i(TAG, "加载完成：$isError")
        super.loadFinish(isError)
    }

}