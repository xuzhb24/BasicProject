package com.android.frame.mvc.viewBinding.AATest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.android.basicproject.databinding.ActivityTestViewbindingBinding
import com.android.frame.mvc.viewBinding.BaseActivity_VB
import com.android.util.alert

/**
 * Created by xuzhb on 2020/4/15
 * Desc:
 */
class TestViewBindingActivity : BaseActivity_VB<ActivityTestViewbindingBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        val titleList = mutableListOf("页面一", "页面二", "页面三")
        val fragmentList: MutableList<Fragment> = mutableListOf(
            TestViewBindingFragment.newInstance("我是页面一"),
            TestViewBindingFragment.newInstance("我是页面二"),
            TestViewBindingFragment.newInstance("我是页面三")
        )
        binding.viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {

            override fun getItem(p0: Int): Fragment = fragmentList.get(p0)

            override fun getCount(): Int = titleList.size

            override fun getPageTitle(position: Int): CharSequence? = titleList.get(position)

        }
        binding.viewPager.offscreenPageLimit = 2  //不加这个的话切换Fragment时都会重新创建初始化，即从最初的onAttach()开始
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun initListener() {
        binding.titleBar.setOnRightClickListener {
            alert(this, "测试视图绑定")
        }
    }

    override fun getViewBinding(): ActivityTestViewbindingBinding =
        ActivityTestViewbindingBinding.inflate(layoutInflater)
}