package com.android.frame.mvp.extra.LoadingDialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import com.android.java.R;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:自定义加载框
 */
public class LoadingDialog extends Dialog {

    private LoadingView mLoadingView;
    private TextView mMessageTv;

    public LoadingDialog(Context context) {
        this(context, 0);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_loading_layout);
        mLoadingView = findViewById(R.id.loading_view);
        mMessageTv = findViewById(R.id.message_tv);
    }

    //显示加载框
    public void show(CharSequence message, boolean cancelable) {
        RotateAnimation animation = new RotateAnimation(0, 359,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(800);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        mLoadingView.startAnimation(animation);
        if (TextUtils.isEmpty(message)) {
            mMessageTv.setVisibility(View.GONE);
        } else {
            mMessageTv.setVisibility(View.VISIBLE);
            mMessageTv.setText(message);
        }
        //点击对话框外的部分不消失
        setCanceledOnTouchOutside(false);
        //点击或按返回键时是否消失
        setCancelable(cancelable);
        //设置对话框居中
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.dimAmount = 0.2f;
        getWindow().setAttributes(params);
        show();
    }

}
