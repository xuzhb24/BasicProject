package com.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.java.R;

/**
 * Created by xuzhb on 2020/7/17
 * Desc:加载框
 */
public class LoadingDialog extends Dialog {

    private String mMessage;
    private boolean mCancelable;

    private TextView mLoadingTv;

    public LoadingDialog(@NonNull Context context) {
        this(context, null);
    }

    public LoadingDialog(@NonNull Context context, String message) {
        this(context, message, true);
    }

    public LoadingDialog(@NonNull Context context, String message, boolean cancelable) {
        super(context, R.style.LoadingDialogStyle);
        this.mMessage = message;
        this.mCancelable = cancelable;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        mLoadingTv = findViewById(R.id.loading_tv);
        if (TextUtils.isEmpty(mMessage)) {
            mLoadingTv.setVisibility(View.GONE);
        } else {
            mLoadingTv.setVisibility(View.VISIBLE);
            mLoadingTv.setText(mMessage);
        }
        //点击对话框外的部分不消失
        setCanceledOnTouchOutside(mCancelable);
        //点击或按返回键时是否消失
        setCancelable(mCancelable);
        //设置对话框居中
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

}
