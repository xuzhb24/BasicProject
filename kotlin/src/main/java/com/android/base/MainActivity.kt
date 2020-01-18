package com.android.base

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mImageButton = intArrayOf(
        R.drawable.selector_bottom_frame,
        R.drawable.selector_bottom_widget,
        R.drawable.selector_bottom_util
    )
    private val mFragmentList: MutableList<Fragment> = mutableListOf(
        FrameFragment.newInstance(),
        WidgetFragment.newInstance(),
        UtilFragment.newInstance()
    )
    private val mTitleList: MutableList<String> = mutableListOf("框架", "控件", "工具")

    override fun handleView(savedInstanceState: Bundle?) {
        initBottomNavigationBar()
    }

    //初始化底部导航栏
    private fun initBottomNavigationBar() {
//        tab_layout.setTabMode(TabLayout.MODE_FIXED)//不可以轮动
        for (i in mTitleList.indices) {
            tab_layout.addTab(tab_layout.newTab().setText(mTitleList.get(i)))
        }
        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(i: Int): Fragment = mFragmentList.get(i)

            override fun getCount(): Int = mTitleList.size

            override fun getPageTitle(i: Int): CharSequence? = mTitleList.get(i)

        }
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 3
        tab_layout.setupWithViewPager(view_pager)
        for (i in mFragmentList.indices) {
            val tab = tab_layout.getTabAt(i)
            //添加自定义布局
            tab!!.setCustomView(getTabView(i))
            //默认选中第一个导航栏
            if (i == 0) {
                (tab.getCustomView()!!.findViewById(R.id.iv) as ImageView).isSelected = true
                (tab.getCustomView()!!.findViewById(R.id.tv) as TextView).isSelected = true
            }
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            //选中之后再次点击Tab时触发
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            //切换Tab时触发，返回切换前的Tab
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            //选择某一个Tab时触发，返回切换后的Tab
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //解决ViewPager + Fragment，点击tab切换时造成的闪屏问题
                tab?.let {
                    // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
                    view_pager.setCurrentItem(it.position, false)
                }
            }

        })
    }

    fun getTabView(position: Int): View {
        val view = LayoutInflater.from(applicationContext).inflate(R.layout.layout_tab_content, null)
        val iv = view.findViewById(R.id.iv) as ImageView
        val tv = view.findViewById(R.id.tv) as TextView
        iv.setImageResource(mImageButton[position])
        tv.setText(mTitleList[position])
        return view
    }

    override fun initBar() {

    }

    override fun initListener() {
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    //再按一次退出程序
    private var mLastPressTime: Long = 0

    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - mLastPressTime > 2 * 1000) {
            showToast("再按一次退出程序", false)
        } else {
            super.onBackPressed()
        }
        mLastPressTime = currentTimeMillis
    }


}
