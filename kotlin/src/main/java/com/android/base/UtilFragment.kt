package com.android.base

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseFragment
import com.android.util.TestUtilActivity
import com.android.util.code.TestCodeUtilActivity
import com.android.util.jumpToTestUtilActivity
import kotlinx.android.synthetic.main.fragment_util.*

/**
 * Created by xuzhb on 2019/9/7
 * Desc:工具篇
 */
class UtilFragment : BaseFragment() {

    companion object {
        fun newInstance() = UtilFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {
        //实现沉浸式状态栏
        statusbar_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_STATUS_BAR)
        }
        //测试时间
        time_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_DATE)
        }
        //测试键盘
        keyboard_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_KEYBOARD)
        }
        //代码创建Drawable
        drawable_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_DRAWABLE)
        }
        //SharePreferences工具类
        sputil_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_SPUTIL)
        }
        //字符串工具类
        string_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_STRING)
        }
        //二维码/条形码工具
        code_tv.setOnClickListener {
            startActivity(TestCodeUtilActivity::class.java)
        }
        //通知管理
        notification_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_NOTIFICATION)
        }
        //连续点击事件监听
        continuous_click_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_CONTINUOUS_CLICK)
        }
        //拼音工具
        pinyin_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_PINYIN)
        }
        //布局参数工具
        layout_params_tv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_LAYOUT_PARAMS)
        }
    }

    override fun initListener() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_util

}