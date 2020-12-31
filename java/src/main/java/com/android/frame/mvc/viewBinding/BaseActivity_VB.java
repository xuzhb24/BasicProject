package com.android.frame.mvc.viewBinding;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.android.base.BaseApplication;
import com.android.java.R;
import com.android.util.ExtraUtil;
import com.android.util.NetReceiver;
import com.android.util.NetworkUtil;
import com.android.util.StatusBar.StatusBarUtil;
import com.android.util.ToastUtil;
import com.android.widget.LoadingDialog.LoadingDialog;
import com.android.widget.TitleBar;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Activity(MVC结合ViewBinding)
 */
public abstract class BaseActivity_VB<VB extends ViewBinding> extends AppCompatActivity
        implements IBaseView, SwipeRefreshLayout.OnRefreshListener {

    protected VB binding;

    //防止RxJava内存泄漏
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    //加载框
    private LoadingDialog mLoadingDialog;
    //标题栏，需在布局文件中固定id名为title_bar
    protected TitleBar mTitleBar;
    //通用的下拉刷新组件，需在布局文件中固定id名为swipe_refresh_layout
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    //通用的RecyclerView组件，需在布局文件中固定id名为R.id.recycler_view
    protected RecyclerView mRecyclerView;

    //网路异常的布局
    private FrameLayout mNetErrorFl;
    private NetReceiver mNetReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        BaseApplication.getInstance().addActivity(this);
        initBar();
        initBaseView();
        initNetReceiver();
        handleView(savedInstanceState);
        initListener();
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected void initBar() {
        mTitleBar = findViewById(R.id.title_bar);
        if (mTitleBar != null) {
            StatusBarUtil.darkModeAndPadding(this, mTitleBar);
            if (isBarBack()) {
                mTitleBar.setOnLeftIconClickListener(v -> finish());
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

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected void initBaseView() {
        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialogStyle);
        //获取布局中的SwipeRefreshLayout组件，重用BaseCompatActivity的下拉刷新逻辑
        //注意布局中SwipeRefreshLayout的id命名为swipe_refresh_layout，否则mSwipeRefreshLayout为null
        //如果SwipeRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_recycler_view" />
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        //如果当前布局文件不包含id为swipe_refresh_layout的组件则不执行下面的逻辑
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
        //获取布局中的RecyclerView组件，注意布局中RecyclerView的id命名为recycler_view，否则mRecyclerView为null
        mRecyclerView = findViewById(R.id.recycler_view);
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = findViewById(R.id.net_error_fl);
    }

    //执行onCreate接下来的逻辑
    public abstract void handleView(Bundle savedInstanceState);

    //所有的事件回调均放在该层，如onClickListener等
    public abstract void initListener();

    //获取ViewBinding
    public abstract VB getViewBinding();

    //下拉刷新
    protected void refreshData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetReceiver();
        //销毁加载框
        mLoadingDialog.dismiss();
        mLoadingDialog = null;

        //取消所有正在执行的订阅
        mCompositeDisposable.clear();

        BaseApplication.getInstance().removeActivity(this);
    }

    //显示加载框
    @Override
    public void showLoading() {
        showLoading("", false);
    }

    //显示加载框
    @Override
    public void showLoading(String message, boolean cancelable) {
        runOnUiThread(() -> mLoadingDialog.show(message, cancelable));
    }

    //取消加载框
    @Override
    public void dismissLoading() {
        runOnUiThread(() -> mLoadingDialog.dismiss());
    }

    //显示Toast
    @Override
    public void showToast(CharSequence text) {
        showToast(text, true, false);
    }

    //显示Toast
    @Override
    public void showToast(CharSequence text, boolean isCenter, boolean longToast) {
        runOnUiThread(() -> ToastUtil.showToast(text, getApplicationContext(), isCenter, longToast));
    }

    //数据加载失败
    @Override
    public void loadFail() {
        showNetErrorLayout();
    }

    //数据加载完成，收起下拉刷新组件SwipeRefreshLayout的刷新头部
    @Override
    public void loadFinish() {
        //如果布局文件中不包含id为swipe_refresh_layout的控件，则swipeRefreshLayout为null
        runOnUiThread(() -> {
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //跳转到登录界面
    @Override
    public void gotoLogin() {
        BaseApplication.getInstance().finishAllActivities();
        Intent intent = new Intent();
        intent.setAction("登录页的action");
        startActivity(intent);
    }

    //RxJava建立订阅关系，方便Activity销毁时取消订阅关系防止内存泄漏
    @Override
    public void addDisposable(Disposable d) {
        mCompositeDisposable.add(d);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        refreshData();     //重新加载数据
        dismissLoading();  //下拉时就不显示加载框了
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

    //网络断开连接提示
    public void showNetErrorLayout() {
        runOnUiThread(() -> {
            if (mNetErrorFl != null) {
                mNetErrorFl.setVisibility(NetworkUtil.isConnected(getApplicationContext()) ? View.GONE : View.VISIBLE);
            }
        });
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
