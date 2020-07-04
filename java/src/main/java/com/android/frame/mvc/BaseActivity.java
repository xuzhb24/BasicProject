package com.android.frame.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import butterknife.ButterKnife;

import com.android.base.BaseApplication;
import com.android.java.R;
import com.android.util.ExtraUtil;
import com.android.util.StatusBar.StatusBarUtil;
import com.android.util.ToastUtil;
import com.android.widget.TitleBar;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:基类Activity(MVC)
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);  //引入ButterKnife
        initBar();
        handleView(savedInstanceState);
        initListener();
        ExtraUtil.getTopActivityName(this);
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected void initBar() {
        TitleBar titleBar = findViewById(R.id.title_bar);
        if (titleBar != null) {
            StatusBarUtil.darkModeAndPadding(this, titleBar);
        } else {
            ViewGroup content = findViewById(android.R.id.content);
            StatusBarUtil.darkModeAndPadding(this, content);
        }
    }

    //执行onCreate接下来的逻辑
    public abstract void handleView(Bundle savedInstanceState);

    //所有的事件回调均放在该层，如onClickListener等
    public abstract void initListener();

    //获取布局
    public abstract int getLayoutId();

    public void startActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }

    public void showToast(String text) {
        runOnUiThread(() -> {
            ToastUtil.showToast(text);
        });
    }

    public void showToast(String text, boolean longToast, boolean isCenter) {
        runOnUiThread(() -> {
            ToastUtil.showToast(text, BaseApplication.getInstance(), longToast, isCenter);
        });
    }

}
