package com.android.util.activity

import android.os.Bundle
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2020/11/17
 * Desc:
 */
class TestJumpActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        if (bundle != null) {
            initCommonLayout(this, "TestJumpActivity", false, true)
            binding.tv.text = bundle.getString(EXTRA_DATA)
        } else {
            initCommonLayout(this, "TestJumpActivity", "返回")
        }
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}