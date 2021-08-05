package com.android.widget.FloatWindow.NeedPermission.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout
import com.android.widget.FloatWindow.NeedPermission.FloatWindow

/**
 * Created by xuzhb on 2021/3/30
 * Desc:
 */
class TestFloatPageThreeActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "悬浮窗页面三", false, true,
            "getX&getY", "show", "hide", "updateXY", "updateX", "updateY"
        )
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            val position = "${FloatWindow.get()?.getX()}x${FloatWindow.get()?.getY()}"
            binding.tv.text = position
        }
        binding.btn2.setOnClickListener {
            FloatWindow.get()?.show()
        }
        binding.btn3.setOnClickListener {
            FloatWindow.get()?.hide()
        }
        binding.btn4.setOnClickListener {
            FloatWindow.get()?.updateXY(0, 0)
        }
        binding.btn5.setOnClickListener {
            FloatWindow.get()?.updateX(0)
        }
        binding.btn6.setOnClickListener {
            FloatWindow.get()?.updateY(0)
        }
    }

}