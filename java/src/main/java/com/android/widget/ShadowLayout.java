package com.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.java.R;

/**
 * Created by xuzhb on 2020/7/1
 * Desc:阴影布局
 */
public class ShadowLayout extends FrameLayout {

    private float shadowRadius;   //模糊半径，越大越模糊，为0阴影消失不见
    private float shadowOffsetX;  //阴影的横向偏移距离，正值向右偏移，负值向左偏移
    private float shadowOffsetY;  //阴影的纵向偏移距离，正值向下偏移，负值向上偏移
    private int shadowColor;      //阴影的颜色
    private float rectRadiusX;    //生成圆角的椭圆的X轴半径
    private float rectRadiusY;    //生成圆角的椭圆的X轴半径

    private Paint mPaint;
    private RectF mRectF;

    public ShadowLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttribute(context, attrs);
        initPaint();
    }

    private void parseAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        shadowRadius = ta.getDimension(R.styleable.ShadowLayout_shadowRadius, 0);
        shadowOffsetX = ta.getDimension(R.styleable.ShadowLayout_shadowOffsetX, 0);
        shadowOffsetY = ta.getDimension(R.styleable.ShadowLayout_shadowOffsetY, 0);
        shadowColor = ta.getColor(R.styleable.ShadowLayout_shadowColor, Color.GRAY);
        rectRadiusX = ta.getDimension(R.styleable.ShadowLayout_rectRadiusX, 0);
        rectRadiusY = ta.getDimension(R.styleable.ShadowLayout_rectRadiusY, 0);
        ta.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Drawable drawable = getBackground();
        if (drawable instanceof ColorDrawable) {
            mPaint.setColor(((ColorDrawable) drawable).getColor());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(50, 50, getMeasuredWidth() - 50, getMeasuredHeight() - 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY, shadowColor);
        canvas.drawRoundRect(mRectF, rectRadiusX, rectRadiusY, mPaint);
    }
}
