package com.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentUtilBinding
import com.android.frame.mvc.BaseFragment
import com.android.util.TestUtilActivity
import com.android.util.code.TestCodeUtilActivity
import com.android.util.glide.TestGlideActivity
import com.android.util.jumpToTestUtilActivity

/**
 * Created by xuzhb on 2019/9/7
 * Desc:工具篇
 */
class UtilFragment : BaseFragment<FragmentUtilBinding>() {

    companion object {
        fun newInstance() = UtilFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {
        //实现沉浸式状态栏
        binding.statusbarTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_STATUS_BAR)
        }
        //测试时间
        binding.timeTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_DATE)
        }
        //测试键盘
        binding.keyboardTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_KEYBOARD)
        }
        //代码创建Drawable
        binding.drawableTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_DRAWABLE)
        }
        //SharePreferences工具类
        binding.sputilTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_SPUTIL)
        }
        //字符串工具类
        binding.stringTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_STRING)
        }
        //二维码/条形码工具
        binding.codeTv.setOnClickListener {
            startActivity(TestCodeUtilActivity::class.java)
        }
        //通知管理
        binding.notificationTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_NOTIFICATION)
        }
        //连续点击事件监听
        binding.continuousClickTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_CONTINUOUS_CLICK)
        }
        //拼音工具
        binding.pinyinTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_PINYIN)
        }
        //布局参数工具
        binding.layoutParamsTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_LAYOUT_PARAMS)
        }
        //正则表达式工具
        binding.regexTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_REGEX)
        }
        //磁盘缓存工具
        binding.cacheTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_CACHE)
        }
        //Glide工具
        binding.glideTv.setOnClickListener {
            startActivity(TestGlideActivity::class.java)
        }
        //Activity工具
        binding.activityTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_ACTIVITY)
        }
        //APP工具
        binding.appTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_APP)
        }
        //设备工具
        binding.deviceTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_DEVICE)
        }
        //Shell工具
        binding.shellTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_SHELL)
        }
        //底部选择器工具
        binding.pickerTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_PICKER_VIEW)
        }
        //崩溃异常监听
        binding.crashTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_CRASH)
        }
        //应用文件清除工具
        binding.cleanTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_CLEAN)
        }
        //SD卡工具
        binding.sdcardTv.setOnClickListener {
            jumpToTestUtilActivity(activity!!, TestUtilActivity.TEST_SDCARD)
        }
    }

    override fun initListener() {
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUtilBinding.inflate(inflater, container, false)

}