package com.android.frame.mvp.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvp.AATest.listType.TestMvpListActivity
import com.android.frame.mvp.AATest.notListType.TestMvpActivity
import com.android.frame.mvp.CommonBaseActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestActivityMvp : CommonBaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "MVP框架", "BaseActivity", "BaseListActivity", "BaseFragment", "BaseListFragment")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(TestMvpActivity::class.java)
        }
        binding.btn2.setOnClickListener {
            startActivity(TestMvpListActivity::class.java)
        }
        binding.btn3.setOnClickListener {
            startActivity(TestFragmentMvp::class.java, Bundle().apply { putInt("fragment_type", 1) })
        }
        binding.btn4.setOnClickListener {
            startActivity(TestFragmentMvp::class.java, Bundle().apply { putInt("fragment_type", 2) })
        }
    }

}