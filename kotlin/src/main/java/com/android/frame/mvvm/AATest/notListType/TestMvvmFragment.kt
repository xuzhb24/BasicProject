package com.android.frame.mvvm.AATest.notListType

import android.os.Bundle
import com.android.basicproject.databinding.FragmentTestMvcBinding
import com.android.frame.mvvm.BaseFragment
import kotlin.random.Random

/**
 * Created by xuzhb on 2021/8/8
 * Desc:
 */
class TestMvvmFragment : BaseFragment<FragmentTestMvcBinding, TestMvvmFragmentViewModel>() {

    companion object {
        fun newInstance() = TestMvvmFragment()
    }

    private val mCities = mutableListOf(
        "深圳", "福州", "北京", "广州", "上海", "厦门",
        "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    )

    override fun handleView(savedInstanceState: Bundle?) {
        mTitleBar?.titleText = "基类Activity(MVVM)"
    }

    override fun initListener() {
    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    override fun refreshData() {
        //模拟一个页面多个接口请求的情况，假设其中一个接口不参与UI显示的逻辑，只保存数据
        viewModel.saveWeatherInfo(mCities[Random.nextInt(mCities.size)])  //假定这个接口只保存数据
        viewModel.showWeatherInfo(mCities[Random.nextInt(mCities.size)])  //主接口，会将接口结果反馈给UI
    }

    override fun loadFinish(isError: Boolean) {
        println("TestMvvmFragment 加载完成：$isError")
        super.loadFinish(isError)
    }

}