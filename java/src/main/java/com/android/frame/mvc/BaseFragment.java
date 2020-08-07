package com.android.frame.mvc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:基类Fragment(MVC)
 */
public abstract class BaseFragment<VB extends ViewBinding>  extends Fragment {

    protected VB binding;

    protected FragmentActivity mActivity;
    protected Context mContext;

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
        return binding.getRoot();
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

    //获取ViewBinding
    public abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(mActivity, clazz);
        startActivity(intent);
    }

}
