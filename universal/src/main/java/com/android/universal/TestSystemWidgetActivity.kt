package com.android.universal

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_test_system_widget.*
import kotlinx.android.synthetic.main.layout_title_bar.*

/**
 * Created by xuzhb on 2020/2/16
 * Desc:系统自带控件的使用
 */
class TestSystemWidgetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_system_widget)
        initTitleBar()
        initView()
        initListener()
    }

    private fun initTitleBar() {
        title_fl.setBackgroundColor(Color.parseColor("#db4b3c"))
        val dp20 = (20 * resources.displayMetrics.density + 0.5f).toInt()
        title_fl.setPadding(dp20, 0, dp20, 0)
        title_tv.text = "系统控件"
        title_tv.setTextColor(Color.WHITE)
        right_tv.text = "说明"
        right_tv.setTextColor(Color.WHITE)
        divider_line.visibility = View.GONE
        left_fl.setOnClickListener {
            finish()
        }
        right_tv.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("本页面主要记录一些系统自带控件的属性和使用方法")
                .show()
        }
    }

    private fun initView() {
        val titleList = mutableListOf<String>(
            "页面一", "页面二", "页面三", "页面四", "页面五", "页面六", "页面七", "页面八"
        )
        val fragmentList = mutableListOf<Fragment>(
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget1),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget2),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget3),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget4),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget5),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget6),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget7),
            TestSystemWidgetFragment.newInstance(R.layout.fragment_test_system_widget8)
        )
        view_pager.offscreenPageLimit = fragmentList.size
        view_pager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {

            override fun getItem(i: Int): Fragment = fragmentList.get(i)

            override fun getCount(): Int = titleList.size

            override fun getPageTitle(i: Int): CharSequence? = titleList.get(i)

        }
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun initListener() {

    }

}