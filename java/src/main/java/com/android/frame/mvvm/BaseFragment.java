package com.android.frame.mvvm;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.android.base.BaseApplication;
import com.android.java.R;
import com.android.util.CheckFastClickUtil;
import com.android.util.LogUtil;
import com.android.util.NetReceiver;
import com.android.util.NetworkUtil;
import com.android.util.ToastUtil;
import com.android.widget.LoadingDialog.LoadingDialog;
import com.android.widget.LoadingLayout.LoadingLayout;
import com.android.widget.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:基类Fragment(MVVM)
 */
public abstract class BaseFragment<VB extends ViewBinding, VM extends BaseViewModel> extends Fragment implements IBaseView, OnRefreshListener {

    private static final String TAG = "BaseFragment";

    protected VB binding;
    protected VM viewModel;

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

    protected Context mContext;

    protected boolean isRefreshing = false;          //是否正在下拉刷新
    protected boolean hasDataLoaded = false;         //是否加载过数据，不管加载成功或失败
    protected boolean hasDataLoadedSuccess = false;  //是否成功加载过数据，设置这个变量的原因是加载状态布局一般只会在第一次加载时显示，当加载成功过一次就不再显示

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewBindingAndViewModel();  //获取ViewBinding和ViewModel
        initBaseView();     //初始化一些通用控件
        initNetReceiver();  //监听网络变化
        return binding.getRoot();
    }

    //获取ViewBinding和ViewModel，这里通过反射获取
    protected void initViewBindingAndViewModel() {
        if (binding == null) {
            Type superclass = getClass().getGenericSuperclass();
            Class<VB> vbClass = (Class<VB>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
            Class<VM> vmClass = (Class<VM>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
            try {
                Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class);
                binding = (VB) method.invoke(null, getLayoutInflater());
                viewModel = new ViewModelProvider(this).get(vmClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //初始化一些通用控件，如加载框、SmartRefreshLayout、网络错误提示布局
    protected void initBaseView() {
        mTitleBar = binding.getRoot().findViewById(R.id.title_bar);
        mLoadingDialog = new LoadingDialog(mContext, R.style.LoadingDialogStyle);
        //获取布局中的加载状态布局
        mLoadingLayout = binding.getRoot().findViewById(R.id.loading_layout);
        if (mLoadingLayout != null) {
            mLoadingLayout.setOnFailListener(this::refreshData);
        }
        //获取布局中的SmartRefreshLayout组件，重用BaseActivity的下拉刷新逻辑
        //注意布局中SmartRefreshLayout的id命名为smart_refresh_layout，否则mSmartRefreshLayout为null
        //如果SmartRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_list" />
        mSmartRefreshLayout = binding.getRoot().findViewById(R.id.smart_refresh_layout);
        //如果当前布局文件不包含id为smart_refresh_layout的组件则不执行下面的逻辑
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
            mSmartRefreshLayout.setEnableLoadMore(false);
            mSmartRefreshLayout.setOnRefreshListener(this);
        }
        //获取布局中的RecyclerView组件，注意布局中RecyclerView的id命名为recycler_view，否则mRecyclerView为null
        mRecyclerView = binding.getRoot().findViewById(R.id.recycler_view);
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = binding.getRoot().findViewById(R.id.net_error_fl);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModelObserver();  //监听BaseViewMode的数据变化
        handleView(savedInstanceState);  //执行onCreate接下来的逻辑
        initListener();  //所有的事件回调均放在该层，如onClickListener等
        if (!needLazyLoadData()) {
            LogUtil.i(TAG, getClass().getName() + " 正在加载数据（非懒加载）");
            refreshData();  //不实现懒加载，即一开始创建页面即加载数据
        }
    }

    //监听BaseViewMode的数据变化
    protected void initViewModelObserver() {
        //加载状态布局
        viewModel.showLoadLayoutData.observe(this, show -> {
            showLoadingLayout();
        });
        //加载弹窗
        viewModel.showLoadingDialogData.observe(this, config -> {
            showLoadingDialog(config.getMessage(), config.isCancelable());
        });
        //加载完成的逻辑
        viewModel.loadFinishErrorData.observe(this, this::loadFinish);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(TAG, getClass().getName() + " onResume");
        if (needLazyLoadData() && !hasDataLoaded) {
            LogUtil.i(TAG, getClass().getName() + " 正在加载数据（懒加载）");
            //刷新数据
            refreshData();
            hasDataLoaded = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG, getClass().getName() + " onPause");
    }

    //执行onCreate接下来的逻辑
    public abstract void handleView(Bundle savedInstanceState);

    //所有的事件回调均放在该层，如onClickListener等
    public abstract void initListener();

    //是否需要懒加载，返回true表示切换到页面时才会加载数据，主要用在ViewPager切换中，
    //注意FragmentPagerAdapter使用BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    protected boolean needLazyLoadData() {
        return true;
    }

    //加载数据，进入页面时默认就会进行加载，请务必重写refreshData，当加载失败点击重试或者下拉刷新时会调用这个方法
    protected void refreshData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterNetReceiver();
        //销毁加载框
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog("加载中", true);
    }

    @Override
    public void showLoadingDialog(String message, boolean cancelable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (mLoadingDialog != null) {
                    mLoadingDialog.show(message, cancelable);
                }
            });
        }
    }

    @Override
    public void showLoadingLayout() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (!isRefreshing && !hasDataLoadedSuccess && mLoadingLayout != null) {  //下拉刷新或者加载成功过都不显示加载状态
                    mLoadingLayout.loadStart();
                }
            });
        }
    }

    @Override
    public void loadFinish(boolean isError) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
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
            getActivity().runOnUiThread(() -> {
                ToastUtil.showToast(text, getActivity().getApplicationContext(), isCenter, longToast);
            });
        }
    }

    //跳转到登录界面
    @Override
    public void gotoLogin() {
        BaseApplication.getInstance().finishAllActivities();
        Intent intent = new Intent();
        String action = mContext.getPackageName() + ".login";  //登录页的action
        LogUtil.i(TAG, "LoginActivity action：" + action);
        intent.setAction(action);
        startActivity(intent);
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
        if (CheckFastClickUtil.isFastClick()) {  //防止快速点击启动两个Activity
            return;
        }
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
        if (CheckFastClickUtil.isFastClick()) {  //防止快速点击启动两个Activity
            return;
        }
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
