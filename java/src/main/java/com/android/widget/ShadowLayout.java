package com.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.java.R;

/**
 * Created by xuzhb on 2020/7/1
 * Desc:阴影布局
 */
public class ShadowLayout extends ViewGroup {

    private float shadowRadius;       //模糊半径，越大越模糊，为0阴影消失不见
    private float shadowOffsetX;      //阴影的横向偏移距离，正值向右偏移，负值向左偏移
    private float shadowOffsetY;      //阴影的纵向偏移距离，正值向下偏移，负值向上偏移
    private int shadowColor;          //阴影的颜色
    private float rectRadiusX;        //圆角矩形的X轴半径
    private float rectRadiusY;        //圆角矩形的X轴半径
    private int rectBackgroundColor;  //圆角矩形的背景色，注意子View不要设置背景，不然此属性会被覆盖

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
        if (attrs != null) {
            parseAttribute(context, attrs);
        }
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
        rectBackgroundColor = ta.getColor(R.styleable.ShadowLayout_rectBackgroundColor, Color.WHITE);
        ta.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(rectBackgroundColor);
        mPaint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY, shadowColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() != 1) {
            throw new IllegalStateException("Here must be one and only one child view!");
        }
        View child = getChildAt(0);
        LayoutParams params = (LayoutParams) child.getLayoutParams();
        int childLeftMargin = (int) (Math.max(shadowRadius - shadowOffsetX, params.leftMargin) + 1);
        int childTopMargin = (int) (Math.max(shadowRadius - shadowOffsetY, params.topMargin) + 1);
        int childRightMargin = (int) (Math.max(shadowRadius + shadowOffsetX, params.rightMargin) + 1);
        int childBottomMargin = (int) (Math.max(shadowRadius + shadowOffsetY, params.bottomMargin) + 1);
        int measuredWidth = getMeasuredWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecMode;
        int widthMeasureSpecSize;
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            widthMeasureSpecMode = MeasureSpec.UNSPECIFIED;
            widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                widthMeasureSpecMode = MeasureSpec.EXACTLY;
                widthMeasureSpecSize = measuredWidth - childLeftMargin - childRightMargin;
            } else if (params.width == LayoutParams.WRAP_CONTENT) {
                widthMeasureSpecMode = MeasureSpec.AT_MOST;
                widthMeasureSpecSize = measuredWidth - childLeftMargin - childRightMargin;
            } else {
                widthMeasureSpecMode = MeasureSpec.EXACTLY;
                widthMeasureSpecSize = params.width;
            }
        }
        int measuredHeight = getMeasuredHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecMode;
        int heightMeasureSpecSize;
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpecMode = MeasureSpec.UNSPECIFIED;
            heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                heightMeasureSpecMode = MeasureSpec.EXACTLY;
                heightMeasureSpecSize = measuredHeight - childBottomMargin - childTopMargin;
            } else if (params.height == LayoutParams.WRAP_CONTENT) {
                heightMeasureSpecMode = MeasureSpec.AT_MOST;
                heightMeasureSpecSize = measuredHeight - childBottomMargin - childTopMargin;
            } else {
                heightMeasureSpecMode = MeasureSpec.EXACTLY;
                heightMeasureSpecSize = params.height;
            }
        }
        measureChild(child, MeasureSpec.makeMeasureSpec(widthMeasureSpecSize, widthMeasureSpecMode), MeasureSpec.makeMeasureSpec(heightMeasureSpecSize, heightMeasureSpecMode));
        int parentWidthMeasureSpec = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMeasureSpec = MeasureSpec.getMode(heightMeasureSpec);
        int height = measuredHeight;
        int width = measuredWidth;
        int childHeight = child.getMeasuredHeight();
        int childWidth = child.getMeasuredWidth();
        if (parentHeightMeasureSpec == MeasureSpec.AT_MOST) {
            height = childHeight + childTopMargin + childBottomMargin;
        }
        if (parentWidthMeasureSpec == MeasureSpec.AT_MOST) {
            width = childWidth + childRightMargin + childLeftMargin;
        }
        if (width < childWidth + 2 * shadowRadius) {
            width = (int) (childWidth + 2 * shadowRadius);
        }
        if (height < childHeight + 2 * shadowRadius) {
            height = (int) (childHeight + 2 * shadowRadius);
        }
        if (height != measuredHeight || width != measuredWidth) {
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int childMeasureWidth = child.getMeasuredWidth();
        int childMeasureHeight = child.getMeasuredHeight();
        child.layout((measuredWidth - childMeasureWidth) / 2, (measuredHeight - childMeasureHeight) / 2,
                (measuredWidth + childMeasureWidth) / 2, (measuredHeight + childMeasureHeight) / 2);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);  //关闭硬件加速，不然阴影效果可能无效
        View child = getChildAt(0);
        mRectF = new RectF(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        canvas.drawRoundRect(mRectF, rectRadiusX, rectRadiusY, mPaint);
        super.dispatchDraw(canvas);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}
