package com.android.util.StatusBar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;


/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class TestStatusBarUtilActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_TEXT = "EXTRA_TEXT";

    @Override
    protected void initBar() {
        switch (getIntent().getIntExtra(EXTRA_TYPE, 1)) {
            case 1:
                binding.rootLl.setBackgroundResource(R.drawable.ic_status_bar);
                mTitleBar.setVisibility(View.GONE);
                StatusBarUtil.darkMode(this, Color.BLACK, 0, false);
                break;
            case 2:
                StatusBarUtil.darkModeAndPadding(this, mTitleBar,
                        getResources().getColor(R.color.white), 1f, true);
                break;
            case 3:
                StatusBarUtil.darkModeAndPadding(this, mTitleBar,
                        getResources().getColor(R.color.black), 1f, false);
                break;
            case 4:
                StatusBarUtil.darkModeAndPadding(this, mTitleBar,
                        getResources().getColor(R.color.black), 0.5f, false);
                break;
            case 5:
                binding.rootLl.setBackgroundResource(R.drawable.ic_status_bar);
                mTitleBar.setVisibility(View.GONE);
                StatusBarUtil.hideNavigationBar(this);
                break;
            case 6:
                binding.rootLl.setBackgroundResource(R.drawable.ic_status_bar);
                mTitleBar.setVisibility(View.GONE);
                StatusBarUtil.setNavigationBarStatusBarTranslucent(this);
                break;
        }
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "标题", false, true);
        binding.tv.setText(getIntent().getStringExtra(EXTRA_TEXT));
    }

    @Override
    public void initListener() {

    }

}
