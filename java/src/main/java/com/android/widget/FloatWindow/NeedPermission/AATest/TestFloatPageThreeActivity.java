package com.android.widget.FloatWindow.NeedPermission.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.widget.FloatWindow.NeedPermission.FloatWindow;

/**
 * Created by xuzhb on 2021/3/10
 * Desc:
 */
public class TestFloatPageThreeActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "悬浮窗页面三", false, true,
                "getX&getY", "show", "hide", "updateXY", "updateX", "updateY");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            String position = FloatWindow.get().getX() + "x" + FloatWindow.get().getY();
            binding.tv.setText(position);
        });
        binding.btn2.setOnClickListener(v -> {
            FloatWindow.get().show();
        });
        binding.btn3.setOnClickListener(v -> {
            FloatWindow.get().hide();
        });
        binding.btn4.setOnClickListener(v -> {
            FloatWindow.get().updateXY(0, 0);
        });
        binding.btn5.setOnClickListener(v -> {
            FloatWindow.get().updateX(0);
        });
        binding.btn6.setOnClickListener(v -> {
            FloatWindow.get().updateY(0);
        });
    }

}