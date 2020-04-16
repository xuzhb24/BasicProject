package com.android.frame.mvc.viewBinding.AATest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.frame.mvc.viewBinding.BaseFragment_VB;
import com.android.java.databinding.FragmentTestViewbindingBinding;
import com.android.util.LogUtil;

/**
 * Created by xuzhb on 2020/4/14
 * Desc:
 */
public class TestViewBindingFragment extends BaseFragment_VB<FragmentTestViewbindingBinding> {

    public static final String EXTRA_DATA = "EXTRA_TYPE";

    private String mData;

    public static TestViewBindingFragment newInstance(String data) {
        TestViewBindingFragment fragment = new TestViewBindingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mData = getArguments().getString(EXTRA_DATA);
        }
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        binding.tv.setText(mData);
    }

    @Override
    public void initListener() {

    }

    @Override
    public FragmentTestViewbindingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTestViewbindingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void onVisible() {
        log("onVisible 可见");
    }

    @Override
    protected void onInvisible() {
        log("onInvisible 不可见");
    }

    private void log(String tag) {
        showToast(mData + " " + tag);
        LogUtil.e("TestViewBindingFragment", mData + " " + tag);
    }
}
