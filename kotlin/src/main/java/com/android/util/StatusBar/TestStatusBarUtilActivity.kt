package com.android.util.StatusBar

import android.os.Bundle
import android.view.View
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2020/1/8
 * Desc:实现沉浸式状态栏
 */
class TestStatusBarUtilActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        const val EXTRA_TYPE = "EXTRA_TYPE"
        const val EXTRA_TEXT = "EXTRA_TEXT"
    }

    override fun initBar() {
        when (intent.getIntExtra(EXTRA_TYPE, 1)) {
            1 -> {
                root_ll.setBackgroundResource(R.drawable.ic_status_bar)
                title_bar.visibility = View.GONE
                StatusBarUtil.darkMode(this, dark = false)
            }
            2 -> {
                StatusBarUtil.darkModeAndPadding(
                    this, title_bar,
                    resources.getColor(R.color.white), 1f, true
                )
            }
            3 -> {
                StatusBarUtil.darkModeAndPadding(
                    this, title_bar,
                    resources.getColor(R.color.black), 1f, false
                )
            }
            4 -> {
                StatusBarUtil.darkModeAndPadding(
                    this, title_bar,
                    resources.getColor(R.color.black), 0.5f, false
                )
            }
            5 -> {
                root_ll.setBackgroundResource(R.drawable.ic_status_bar)
                title_bar.visibility = View.GONE
                StatusBarUtil.hideNavigationBar(this)
            }
            6 -> {
                root_ll.setBackgroundResource(R.drawable.ic_status_bar)
                title_bar.visibility = View.GONE
                StatusBarUtil.setNavigationBarStatusBarTranslucent(this)
            }
        }
    }

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "标题", showTextView = true)
        tv.text = intent.getStringExtra(EXTRA_TEXT)
    }

    override fun initListener() {
    }

}