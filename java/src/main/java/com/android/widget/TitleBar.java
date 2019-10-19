package com.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.java.R;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:标题栏
 */
public class TitleBar extends FrameLayout {

    private static final float DEFAULT_MARGIN = SizeUtil.dp2px(20f);
    private static final float DEFAULT_TEXT_MARGIN = SizeUtil.dp2px(20f);
    private static final float DEFAULT_SIDE_TEXT_SIZE = SizeUtil.sp2px(15f);
    private static final int DEFAULT_SIDE_TEXT_COLOR = Color.WHITE;
    private static final float DEFAULT_TEXT_SIZE = SizeUtil.sp2px(18f);
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private Drawable leftIcon;
    private float leftIconMargin;
    private boolean showLeftIcon;
    private Drawable rightIcon;
    private float rightIconMargin;
    private boolean showRightIcon;
    private String leftText;
    private float leftTextSize;
    private int leftTextColor;
    private float leftTextMargin;
    private String titleText;
    private float titleTextSize;
    private int titleTextColor;
    private String rightText;
    private float rightTextSize;
    private int rightTextColor;
    private float rightTextMargin;
    private boolean showDivider;

    public void setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
        mLeftIv.setImageDrawable(leftIcon);
    }

    public void setLeftIconMargin(float leftIconMargin) {
        this.leftIconMargin = leftIconMargin;
        setViewHorizontalMargin(mLeftFl, leftIconMargin, true);
    }

    public void showLeftIcon(boolean showLeftIcon) {
        this.showLeftIcon = showLeftIcon;
        if (showLeftIcon) {
            mLeftFl.setVisibility(View.VISIBLE);
        } else {
            mLeftFl.setVisibility(View.GONE);
        }
    }

    public void setRightIcon(Drawable rightIcon) {
        this.rightIcon = rightIcon;
        mRightIv.setImageDrawable(rightIcon);
    }

    public void setRightIconMargin(float rightIconMargin) {
        this.rightIconMargin = rightIconMargin;
        setViewHorizontalMargin(mRightFl, rightIconMargin, false);
    }

    public void showRightIcon(boolean showRightIcon) {
        this.showRightIcon = showRightIcon;
        if (showRightIcon) {
            mRightFl.setVisibility(View.VISIBLE);
        } else {
            mRightFl.setVisibility(View.GONE);
        }
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        mLeftTv.setText(leftText);
    }

    public void setLeftTextSize(float leftTextSize) {
        this.leftTextSize = leftTextSize;
        mLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
    }

    public void setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
        mLeftTv.setTextColor(leftTextColor);
    }

    public void setLeftTextMargin(float leftTextMargin) {
        this.leftTextMargin = leftTextMargin;
        setViewHorizontalMargin(mLeftTv, leftTextMargin, true);
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        mTitleTv.setText(titleText);
    }

    public void setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        mTitleTv.setTextColor(titleTextColor);
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        mRightTv.setText(rightText);
    }

    public void setRightTextSize(float rightTextSize) {
        this.rightTextSize = rightTextSize;
        mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
    }

    public void setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        mRightTv.setTextColor(rightTextColor);
    }

    public void setRightTextMargin(float rightTextMargin) {
        this.rightTextMargin = rightTextMargin;
        setViewHorizontalMargin(mRightTv, rightTextMargin, false);
    }

    public void showDivider(boolean showDivider) {
        this.showDivider = showDivider;
        if (showDivider) {
            mDividerLine.setVisibility(View.VISIBLE);
        } else {
            mDividerLine.setVisibility(View.GONE);
        }
    }

    //设置背景色
    @Override
    public void setBackgroundColor(int color) {
        mTitleFl.setBackgroundColor(color);
    }

    private FrameLayout mTitleFl;
    private FrameLayout mLeftFl;
    private ImageView mLeftIv;
    private TextView mLeftTv;
    private TextView mTitleTv;
    private TextView mRightTv;
    private FrameLayout mRightFl;
    private ImageView mRightIv;
    private View mDividerLine;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.TitleBar), context);
        initView(context);
    }

    //获取自定义属性集
    private void parseAttributes(TypedArray ta, Context context) {
        leftIcon = ta.getDrawable(R.styleable.TitleBar_leftIcon);
        if (leftIcon == null) {
            leftIcon = context.getResources().getDrawable(R.drawable.ic_back);
        }
        leftIconMargin = ta.getDimension(R.styleable.TitleBar_leftIconMargin, DEFAULT_MARGIN);
        showLeftIcon = ta.getBoolean(R.styleable.TitleBar_showLeftIcon, true);
        rightIcon = ta.getDrawable(R.styleable.TitleBar_rightIcon);
        if (rightIcon == null) {
            rightIcon = context.getResources().getDrawable(R.drawable.ic_add);
        }
        rightIconMargin = ta.getDimension(R.styleable.TitleBar_rightIconMargin, DEFAULT_MARGIN);
        showRightIcon = ta.getBoolean(R.styleable.TitleBar_showRightIcon, false);
        leftText = ta.getString(R.styleable.TitleBar_leftText);
        leftTextSize = ta.getDimension(R.styleable.TitleBar_leftTextSize, DEFAULT_SIDE_TEXT_SIZE);
        leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor, DEFAULT_SIDE_TEXT_COLOR);
        leftTextMargin = ta.getDimension(R.styleable.TitleBar_leftTextMargin, DEFAULT_TEXT_MARGIN);
        titleText = ta.getString(R.styleable.TitleBar_titleText);
        titleTextSize = ta.getDimension(R.styleable.TitleBar_titleTextSize, DEFAULT_TEXT_SIZE);
        titleTextColor = ta.getColor(R.styleable.TitleBar_titleTextColor, DEFAULT_TEXT_COLOR);
        rightText = ta.getString(R.styleable.TitleBar_rightText);
        rightTextSize = ta.getDimension(R.styleable.TitleBar_rightTextSize, DEFAULT_SIDE_TEXT_SIZE);
        rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor, DEFAULT_SIDE_TEXT_COLOR);
        rightTextMargin = ta.getDimension(R.styleable.TitleBar_rightTextMargin, DEFAULT_TEXT_MARGIN);
        showDivider = ta.getBoolean(R.styleable.TitleBar_showDivider, false);
        ta.recycle();
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);
        mTitleFl = layout.findViewById(R.id.title_fl);
        mLeftFl = layout.findViewById(R.id.left_fl);
        mLeftIv = layout.findViewById(R.id.left_iv);
        mLeftTv = layout.findViewById(R.id.left_tv);
        mTitleTv = layout.findViewById(R.id.title_tv);
        mRightTv = layout.findViewById(R.id.right_tv);
        mRightFl = layout.findViewById(R.id.right_fl);
        mRightIv = layout.findViewById(R.id.right_iv);
        mDividerLine = layout.findViewById(R.id.divider_line);

        if (showLeftIcon) {
            mLeftIv.setImageDrawable(leftIcon);
            mLeftFl.setVisibility(View.VISIBLE);
            setViewHorizontalMargin(mLeftFl, leftIconMargin, true);
        } else {
            mLeftFl.setVisibility(View.GONE);
        }

        if (showRightIcon) {
            mRightIv.setImageDrawable(rightIcon);
            setViewHorizontalMargin(mRightFl, rightIconMargin, false);
            mRightFl.setVisibility(View.VISIBLE);
        } else {
            mRightFl.setVisibility(View.GONE);
        }

        mLeftTv.setText(leftText);
        mLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        mLeftTv.setTextColor(leftTextColor);
        setViewHorizontalMargin(mLeftTv, leftTextMargin, true);

        mTitleTv.setText(titleText);
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        mTitleTv.setTextColor(titleTextColor);

        mRightTv.setText(rightText);
        mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        mRightTv.setTextColor(rightTextColor);
        setViewHorizontalMargin(mRightTv, rightTextMargin, false);

        if (showDivider) {
            mDividerLine.setVisibility(View.VISIBLE);
        } else {
            mDividerLine.setVisibility(View.GONE);
        }

    }

    private void setViewHorizontalMargin(View view, float margin, boolean isLeft) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (isLeft) {
            params.leftMargin = (int) margin;
        } else {
            params.rightMargin = (int) margin;
        }
        view.requestLayout();
    }

    private OnLeftClickListener mOnLeftClickListener;
    private OnRightClickListener mOnRightClickListener;

    public void setOnLeftClickListener(OnLeftClickListener listener) {
        this.mOnLeftClickListener = listener;
        mLeftFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLeftClickListener.onLeftClick(v);
            }
        });
    }

    public void setOnRightClickListener(OnRightClickListener listener) {
        this.mOnRightClickListener = listener;
        mRightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRightClickListener.onRightClick(v);
            }
        });
    }

    public interface OnLeftClickListener {
        void onLeftClick(View v);
    }

    public interface OnRightClickListener {
        void onRightClick(View v);
    }

}
