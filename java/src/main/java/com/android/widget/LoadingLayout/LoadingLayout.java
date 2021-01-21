package com.android.widget.LoadingLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
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

    private Drawable loadingSrc;     //加载中的图片
    private String loadingDescText;  //加载中的文本描述
    private Drawable emptySrc;       //无数据的图片
    private String emptyDescText;    //无数据的文本描述
    private String emptyActionText;  //无数据时操作按钮的文本
    private Drawable failSrc;        //加载失败的图片
    private String failDescText;     //加载失败的文本描述
    private String failActionText;   //重试的文本描述
    private boolean intercept;       //是否拦截点击事件，为true时底下的View无法收到点击事件

    public void setLoadingSrc(Drawable loadingSrc) {
        this.loadingSrc = loadingSrc;
        mLoadingIv.setImageDrawable(loadingSrc);
    }

    public void setLoadingDescText(String loadingDescText) {
        this.loadingDescText = loadingDescText;
        setDescText(mDescTv, loadingDescText);
    }

    public void setEmptySrc(Drawable emptySrc) {
        this.emptySrc = emptySrc;
        mEmptyIv.setImageDrawable(emptySrc);
    }

    public void setEmptyDescText(String emptyDescText) {
        this.emptyDescText = emptyDescText;
        setDescText(mDescTv, emptyDescText);
    }

    public void setEmptyActionText(String emptyActionText) {
        this.emptyActionText = emptyActionText;
        setActionText(mActionBtn, emptyActionText);
    }

    public void setFailSrc(Drawable failSrc) {
        this.failSrc = failSrc;
        mFailIv.setImageDrawable(failSrc);
    }

    public void setFailDescText(String failDescText) {
        this.failDescText = failDescText;
        setDescText(mDescTv, failDescText);
    }

    public void setFailActionText(String failActionText) {
        this.failActionText = failActionText;
        setActionText(mActionBtn, failActionText);
    }

    //获取根布局，以便设置布局的LayoutParams
    public LinearLayout getRootLayout() {
        return mRootLayout;
    }

    private LinearLayout mRootLayout;
    private ImageView mLoadingIv;
    private ImageView mEmptyIv;
    private ImageView mFailIv;
    private TextView mDescTv;
    private Button mActionBtn;

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
        setLoadState(STATE_HIDE);
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_loading, this);
        mRootLayout = layout.findViewById(R.id.root_ll);
        mLoadingIv = layout.findViewById(R.id.loading_iv);
        mEmptyIv = layout.findViewById(R.id.empty_iv);
        mFailIv = layout.findViewById(R.id.fail_iv);
        mDescTv = layout.findViewById(R.id.desc_tv);
        mActionBtn = layout.findViewById(R.id.action_btn);
    }

    //获取自定义属性集
    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        loadingSrc = ta.getDrawable(R.styleable.LoadingLayout_loadingSrc);
        if (loadingSrc == null) {
            loadingSrc = getResources().getDrawable(R.drawable.ic_load_loading);
        }
        loadingDescText = ta.getString(R.styleable.LoadingLayout_loadingDescText);
        emptySrc = ta.getDrawable(R.styleable.LoadingLayout_emptySrc);
        if (emptySrc == null) {
            emptySrc = getResources().getDrawable(R.drawable.ic_load_empty);
        }
        emptyDescText = ta.getString(R.styleable.LoadingLayout_emptyDescText);
        emptyActionText = ta.getString(R.styleable.LoadingLayout_emptyActionText);
        failSrc = ta.getDrawable(R.styleable.LoadingLayout_failSrc);
        if (failSrc == null) {
            failSrc = getResources().getDrawable(R.drawable.ic_load_fail);
        }
        failDescText = ta.getString(R.styleable.LoadingLayout_failDescText);
        failActionText = ta.getString(R.styleable.LoadingLayout_failActionText);
        intercept = ta.getBoolean(R.styleable.LoadingLayout_intercept, true);
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
        if (loadState == STATE_HIDE) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            mLoadingIv.clearAnimation();  //取消动画
            mLoadingIv.setVisibility(GONE);
            mEmptyIv.setVisibility(GONE);
            mFailIv.setVisibility(GONE);
            mActionBtn.setVisibility(GONE);
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
                setDescText(mDescTv, loadingDescText);
            } else if (loadState == STATE_EMPTY) {
                mEmptyIv.setVisibility(VISIBLE);
                mEmptyIv.setImageDrawable(emptySrc);
                setDescText(mDescTv, emptyDescText);
                setActionText(mActionBtn, emptyActionText);
                mActionBtn.setOnClickListener(v -> {
                    if (mOnEmptyListener != null) {
                        mOnEmptyListener.onEmpty();
                    }
                });
            } else if (loadState == STATE_FAIL) {
                mFailIv.setVisibility(VISIBLE);
                mFailIv.setImageDrawable(failSrc);
                setDescText(mDescTv, failDescText);
                setActionText(mActionBtn, failActionText);
                mActionBtn.setOnClickListener(v -> {
                    if (mOnFailListener != null) {
                        mOnFailListener.onFail();
                    }
                });
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

    private void setActionText(Button btn, String text) {
        if (TextUtils.isEmpty(text)) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
            btn.setText(text);
        }
    }

    public OnEmptyListener mOnEmptyListener;

    //无数据时相应的操作
    public void setOnEmptyListener(OnEmptyListener listener) {
        this.mOnEmptyListener = listener;
    }

    public interface OnEmptyListener {
        void onEmpty();
    }

    public OnFailListener mOnFailListener;

    //加载失败时相应的操作，如点击重试
    public void setOnFailListener(OnFailListener listener) {
        this.mOnFailListener = listener;
    }

    public interface OnFailListener {
        void onFail();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return intercept;  //拦截点击事件
    }
}
