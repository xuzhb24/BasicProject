package com.android.frame.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

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
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB binding;

    //标题栏，需在布局文件中固定id名为title_bar
    protected TitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        initBaseView();
        initBar();
        handleView(savedInstanceState);
        initListener();
    }

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected void initBaseView() {
        mTitleBar = findViewById(R.id.title_bar);
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected void initBar() {
        mTitleBar = findViewById(R.id.title_bar);
        if (mTitleBar != null) {
            StatusBarUtil.darkModeAndPadding(this, mTitleBar);
            if (isBarBack()) {
                mTitleBar.setOnLeftClickListener(v -> finish());
            }
        } else {
            ViewGroup content = findViewById(android.R.id.content);
            StatusBarUtil.darkModeAndPadding(this, content);
        }
    }

    //点击标题栏左侧图标是否退出Activity，默认true
    protected boolean isBarBack() {
        return true;
    }

    //执行onCreate接下来的逻辑
    public abstract void handleView(Bundle savedInstanceState);

    //所有的事件回调均放在该层，如onClickListener等
    public abstract void initListener();

    //获取ViewBinding
    public abstract VB getViewBinding();

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //屏幕顶部中间区域双击获取当前Activity类名，只在debug环境下有效
        ExtraUtil.parseActivity(this, ev);
        return super.dispatchTouchEvent(ev);
    }
}
