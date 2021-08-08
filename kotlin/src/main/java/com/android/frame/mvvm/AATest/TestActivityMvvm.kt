package com.android.frame.mvvm.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvvm.AATest.listType.TestMvvmListActivity
import com.android.frame.mvvm.AATest.notListType.TestMvvmActivity
import com.android.frame.mvvm.CommonBaseActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2021/8/6
 * Desc:
 */
class TestActivityMvvm : CommonBaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "MVVM框架", "BaseActivity", "BaseListActivity", "BaseFragment", "BaseListFragment")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(TestMvvmActivity::class.java)
        }
        binding.btn2.setOnClickListener {
            startActivity(TestMvvmListActivity::class.java)
        }
        binding.btn3.setOnClickListener {
            startActivity(TestFragmentMvvm::class.java, Bundle().apply { putInt("fragment_type", 1) })
        }
        binding.btn4.setOnClickListener {
            startActivity(TestFragmentMvvm::class.java, Bundle().apply { putInt("fragment_type", 2) })
        }
    }
}