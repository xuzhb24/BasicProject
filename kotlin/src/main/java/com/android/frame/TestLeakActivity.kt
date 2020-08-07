package com.android.frame

import android.content.Context
import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2019/8/31
 * Desc:测试内存泄漏
 */
class TestLeakActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        var mContext: Context? = null
    }

    override fun handleView(savedInstanceState: Bundle?) {
        mContext = this  //测试内存泄漏
        initCommonLayout(this, "测试内存泄漏")
    }

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}