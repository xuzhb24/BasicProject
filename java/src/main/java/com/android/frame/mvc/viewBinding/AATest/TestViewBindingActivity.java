package com.android.frame.mvc.viewBinding.AATest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.frame.mvc.viewBinding.BaseActivity_VB;
import com.android.java.databinding.ActivityTestViewbindingBinding;
import com.android.util.ExtraUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/4/14
 * Desc:
 */
public class TestViewBindingActivity extends BaseActivity_VB<ActivityTestViewbindingBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        List<String> titeList = new ArrayList<>();
        titeList.add("页面一");
        titeList.add("页面二");
        titeList.add("页面三");
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TestViewBindingFragment.newInstance("我是页面一"));
        fragmentList.add(TestViewBindingFragment.newInstance("我是页面二"));
        fragmentList.add(TestViewBindingFragment.newInstance("我是页面三"));
        binding.viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return titeList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titeList.get(position);
            }
        });
        binding.viewPager.setOffscreenPageLimit(2);  //不加这个的话切换Fragment时都会重新创建初始化，即从最初的onAttach()开始
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public void initListener() {
        binding.titleBar.setOnRightClickListener(v -> {
            ExtraUtil.alert(this, "测试视图绑定");
        });
    }

    @Override
    public ActivityTestViewbindingBinding getViewBinding() {
        return ActivityTestViewbindingBinding.inflate(getLayoutInflater());
    }
}
