package com.android.frame.mvc.viewBinding;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.android.base.BaseApplication;
import com.android.java.R;
import com.android.util.NetReceiver;
import com.android.util.NetworkUtil;
import com.android.util.ToastUtil;
import com.android.widget.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Fragment(MVC结合ViewBinding)
 */
public abstract class BaseFragment_VB<VB extends ViewBinding> extends Fragment
        implements IBaseView, SwipeRefreshLayout.OnRefreshListener {

    protected VB binding;

    //防止RxJava内存泄漏
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    //加载框
    private LoadingDialog mLoadingDialog;
    //通用的下拉刷新组件，需在布局文件中固定id名为swipe_refresh_layout
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //通用的RecyclerView组件，需在布局文件中固定id名为R.id.recycler_view
    protected RecyclerView mRecyclerView;

    //网路异常的布局
    private FrameLayout mNetErrorFl;
    private NetReceiver mNetReceiver;

    protected FragmentActivity mActivity;
    protected Context mContext;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //第一次加载时，setUserVisibleHint会比onAttach先回调，此时布局为null;
        //之后切换Fragment时，当Fragment变得可见或可见时都会回调setUserVisibleHint，此时布局不为null
        if (binding != null) {
            if (isVisibleToUser) {
                onVisible();
            } else {
                onInvisible();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = getViewBinding(inflater, container);
        }
        initBaseView();
        initNetReceiver();
        return binding.getRoot();
    }

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected void initBaseView() {
        mLoadingDialog = new LoadingDialog(getContext(), R.style.LoadingDialogStyle);
        //获取布局中的SwipeRefreshLayout组件，重用BaseCompatActivity的下拉刷新逻辑
        //注意布局中SwipeRefreshLayout的id命名为swipe_refresh_layout，否则mSwipeRefreshLayout为null
        //如果SwipeRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_recycler_view" />
        mSwipeRefreshLayout = binding.getRoot().findViewById(R.id.swipe_refresh_layout);
        //如果当前布局文件不包含id为swipe_refresh_layout的组件则不执行下面的逻辑
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
        //获取布局中的RecyclerView组件，注意布局中RecyclerView的id命名为recycler_view，否则mRecyclerView为null
        mRecyclerView = mRecyclerView.findViewById(R.id.recycler_view);
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = binding.getRoot().findViewById(R.id.net_error_fl);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleView(savedInstanceState);
        initListener();
    }

    //执行onCreate接下来的逻辑
    public abstract void handleView(Bundle savedInstanceState);

    //所有的事件回调均放在该层，如onClickListener等
    public abstract void initListener();

    //获取ViewBinding
    public abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);

    //下拉刷新
    public void refreshData() {

    }

    //页面可见且布局不为null时回调
    protected void onVisible() {

    }

    //页面不可见且布局不为null时回调
    protected void onInvisible() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterNetReceiver();
        //销毁加载框
        mLoadingDialog.dismiss();
        mLoadingDialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消所有正在执行的订阅
        mCompositeDisposable.clear();
    }

    @Override
    public void showLoading() {
        showLoading("", false);
    }

    //显示加载框
    @Override
    public void showLoading(String message, boolean cancelable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> mLoadingDialog.show(message, cancelable));
        }
    }

    //取消加载框
    @Override
    public void dismissLoading() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> mLoadingDialog.dismiss());
        }
    }

    //显示Toast
    @Override
    public void showToast(CharSequence text) {
        showToast(text, true, false);
    }

    //显示Toast
    @Override
    public void showToast(CharSequence text, boolean isCenter, boolean longToast) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> ToastUtil.showToast(text, getActivity().getApplicationContext(), isCenter, longToast));
        }
    }

    //数据加载失败
    @Override
    public void loadFail() {
        showNetErrorLayout();
    }

    //完成数据加载，收起下拉刷新组件SwipeRefreshLayout的刷新头部
    @Override
    public void loadFinish() {
        if (getActivity() != null) {
            //如果布局文件中不包含id为swipe_refresh_layout的控件，则swipeRefreshLayout为null
            getActivity().runOnUiThread(() -> {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
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

    //网络断开连接提示
    public void showNetErrorLayout() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (mNetErrorFl != null) {
                    mNetErrorFl.setVisibility(NetworkUtil.isConnected(getActivity()) ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    //启动指定的Activity
    protected void startActivity(Class clazz) {
        startActivity(clazz, null);
    }

    //携带数据启动指定的Activity
    protected void startActivity(Class clazz, Bundle extras) {
        if (getActivity() != null) {
            Intent intent = new Intent();
            if (extras != null) {
                intent.putExtras(extras);
            }
            intent.setClass(getActivity(), clazz);
            startActivity(intent);
        }
    }

    //启动指定的Activity并接收返回的结果
    protected void startActivityForResult(Class clazz, int requestCode) {
        startActivityForResult(clazz, null, requestCode);
    }

    //携带数据启动指定的Activity并接受返回的结果
    protected void startActivityForResult(Class clazz, Bundle extras, int requestCode) {
        if (getActivity() != null) {
            Intent intent = new Intent();
            if (extras != null) {
                intent.putExtras(extras);
            }
            intent.setClass(getActivity(), clazz);
            startActivityForResult(intent, requestCode);
        }
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
        if (getContext() != null) {
            getContext().registerReceiver(mNetReceiver, filter);
        }
        mNetReceiver.setOnNetChangeListener(isConnected -> {
            showNetErrorLayout();
        });
    }

    //注销广播
    private void unregisterNetReceiver() {
        if (mNetErrorFl == null) {
            return;
        }
        if (getContext() != null) {
            getContext().unregisterReceiver(mNetReceiver);
        }
        mNetReceiver = null;
    }

}

