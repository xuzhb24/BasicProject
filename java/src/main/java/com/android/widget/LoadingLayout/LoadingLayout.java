package com.android.widget.LoadingLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.java.R;

/**
 * Created by xuzhb on 2020/7/18
 * Desc:加载状态布局
 */
public class LoadingLayout extends LinearLayout {

    //加载状态
    private static final int STATE_LOADING = 0;  //加载中
    private static final int STATE_EMPTY = 1;    //无数据
    private static final int STATE_FAIL = 2;     //加载失败
    private static final int STATE_HIDE = 3;     //隐藏

    private Drawable loadingSrc;  //加载中的图片
    private String loadingDesc;   //加载中的文本描述
    private Drawable emptySrc;    //无数据的图片
    private String emptyDesc;     //无数据的文本描述
    private Drawable failSrc;     //加载失败的图片
    private String failDesc;      //加载失败的文本描述
    private String retryDesc;     //重试的文本描述

    public void setLoadingSrc(Drawable loadingSrc) {
        this.loadingSrc = loadingSrc;
        mLoadingIv.setImageDrawable(loadingSrc);
    }

    public void setLoadingDesc(String loadingDesc) {
        this.loadingDesc = loadingDesc;
        setDescText(mDescTv, loadingDesc);
    }

    public void setEmptySrc(Drawable emptySrc) {
        this.emptySrc = emptySrc;
        mEmptyIv.setImageDrawable(emptySrc);
    }

    public void setEmptyDesc(String emptyDesc) {
        this.emptyDesc = emptyDesc;
        setDescText(mDescTv, emptyDesc);
    }

    public void setFailSrc(Drawable failSrc) {
        this.failSrc = failSrc;
        mFailIv.setImageDrawable(failSrc);
    }

    public void setFailDesc(String failDesc) {
        this.failDesc = failDesc;
        setDescText(mDescTv, failDesc);
    }

    public void setRetryDesc(String retryDesc) {
        this.retryDesc = retryDesc;
        setDescText(mDescTv, retryDesc);
    }

    private ImageView mLoadingIv;
    private ImageView mEmptyIv;
    private ImageView mFailIv;
    private TextView mDescTv;
    private TextView mRetryTv;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        if (attrs != null) {
            parseAttributes(context, attrs);
        }
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_loading, this);
        mLoadingIv = layout.findViewById(R.id.loading_iv);
        mEmptyIv = layout.findViewById(R.id.empty_iv);
        mFailIv = layout.findViewById(R.id.fail_iv);
        mDescTv = layout.findViewById(R.id.desc_tv);
        mRetryTv = layout.findViewById(R.id.retry_tv);
        mRetryTv.setOnClickListener(v -> {
            if (mOnRetryListener != null) {
                mOnRetryListener.onRetry();
            }
        });
    }

    //获取自定义属性集
    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        loadingSrc = ta.getDrawable(R.styleable.LoadingLayout_loadingSrc);
        if (loadingSrc == null) {
            loadingSrc = getResources().getDrawable(R.drawable.ic_load_loading);
        }
        loadingDesc = ta.getString(R.styleable.LoadingLayout_loadingDesc);
        emptySrc = ta.getDrawable(R.styleable.LoadingLayout_emptySrc);
        if (emptySrc == null) {
            emptySrc = getResources().getDrawable(R.drawable.ic_load_empty);
        }
        emptyDesc = ta.getString(R.styleable.LoadingLayout_emptyDesc);
        failSrc = ta.getDrawable(R.styleable.LoadingLayout_failSrc);
        if (failSrc == null) {
            failSrc = getResources().getDrawable(R.drawable.ic_load_fail);
        }
        failDesc = ta.getString(R.styleable.LoadingLayout_failDesc);
        retryDesc = ta.getString(R.styleable.LoadingLayout_retryDesc);
        ta.recycle();
    }

    //开始加载
    public void loadStart() {
        setLoadState(STATE_LOADING);
    }

    //无数据
    public void loadEmpty() {
        setLoadState(STATE_EMPTY);
    }

    //加载失败
    public void loadFail() {
        setLoadState(STATE_FAIL);
    }

    //加载完成，则隐藏
    public void loadComplete() {
        setLoadState(STATE_HIDE);
    }

    private void setLoadState(int loadState) {
        System.out.println("TAG5======" + loadState);
        if (loadState == STATE_HIDE) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            mLoadingIv.clearAnimation();  //取消动画
            mLoadingIv.setVisibility(GONE);
            mEmptyIv.setVisibility(GONE);
            mFailIv.setVisibility(GONE);
            mRetryTv.setVisibility(GONE);
            if (loadState == STATE_LOADING) {
                mLoadingIv.setVisibility(VISIBLE);
                mLoadingIv.setImageDrawable(loadingSrc);
                RotateAnimation animation = new RotateAnimation(0f, 359f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.setDuration(800);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.RESTART);
                mLoadingIv.startAnimation(animation);
                setDescText(mDescTv, loadingDesc);
            } else if (loadState == STATE_EMPTY) {
                mEmptyIv.setVisibility(VISIBLE);
                mEmptyIv.setImageDrawable(emptySrc);
                setDescText(mDescTv, emptyDesc);
            } else if (loadState == STATE_FAIL) {
                mFailIv.setVisibility(VISIBLE);
                mFailIv.setImageDrawable(failSrc);
                setDescText(mDescTv, failDesc);
                setDescText(mRetryTv, retryDesc);
            }
        }
    }

    private void setDescText(TextView tv, String desc) {
        if (TextUtils.isEmpty(desc)) {
            tv.setVisibility(GONE);
        } else {
            tv.setVisibility(VISIBLE);
            tv.setText(desc);
        }
    }

    public OnRetryListener mOnRetryListener;

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public interface OnRetryListener {
        void onRetry();
    }

}
