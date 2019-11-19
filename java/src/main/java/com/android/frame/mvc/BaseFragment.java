package com.android.frame.mvc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.android.base.BaseApplication;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:基类Fragment(MVC)
 */
public abstract class BaseFragment extends Fragment {

    protected FragmentActivity mActivity;
    protected Context mContext;
    protected View mRootView;

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
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);  //引入ButterKnife
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onDestroy() {
        mUnbinder.unbind();  //解绑ButterKnife
        //监控内存泄漏
        BaseApplication.getRefWatcher().watch(mActivity);
        super.onDestroy();
    }

    public void startActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(mActivity, clazz);
        startActivity(intent);
    }

}
