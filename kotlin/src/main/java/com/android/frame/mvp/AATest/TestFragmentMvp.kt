package com.android.frame.mvp.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestFragmentBinding
import com.android.frame.mvp.AATest.listType.TestMvpListFragment
import com.android.frame.mvp.AATest.notListType.TestMvpFragment
import com.android.frame.mvp.CommonBaseActivity

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestFragmentMvp : CommonBaseActivity<ActivityTestFragmentBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getIntExtra("fragment_type", 1)) {
            1 -> {
                mTitleBar?.titleText = "基类Fragment(MVP)"
                supportFragmentManager.beginTransaction().add(R.id.content_fl, TestMvpFragment.newInstance()).commit()
            }
            2 -> {
                mTitleBar?.titleText = "列表Fragment(MVP)"
                supportFragmentManager.beginTransaction().add(R.id.content_fl, TestMvpListFragment.newInstance()).commit()
            }
        }
    }

    override fun initListener() {
    }

}