package com.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.java.R;

/**
 * Created by xuzhb on 2020/7/17
 * Desc:加载框
 */
public class LoadingDialog extends Dialog {

    private TextView mLoadingTv;

    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialogStyle);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_loading);
        mLoadingTv = findViewById(R.id.loading_tv);
    }

    //显示加载框，默认可取消
    public void show(CharSequence message) {
        show(message, true);
    }

    //显示加载框
    public void show(CharSequence message, boolean cancelable) {
        if (TextUtils.isEmpty(message)) {
            mLoadingTv.setVisibility(View.GONE);
        } else {
            mLoadingTv.setVisibility(View.VISIBLE);
            mLoadingTv.setText(message);
        }
        //点击对话框外的部分不消失
        setCanceledOnTouchOutside(cancelable);
        //点击或按返回键时是否消失
        setCancelable(cancelable);
        show();
    }

}