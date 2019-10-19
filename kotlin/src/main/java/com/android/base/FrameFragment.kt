package com.android.base

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.TestLeakActivity
import com.android.frame.http.AATest.TestRetrofitActivity
import com.android.frame.mvc.BaseFragment
import com.android.frame.permission.PermissionFrameActivity
import com.android.frame.permission.PermissionRequestActivity
import kotlinx.android.synthetic.main.fragment_frame.*

/**
 * Created by xuzhb on 2019/9/7
 * Desc:框架篇
 */
class FrameFragment : BaseFragment() {

    companion object {
        fun newInstance() = FrameFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        //动态权限申请
        //原生API实现
        orignal_permission_tv.setOnClickListener {
            startActivity(PermissionRequestActivity::class.java)
        }
        //第三方框架EasyPermission实现
        easy_permission_tv.setOnClickListener {
            startActivity(PermissionFrameActivity::class.java)
        }
        //测试内存泄漏
        leak_tv.setOnClickListener {
            startActivity(TestLeakActivity::class.java)
        }
        //测试Retrofit
        retrofit_tv.setOnClickListener {
            startActivity(TestRetrofitActivity::class.java)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_frame

}