package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.frame.TestLeakActivity;
import com.android.frame.guide.GuideActivity;
import com.android.frame.http.AATest.TestRetrofitActivity;
import com.android.frame.mvc.AATest.TestActivityMvc;
import com.android.frame.mvc.BaseFragment;
import com.android.frame.mvp.AATest.activity.TestMvpActivity;
import com.android.java.databinding.FragmentFrameBinding;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:框架篇
 */
public class FrameFragment extends BaseFragment<FragmentFrameBinding> {

    public static FrameFragment newInstance() {
        return new FrameFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        //测试内存泄漏
        binding.leakTv.setOnClickListener(v -> {
            startActivity(TestLeakActivity.class);
        });
        //动态权限申请
        //原生API实现
        binding.orignalPermissionTv.setOnClickListener(v -> {

        });
        //EasyPermission
        binding.easyPermissionTv.setOnClickListener(v -> {

        });
        //测试Retrofit
        binding.retrofitTv.setOnClickListener(v -> {
            startActivity(TestRetrofitActivity.class);
        });
        //MVC框架
        binding.mvcTv.setOnClickListener(v -> {
            startActivity(TestActivityMvc.class);
        });
        //MVP框架
        binding.mvpTv.setOnClickListener(v -> {
            startActivity(TestMvpActivity.class);
        });
        //引导页
        binding.guideTv.setOnClickListener(v -> {
            startActivity(GuideActivity.class);
        });
    }

    @Override
    public FragmentFrameBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFrameBinding.inflate(inflater, container, false);
    }

}
