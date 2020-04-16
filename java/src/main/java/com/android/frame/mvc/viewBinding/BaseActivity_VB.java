package com.android.frame.mvc.viewBinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.viewbinding.ViewBinding;

import com.android.base.BaseApplication;
import com.android.java.R;
import com.android.util.ExtraUtil;
import com.android.util.StatusBar.StatusBarUtil;
import com.android.util.ToastUtil;
import com.android.widget.TitleBar;
import com.google.gson.Gson;

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Activity(MVC结合ViewBinding)
 */
public abstract class BaseActivity_VB<VB extends ViewBinding> extends AppCompatActivity {

    protected Gson gson = new Gson();
    protected VB binding;

    //标题栏，需在布局文件中固定id名为title_bar
    protected TitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        BaseApplication.getInstance().addActivity(this);
        initBar();
        handleView(savedInstanceState);
        initListener();
        ExtraUtil.getTopActivityName(this);
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

    //获取ViewBinding，其实可以通过反射获取，只是运行时反射影响性能
    public abstract VB getViewBinding();

    //通过反射获取ViewBinding
//    private VB getViewBinding() {
//        Type type = getClass().getGenericSuperclass();
//        if (type instanceof ParameterizedType) {
//            try {
//                Class clazz = (Class<VB>) ((ParameterizedType) type).getActualTypeArguments()[0];
//                Method method = clazz.getMethod("inflate", LayoutInflater.class);
//                return (VB) method.invoke(null, getLayoutInflater());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(this);
        //监控内存泄漏
        BaseApplication.getRefWatcher().watch(this);
    }

    //显示Toast
    public void showToast(CharSequence text) {
        showToast(text, true, false);
    }

    //显示Toast
    public void showToast(CharSequence text, boolean isCenter, boolean longToast) {
        runOnUiThread(() -> {
            ToastUtil.showToast(text, getApplicationContext(), isCenter, longToast);
        });
    }

    //启动指定的Activity
    protected void startActivity(Class clazz) {
        startActivity(clazz, null);
    }

    //携带数据启动指定的Activity
    protected void startActivity(Class clazz, Bundle extras) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(this, clazz);
        startActivity(intent);
    }

    //启动指定的Activity并接收返回的结果
    protected void startActivityForResult(Class clazz, int requestCode) {
        startActivityForResult(clazz, null, requestCode);
    }

    //携带数据启动指定的Activity并接受返回的结果
    protected void startActivityForResult(Class clazz, Bundle extras, int requestCode) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    //跳转到登录界面
    protected void gotoLogin() {
        BaseApplication.getInstance().finishAllActivities();
        Intent intent = new Intent();
        intent.setAction("登录页的action");
        startActivity(intent);
    }

}
