package com.android.widget.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import com.android.java.R;
import com.android.widget.ViewHolder;

/**
 * Created by xuzhb on 2019/10/21
 * Desc:基类Dialog，使用DialogFragment实现
 */
public abstract class BaseDialog extends DialogFragment {

    @LayoutRes
    protected int mLayoutId = -1;           //对话框的布局id
    private int mWidth;                     //对话框的宽度
    private int mHeight;                    //对话框的高度
    private int mMargin;                    //对话框左右边距
    private float mDimAmount = 0.5f;        //背景透明度
    private int mAnimationStyle = -1;       //对话框出现消失的动画
    private boolean mCancelable = true;     //是否可点击取消
    private int mGravity = Gravity.CENTER;  //对话框显示的位置，默认正中间

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
        mLayoutId = getLayoutId();  //设置dialog布局
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        convertView(new ViewHolder(view), getDialog());  //获取dialog布局的控件
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    //初始化参数
    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = mDimAmount;
            params.gravity = mGravity;
            //设置dialog宽度
            if (mWidth == 0) {
                params.width = getContext().getResources().getDisplayMetrics().widthPixels - 2 * mMargin;
            } else {
                params.width = mWidth;
            }
            //设置dialog高度
            if (mHeight == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = mHeight;
            }
            //设置dialog动画
            if (mAnimationStyle != -1) {
                window.setWindowAnimations(mAnimationStyle);
            }
            window.setAttributes(params);
        }
        setCancelable(mCancelable);
    }

    //设置宽高
    public BaseDialog setViewParams(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    //设置左右的边距
    public BaseDialog setHorizontalMargin(int margin) {
        mMargin = margin;
        return this;
    }

    //设置背景昏暗度
    public BaseDialog setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }

    //设置动画
    public BaseDialog setAnimationStyle(@StyleRes int animationStyle) {
        mAnimationStyle = animationStyle;
        return this;
    }

    //设置Outside是否可点击
    public BaseDialog setOutsideCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    //在中间显示
    public void show(FragmentManager manager) {
        super.show(manager, BaseDialog.class.getName());
    }

    //在底部显示
    public void showAtBottom(FragmentManager manager) {
        mGravity = Gravity.BOTTOM;
        super.show(manager, BaseDialog.class.getName());
    }

    //获取dialog的布局Id
    public abstract int getLayoutId();

    //处理dialog布局上的控件
    public abstract void convertView(ViewHolder holder, Dialog dialog);

}

