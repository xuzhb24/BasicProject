package com.android.frame.mvvm.AATest;

import android.os.Bundle;

import com.android.frame.mvvm.AATest.listType.TestMvvmListFragment;
import com.android.frame.mvvm.AATest.notListType.TestMvvmFragment;
import com.android.frame.mvvm.CommonBaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestFragmentBinding;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestFragmentMvvm extends CommonBaseActivity<ActivityTestFragmentBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        switch (getIntent().getIntExtra("fragment_type", 1)) {
            case 1:
                mTitleBar.setTitleText("基类Fragment(MVVM)");
                getSupportFragmentManager().beginTransaction().add(R.id.content_fl, TestMvvmFragment.newInstance()).commit();
                break;
            case 2:
                mTitleBar.setTitleText("列表Fragment(MVVM)");
                getSupportFragmentManager().beginTransaction().add(R.id.content_fl, TestMvvmListFragment.newInstance()).commit();
                break;
        }
    }

    @Override
    public void initListener() {

    }
}
