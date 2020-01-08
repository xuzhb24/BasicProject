package com.android.frame.mvp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.android.base.BaseApplication;
import com.android.frame.mvp.extra.LoadingDialog.LoadingDialog;
import com.android.frame.mvp.extra.NetReceiver;
import com.android.java.R;
import com.android.util.ToastUtil;
import com.google.gson.Gson;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:基类Fragment(MVP)
 */
public abstract class BaseFragment<V extends IBaseView, P extends BasePresenter<V>> extends Fragment
        implements IBaseView, SwipeRefreshLayout.OnRefreshListener {

    protected Gson mGson = new Gson();
    protected P mPresenter;

    //防止RxJava内存泄漏
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    //加载框
    private LoadingDialog mLoadingDialog;
    //通用的下拉刷新组件
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //网路异常的布局
    private FrameLayout mNetErrorFl;
    private NetReceiver mNetReceiver;

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;

    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);  //引入ButterKnife
        mPresenter = getPresenter();
        mPresenter.attachView((V) this);
        initBaseView();
        initNetReceiver();
        return mRootView;
    }

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected void initBaseView() {
        mLoadingDialog = new LoadingDialog(getContext(), R.style.LoadingDialogStyle);
        //当布局文件中包含SwipeRefreshLayout组件，id命名为swipe_refresh_layout即可重用BaseCompatActivity的下拉刷新逻辑
        //如果SwipeRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_recycler_view" />
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh_layout);
        //如果当前布局文件不包含id为swipe_refresh_layout的组件则不执行下面的逻辑
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = mRootView.findViewById(R.id.net_error_fl);

        /*
         * 完整的一次下拉刷新过程
         * 1、在布局文件中包含id为swipe_refresh_layout的SwipeRefreshLayout组件；
         * 2、下拉时SwipeRefreshLayout的onRefresh()调用BasePresenter的loadData()重新加载数据；
         * 3、请求数据结束或请求数据异常调用IBaseView的loadFinish()收起SwipeRefreshLayout的刷新头部，完成一次下拉刷新；
         * 所以实现下拉刷新只需要：
         * 1、在布局文件中包含id为swipe_refresh_layout的SwipeRefreshLayout组件；
         * 2、重写BasePresenter的loadData()方法；
         * 3、在刷新结束时调用IBaseView的loadFinish()方法收起刷新头部，而在自定义Observer类CustomObserver中已经实现了
         *    这部分逻辑，在请求数据结束或请求数据出现异常时会调用IBaseView的loadFinish()
         */
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

    //获取布局
    public abstract int getLayoutId();

    //获取Activity对应的Presenter，对于不需要额外声明Presenter的Activity，可以选择继承CommonBaseActivity
    public abstract P getPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterNetReceiver();
        //销毁加载框
        mLoadingDialog.dismiss();
        mLoadingDialog = null;

        //解绑activity和presenter
        mPresenter.detachView();
        mPresenter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消所有正在执行的订阅
        mCompositeDisposable.clear();
        mUnbinder.unbind();  //解绑ButterKnife
        //监控内存泄漏
        BaseApplication.getRefWatcher().watch(this);
    }

    @Override
    public void showLoading() {
        showLoading("", false);
    }

    //显示加载框
    @Override
    public void showLoading(String message, boolean cancelable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                mLoadingDialog.show(message, cancelable);
            });
        }
    }

    //取消加载框
    @Override
    public void dismissLoading() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                mLoadingDialog.dismiss();
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
                ToastUtil.showToast(text, getContext(), isCenter, longToast);
            });
        }
    }

    //显示网络错误提示布局
    @Override
    public void showNetErrorLayout(boolean isShow) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (mNetErrorFl != null) {
                    mNetErrorFl.setVisibility(isShow ? View.VISIBLE : View.GONE);
                }
            });
        }
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
        mPresenter.loadData();  //重新加载数据
        dismissLoading();  //下拉时就不显示加载框了
    }

    //启动指定的Activity
    protected void startActivity(Class clazz) {
        if (getActivity() != null) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), clazz);
            startActivity(intent);
        } else {
            showToast("activity已销毁");
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
            showNetErrorLayout(!isConnected);
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
