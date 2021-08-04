package com.android.frame.mvp.AATest;

import android.os.Bundle;

import com.android.frame.mvp.AATest.listType.TestMvpListActivity;
import com.android.frame.mvp.AATest.notListType.TestMvpActivity;
import com.android.frame.mvp.CommonBaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestActivityMvp extends CommonBaseActivity<ActivityCommonLayoutBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "MVP框架", "BaseActivity", "BaseListActivity", "BaseFragment", "BaseListFragment");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            startActivity(TestMvpActivity.class);
        });
        binding.btn2.setOnClickListener(v -> {
            startActivity(TestMvpListActivity.class);
        });
        binding.btn3.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("fragment_type", 1);
            startActivity(TestFragmentMvp.class, bundle);
        });
        binding.btn4.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("fragment_type", 2);
            startActivity(TestFragmentMvp.class, bundle);
        });
    }

}
