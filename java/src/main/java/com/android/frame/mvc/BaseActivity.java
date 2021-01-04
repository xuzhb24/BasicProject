package com.android.frame.mvc;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.android.base.BaseApplication;
import com.android.java.R;
import com.android.util.CheckFastClickUtil;
import com.android.util.ExtraUtil;
import com.android.util.LogUtil;
import com.android.util.NetReceiver;
import com.android.util.NetworkUtil;
import com.android.util.StatusBar.StatusBarUtil;
import com.android.util.ToastUtil;
import com.android.widget.LoadingDialog.LoadingDialog;
import com.android.widget.LoadingLayout.LoadingLayout;
import com.android.widget.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:基类Activity(MVC)，和CustomObserver配合使用
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity implements IBaseView, OnRefreshListener {

    private static final String TAG = "BaseActivity";

    protected VB binding;

    //防止RxJava内存泄漏
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    //加载弹窗
    protected LoadingDialog mLoadingDialog;
    //通用加载状态布局，需在布局文件中固定id名为loading_layout
    protected LoadingLayout mLoadingLayout;
    //通用标题栏，需在布局文件中固定id名为title_bar
    protected TitleBar mTitleBar;
    //通用下拉刷新组件，需在布局文件中固定id名为smart_refresh_layout
    protected SmartRefreshLayout mSmartRefreshLayout;
    //通用的RecyclerView组件，需在布局文件中固定id名为R.id.recycler_view
    protected RecyclerView mRecyclerView;

    //通用网络异常的布局
    private FrameLayout mNetErrorFl;
    private NetReceiver mNetReceiver;

    protected boolean isRefreshing = false;          //是否正在下拉刷新
    protected boolean hasDataLoadedSuccess = false;  //是否成功加载过数据，设置这个变量的原因是加载状态布局一般只会在第一次加载时显示，当加载成功过一次就不再显示

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        BaseApplication.getInstance().addActivity(this);
        initBaseView();
        initBar();
        initNetReceiver();
        handleView(savedInstanceState);
        initListener();
        //加载数据
        refreshData();
    }

    //初始化一些通用控件，如加载框、SmartRefreshLayout、网络错误提示布局
    protected void initBaseView() {
        mTitleBar = findViewById(R.id.title_bar);
        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialogStyle);
        //获取布局中的加载状态布局
        mLoadingLayout = findViewById(R.id.loading_layout);
        if (mLoadingLayout != null) {
            mLoadingLayout.setOnFailListener(this::refreshData);
        }
        //获取布局中的SmartRefreshLayout组件，重用BaseActivity的下拉刷新逻辑
        //注意布局中SmartRefreshLayout的id命名为smart_refresh_layout，否则mSmartRefreshLayout为null
        //如果SmartRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_list" />
        mSmartRefreshLayout = findViewById(R.id.smart_refresh_layout);
        //如果当前布局文件不包含id为smart_refresh_layout的组件则不执行下面的逻辑
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
            mSmartRefreshLayout.setEnableLoadMore(false);
            mSmartRefreshLayout.setOnRefreshListener(this);
        }
        //获取布局中的RecyclerView组件，注意布局中RecyclerView的id命名为recycler_view，否则mRecyclerView为null
        mRecyclerView = findViewById(R.id.recycler_view);
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = findViewById(R.id.net_error_fl);
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected void initBar() {
        if (mTitleBar != null) {  //如果当前布局包含id为title_bar的标题栏控件，以该控件为基准实现沉浸式状态栏
            StatusBarUtil.darkModeAndPadding(this, mTitleBar);
            if (isBarBack()) {
                mTitleBar.setOnLeftIconClickListener(v -> finish());
            }
        } else {  //以ContentView为基准实现沉浸式状态栏，颜色是整个布局的背景色
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

    //加载数据，进入页面时默认就会进行加载，请务必重写refreshData，当加载失败点击重试或者下拉刷新时会调用这个方法
    protected void refreshData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetReceiver();
        //销毁加载框
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }

        //取消所有正在执行的订阅
        mCompositeDisposable.clear();

        BaseApplication.getInstance().removeActivity(this);
    }

    //显示加载弹窗
    @Override
    public void showLoadingDialog() {
        showLoadingDialog("加载中", true);
    }

    //显示加载弹窗
    @Override
    public void showLoadingDialog(String message, boolean cancelable) {
        runOnUiThread(() -> {
            if (mLoadingDialog != null) {
                mLoadingDialog.show(message, cancelable);
            }
        });
    }

    //显示加载状态布局
    @Override
    public void showLoadingLayout() {
        runOnUiThread(() -> {
            if (!isRefreshing && !hasDataLoadedSuccess && mLoadingLayout != null) {  //下拉刷新或者加载成功过都不显示加载状态
                mLoadingLayout.loadStart();
            }
        });
    }

    //数据加载完成
    @Override
    public void loadFinish(boolean isError) {
        runOnUiThread(() -> {
            if (!isError) {
                hasDataLoadedSuccess = true;
            }
            showNetErrorLayout();
            if (mLoadingLayout != null) {
                if (isError && !hasDataLoadedSuccess) {  //数据加载失败，且当前页面无数据
                    mLoadingLayout.loadFail();
                } else {
                    mLoadingLayout.loadComplete();
                }
            }
            //取消加载弹窗
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            //完成下拉刷新动作
            if (mSmartRefreshLayout != null) {
                if (isRefreshing) {
                    mSmartRefreshLayout.finishRefresh(!isError);
                }
                isRefreshing = false;
            }
        });
    }

    //显示Toast
    @Override
    public void showToast(CharSequence text) {
        showToast(text, true, false);
    }

    //显示Toast
    @Override
    public void showToast(CharSequence text, boolean isCenter, boolean longToast) {
        runOnUiThread(() -> {
            ToastUtil.showToast(text, getApplicationContext(), isCenter, longToast);
        });
    }

    //跳转到登录界面
    @Override
    public void gotoLogin() {
        BaseApplication.getInstance().finishAllActivities();
        Intent intent = new Intent();
        String action = getPackageName() + ".login";  //登录页的action
        LogUtil.i(TAG, "LoginActivity action：" + action);
        intent.setAction(action);
        startActivity(intent);
    }

    //RxJava建立订阅关系，方便Activity销毁时取消订阅关系防止内存泄漏
    @Override
    public void addDisposable(Disposable d) {
        mCompositeDisposable.add(d);
    }

    //下拉刷新
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefreshing = true;
        refreshData();  //重新加载数据
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();  //下拉时就不显示加载框了
        }
    }

    //网络断开连接提示
    public void showNetErrorLayout() {
        //如果当前布局文件中不包含layout_net_error则netErrorFl为null，此时不执行下面的逻辑
        runOnUiThread(() -> {
            if (mNetErrorFl != null) {
                mNetErrorFl.setVisibility(NetworkUtil.isConnected(getApplicationContext()) ? View.GONE : View.VISIBLE);
            }
        });
    }

    //启动指定的Activity
    protected void startActivity(Class clazz) {
        startActivity(clazz, null);
    }

    //携带数据启动指定的Activity
    protected void startActivity(Class clazz, Bundle extras) {
        if (CheckFastClickUtil.isFastClick()) {  //防止快速点击启动两个Activity
            return;
        }
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
        if (CheckFastClickUtil.isFastClick()) {  //防止快速点击启动两个Activity
            return;
        }
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    //注册广播动态监听网络变化
    private void initNetReceiver() {
        //如果不含有layout_net_error，则不注册广播
        if (mNetErrorFl == null) {
            return;
        }
        //动态注册，Android 7.0之后取消了静态注册方式
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetReceiver = new NetReceiver();
        registerReceiver(mNetReceiver, filter);
        mNetReceiver.setOnNetChangeListener(isConnected -> {
            showNetErrorLayout();
        });
    }

    //注销广播
    private void unregisterNetReceiver() {
        if (mNetErrorFl == null) {
            return;
        }
        unregisterReceiver(mNetReceiver);
        mNetReceiver = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //屏幕顶部中间区域双击获取当前Activity类名，只在debug环境下有效
        ExtraUtil.parseActivity(this, ev);
        return super.dispatchTouchEvent(ev);
    }
}
