package com.android.widget.FloatWindow

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout
import com.android.widget.FloatWindow.NeedPermission.AATest.TestFloatPageOneActivity
import com.android.widget.FloatWindow.NoPermission.AATest.TestFloatPageActivity

/**
 * Created by xuzhb on 2021/3/25
 * Desc:
 */
class TestFloatActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "悬浮窗", "申请权限", "无需权限")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(TestFloatPageOneActivity::class.java)
        }
        binding.btn2.setOnClickListener {
            TestFloatPageActivity.start(this, 1)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}