package com.android.frame.mvvm.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestFragmentBinding
import com.android.frame.mvvm.AATest.listType.TestMvvmListFragment
import com.android.frame.mvvm.AATest.notListType.TestMvvmFragment
import com.android.frame.mvvm.CommonBaseActivity

/**
 * Created by xuzhb on 2021/8/6
 * Desc:
 */
class TestFragmentMvvm : CommonBaseActivity<ActivityTestFragmentBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getIntExtra("fragment_type", 1)) {
            1 -> {
                mTitleBar?.titleText = "基类Fragment(MVVM)"
                supportFragmentManager.beginTransaction().add(R.id.content_fl, TestMvvmFragment.newInstance()).commit()
            }
            2 -> {
                mTitleBar?.titleText = "列表Fragment(MVVM)"
                supportFragmentManager.beginTransaction().add(R.id.content_fl, TestMvvmListFragment.newInstance()).commit()
            }
        }
    }

    override fun initListener() {
    }

}