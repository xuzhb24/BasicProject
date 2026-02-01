package com.android.util.magicIndicator.test

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.android.frame.mvc.BaseActivity
import com.android.kotlin.databinding.ActivityTestMagicindicatorBinding
import com.android.universal.R
import com.android.util.dp2px
import com.android.util.magicIndicator.MagicIndicatorUtil
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

/**
 * Created by xuzhb on 2026/1/31
 * Desc:
 */
class TestMagicIndicatorActivity : BaseActivity<ActivityTestMagicindicatorBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initIndicator1()
        initIndicator2()
        initIndicator3()
        initIndicator4()
        initIndicator5()
        initIndicator6()
        initIndicator7()
    }

    override fun initListener() {
    }

    private fun initIndicator1() {
        val list = mutableListOf("指示器1", "指示器2", "指示器3")
        setText(binding.tv1, "单MagicIndicator切换")
        MagicIndicatorUtil.bindScaleTitleView(this, binding.magicIndicator1, list, { index ->
            setText(binding.tv1, "单MagicIndicator切换 ${list[index]}")
        })
    }

    private fun initIndicator2() {
        val list = mutableListOf("指示器1", "指示器2", "指示器3", "指示器4", "指示器5", "指示器6", "指示器7", "指示器8", "指示器9")
        MagicIndicatorUtil.bindScaleTitleView(
            this, binding.magicIndicator2, list, { index -> showToast(list[index]) },
            1f, false, "#999999".toColorInt(), "#353B50".toColorInt(),
            18f, 10f, 10f, 50f, MagicIndicatorUtil.createIndicator(this)
        )
    }

    private fun initIndicator3() {
        val list = mutableListOf("指示器指示器1", "指示器2", "指示器3")
        binding.viewPager3.adapter = SimpleFragmentPagerAdapter(supportFragmentManager, list)
        MagicIndicatorUtil.bindScaleTitleView(this, binding.viewPager3, binding.magicIndicator3, list)
    }

    private fun initIndicator4() {
        val list = mutableListOf("指示器指示器1", "指示器2", "指示器3")
        val textSize = 16f
        val padding = MagicIndicatorUtil.calculateItemPaddingDp(list, 3, textSize, 15f)
        binding.viewPager4.adapter = SimpleFragmentPagerAdapter(supportFragmentManager, list)
        MagicIndicatorUtil.bindScaleTitleView(
            this, binding.viewPager4, binding.magicIndicator4, list, 1f,
            false, Color.GRAY, Color.BLUE, textSize,
            padding, padding, padding,
            MagicIndicatorUtil.createIndicator(this, color = Color.BLUE)
        )
    }

    private fun initIndicator5() {
        val list = mutableListOf("指示器1", "指示器2", "指示器3", "指示器4", "指示器5", "指示器6", "指示器7", "指示器8", "指示器9")
        MagicIndicatorUtil.bindCommonTitleView(
            this, binding.magicIndicator5, R.layout.layout_indicator_tab_content, { index ->
                showToast(list[index])
            }, list, mutableListOf(), R.drawable.shape_gradient_tab_bg,
            true, 14f, 16f, 0f, 20f
        )
    }

    private fun initIndicator6() {
        val titleList = mutableListOf("框架", "控件", "工具")
        val imageList = mutableListOf(
            R.drawable.selector_bottom_frame,
            R.drawable.selector_bottom_widget,
            R.drawable.selector_bottom_util
        )
        binding.viewPager6.adapter = SimpleFragmentPagerAdapter(supportFragmentManager, titleList)
        MagicIndicatorUtil.bindCommonTitleView(
            this, binding.viewPager6, binding.magicIndicator6, R.layout.layout_tab_content,
            titleList, imageList, null, null, 12f, 12f
        )
    }

    private var mDataList: MutableList<String> = mutableListOf()
    private var mFragmentPagerAdapter: SimpleFragmentPagerAdapter? = null
    private var mNavigatorAdapter: CommonNavigatorAdapter? = null

    private fun initIndicator7() {
        mDataList.clear()
        mDataList.addAll(createDataList())
        mFragmentPagerAdapter = SimpleFragmentPagerAdapter(supportFragmentManager, mDataList)
        mNavigatorAdapter = MagicIndicatorUtil.createNavigatorAdapter(mDataList, { index ->
            MagicIndicatorUtil.createCommonTitleView(this, index, R.layout.layout_indicator_tab_content, mDataList, R.drawable.shape_gradient_tab_bg, {
                binding.viewPager7.setCurrentItem(index, false)
            }, null, 12f, 12f)
        })
        binding.viewPager7.adapter = mFragmentPagerAdapter
        binding.viewPager7.offscreenPageLimit = mDataList.size
        binding.magicIndicator7.navigator = CommonNavigator(this).apply {
            leftPadding = 6f.dp2px().toInt()
            rightPadding = 6f.dp2px().toInt()
            adapter = mNavigatorAdapter
        }
        ViewPagerHelper.bind(binding.magicIndicator7, binding.viewPager7)
        binding.btn7.setOnClickListener {
            notifyIndicator7DataChange()
        }
    }

    private fun notifyIndicator7DataChange() {
        mDataList.clear()
        mDataList.addAll(createDataList())
        mFragmentPagerAdapter?.notifyDataSetChanged()
        mNavigatorAdapter?.notifyDataSetChanged()
    }

    private fun createDataList(): MutableList<String> {
        val title = mutableListOf("指示器", "页面", "tab").random()
        val list: MutableList<String> = mutableListOf()
        for (i in 1..((3..9).random())) {
            list.add("${title}$i")
        }
        return list
    }

    private fun setText(tv: TextView, text: String) {
        tv.text = text
    }

    private inner class SimpleFragmentPagerAdapter(
        fm: FragmentManager,
        private val list: MutableList<String>
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = TestMagicIndicatorFragment.newInstance("页面 ${list[position]}")

        override fun getCount() = list.size

        override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE
    }

}