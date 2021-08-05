package com.android.frame.mvc.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestFragmentBinding
import com.android.frame.mvc.BaseActivity

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
class TestFragmentMvc : BaseActivity<ActivityTestFragmentBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getIntExtra("fragment_type", 1)) {
            1 -> {
                mTitleBar?.titleText = "基类Fragment(MVC)"
                supportFragmentManager.beginTransaction().add(R.id.content_fl, TestMvcFragment.newInstance()).commit()
            }
            2 -> {
                mTitleBar?.titleText = "列表Fragment(MVC)"
                supportFragmentManager.beginTransaction().add(R.id.content_fl, TestMvcListFragment.newInstance()).commit()
            }
        }
    }

    override fun initListener() {
    }

}