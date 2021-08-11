package com.android.frame.mvvm.AATest;

import android.os.Bundle;

import com.android.frame.mvvm.AATest.listType.TestMvvmListActivity;
import com.android.frame.mvvm.AATest.notListType.TestMvvmActivity;
import com.android.frame.mvvm.CommonBaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestActivityMvvm extends CommonBaseActivity<ActivityCommonLayoutBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "MVVM框架", "BaseActivity", "BaseListActivity", "BaseFragment", "BaseListFragment");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            startActivity(TestMvvmActivity.class);
        });
        binding.btn2.setOnClickListener(v -> {
            startActivity(TestMvvmListActivity.class);
        });
        binding.btn3.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("fragment_type", 1);
            startActivity(TestFragmentMvvm.class, bundle);
        });
        binding.btn4.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("fragment_type", 2);
            startActivity(TestFragmentMvvm.class, bundle);
        });
    }
}
