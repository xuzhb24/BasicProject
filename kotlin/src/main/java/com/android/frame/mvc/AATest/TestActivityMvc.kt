package com.android.frame.mvc.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
class TestActivityMvc : BaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "MVC框架", "BaseActivity", "BaseListActivity", "BaseFragment", "BaseListFragment")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(TestMvcActivity::class.java)
        }
        binding.btn2.setOnClickListener {
            startActivity(TestMvcListActivity::class.java)
        }
        binding.btn3.setOnClickListener {
            startActivity(TestFragmentMvc::class.java, Bundle().apply { putInt("fragment_type", 1) })
        }
        binding.btn4.setOnClickListener {
            startActivity(TestFragmentMvc::class.java, Bundle().apply { putInt("fragment_type", 2) })
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}