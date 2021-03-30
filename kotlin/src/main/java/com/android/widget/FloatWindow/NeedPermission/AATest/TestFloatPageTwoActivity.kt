package com.android.widget.FloatWindow.NeedPermission.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2021/3/30
 * Desc:
 */
class TestFloatPageTwoActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "悬浮窗页面二", false, true, "跳转到悬浮窗页面三")
        binding.tv.text = "悬浮窗A不在页面二显示"
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(TestFloatPageThreeActivity::class.java)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}