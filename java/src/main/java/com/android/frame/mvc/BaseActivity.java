package com.android.frame.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.android.application.BaseApplication;
import com.android.util.ToastUtil;

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
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected void initBar() {

    }

    //执行onCreate接下来的逻辑
    public abstract void handleView(Bundle savedInstanceState);

    //所有的事件回调均放在该层，如onClickListener等
    public abstract void initListener();

    //获取布局
    public abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //监控内存泄漏
        BaseApplication.Companion.getRefWatcher().watch(this);
    }

    public void startActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }

    public void showToast(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(text);
            }
        });
    }

    public void showToast(String text, boolean longToast, boolean isCenter) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(text, BaseApplication.instance, longToast, isCenter);
            }
        });
    }

}
