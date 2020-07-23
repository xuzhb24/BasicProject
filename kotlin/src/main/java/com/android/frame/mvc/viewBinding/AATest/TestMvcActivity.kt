package com.android.frame.mvc.viewBinding.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.viewBinding.BaseActivity_VB
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2020/7/23
 * Desc:
 */
class TestMvcActivity : BaseActivity_VB<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "MVC框架", "天气信息", "网易新闻")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(WeatherActivity::class.java)
        }
        binding.btn2.setOnClickListener {
            startActivity(NewsListActivity::class.java)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}