package com.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentFrameBinding
import com.android.frame.TestLeakActivity
import com.android.frame.camera.zxing.AATest.TestZXingActivity
import com.android.frame.guide.GuideActivity
import com.android.frame.http.AATest.TestRetrofitActivity
import com.android.frame.mvc.BaseFragment
import com.android.frame.mvc.viewBinding.AATest.TestMvcActivity
import com.android.frame.mvp.AATest.activity.TestMvpActivity
import com.android.frame.permission.PermissionFrameActivity
import com.android.frame.permission.PermissionRequestActivity

/**
 * Created by xuzhb on 2019/9/7
 * Desc:框架篇
 */
class FrameFragment : BaseFragment<FragmentFrameBinding>() {

    companion object {
        fun newInstance() = FrameFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        //动态权限申请
        //原生API实现
        binding.orignalPermissionTv.setOnClickListener {
            startActivity(PermissionRequestActivity::class.java)
        }
        //第三方框架EasyPermission实现
        binding.easyPermissionTv.setOnClickListener {
            startActivity(PermissionFrameActivity::class.java)
        }
        //测试内存泄漏
        binding.leakTv.setOnClickListener {
            startActivity(TestLeakActivity::class.java)
        }
        //测试Retrofit
        binding.retrofitTv.setOnClickListener {
            startActivity(TestRetrofitActivity::class.java)
        }
        //MVP框架
        binding.mvpTv.setOnClickListener {
            startActivity(TestMvpActivity::class.java)
        }
        //MVC框架
        binding.mvcTv.setOnClickListener {
            startActivity(TestMvcActivity::class.java)
        }
        //zxing扫码
        binding.zxingTv.setOnClickListener {
            startActivity(TestZXingActivity::class.java)
        }
        //引导页
        binding.guideTv.setOnClickListener {
            startActivity(GuideActivity::class.java)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFrameBinding.inflate(inflater, container, false)

}