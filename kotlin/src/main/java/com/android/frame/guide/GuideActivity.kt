package com.android.frame.guide

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.viewpager.widget.ViewPager
import com.android.base.MainActivity
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityGuideBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.android.util.StatusBar.StatusBarUtil

/**
 * Created by xuzhb on 2020/12/28
 * Desc:引导页
 */
class GuideActivity : BaseActivity<ActivityGuideBinding>() {

    private var mLayoutIds: IntArray = intArrayOf(
        R.layout.layout_guide_one,
        R.layout.layout_guide_two,
        R.layout.layout_guide_three
    )

    override fun initBar() {
        StatusBarUtil.darkMode(this)
    }

    override fun handleView(savedInstanceState: Bundle?) {
        val viewList: MutableList<View> = mutableListOf()
        for (i in mLayoutIds.indices) {
            val radioButton = RadioButton(this)
            radioButton.setButtonDrawable(R.drawable.selector_guide_indicator)
            val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val margin = SizeUtil.dp2pxInt(6f)
            params.leftMargin = margin
            params.rightMargin = margin
            radioButton.layoutParams = params
            radioButton.tag = i
            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.guideVp.setCurrentItem(buttonView.tag.toString().toInt(), false)
                }
            }
            binding.guideRg.addView(radioButton, params)
            if (i == 0) {
                radioButton.isChecked = true
            }
            val view = layoutInflater.inflate(mLayoutIds[i], null)
            viewList.add(view)
            binding.guideVp.adapter = SimpleViewAdapter(viewList)
            binding.guideVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    val radioButton: RadioButton = binding.guideRg.findViewWithTag(position)
                    if (!radioButton.isChecked) {
                        radioButton.isChecked = true
                    }
                    binding.useBtn.visibility = if (position == mLayoutIds.size - 1) View.VISIBLE else View.INVISIBLE
                }

            })
        }
    }

    override fun initListener() {
        binding.useBtn.setOnClickListener {
            startActivity(MainActivity::class.java)
            finish()
        }
    }

    override fun getViewBinding() = ActivityGuideBinding.inflate(layoutInflater)

}