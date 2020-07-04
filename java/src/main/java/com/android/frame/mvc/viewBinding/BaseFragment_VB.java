package com.android.frame.mvc.viewBinding;

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
import android.viewbinding.ViewBinding;

import com.android.base.BaseApplication;
import com.android.util.ToastUtil;
import com.google.gson.Gson;

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Fragment(MVC结合ViewBinding)
 */
public abstract class BaseFragment_VB<VB extends ViewBinding> extends Fragment {

    protected Gson gson = new Gson();
    protected VB binding;

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
        return binding.getRoot();
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

    //页面可见且布局不为null时回调
    protected void onVisible() {

    }

    //页面不可见且布局不为null时回调
    protected void onInvisible() {

    }

    //显示Toast
    public void showToast(CharSequence text) {
        showToast(text, true, false);
    }

    //显示Toast
    public void showToast(CharSequence text, boolean isCenter, boolean longToast) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                ToastUtil.showToast(text, getActivity().getApplicationContext(), isCenter, longToast);
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

    //跳转到登录界面
    protected void gotoLogin() {
        BaseApplication.getInstance().finishAllActivities();
        Intent intent = new Intent();
        intent.setAction("登录页的action");
        startActivity(intent);
    }

}

