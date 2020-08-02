package com.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

import com.android.java.R;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2020/7/11
 * Desc:自定义圆弧
 */
public class ArcView extends View {

    private static final float DEFAULT_START_ANGLE = -90f;
    private static final float DEFAULT_SWEEP_ANGLE = 270f;
    private static final boolean DEFAULT_ROUND_CAP = true;
    private static final float DEFAULT_RING_WIDTH = SizeUtil.dp2px(5f);
    private static final int DEFAULT_RING_COLOR = Color.parseColor("#0888FF");
    private static final int DEFAULT_RING_BACKGROUND_COLOR = Color.TRANSPARENT;

    //绘制开始的角度
    private float startAngle = DEFAULT_START_ANGLE;
    //扫描的角度
    private float sweepAngle = DEFAULT_SWEEP_ANGLE;
    //是否设置画笔帽为Paint.Cap.ROUND，即圆形样式，默认为true
    private boolean roundCap = DEFAULT_ROUND_CAP;
    //圆弧宽度
    private float ringWidth = DEFAULT_RING_WIDTH;
    //圆弧颜色，若在代码中设置了rindColorArray则无效果
    private int ringColor = DEFAULT_RING_COLOR;
    //圆弧渐变色，通过代码设置，实现渐变效果，优先级比ringColor高
    private int[] rindColorArray;
    //圆弧背景色
    private int ringBackgroungColor = DEFAULT_RING_BACKGROUND_COLOR;

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public boolean isRoundCap() {
        return roundCap;
    }

    public void setRoundCap(boolean roundCap) {
        this.roundCap = roundCap;
    }

    public float getRingWidth() {
        return ringWidth;
    }

    public void setRingWidth(float ringWidth) {
        this.ringWidth = ringWidth;
    }

    public int getRingColor() {
        return ringColor;
    }

    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
    }

    public int[] getRindColorArray() {
        return rindColorArray;
    }

    public void setRindColorArray(int[] rindColorArray) {
        this.rindColorArray = rindColorArray;
    }

    public int getRingBackgroungColor() {
        return ringBackgroungColor;
    }

    public void setRingBackgroungColor(int ringBackgroungColor) {
        this.ringBackgroungColor = ringBackgroungColor;
    }

    private Paint mArcPaint;        //绘制圆弧
    private Paint mBgPaint;         //绘制圆弧背景
    private RectF mRectF;           //圆弧对应的RectF
    private SweepGradient mShader;  //实现圆弧渐变效果，如果有在代码中设置rindColorArray的话

    private int mWidth;
    private int mHeight;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            parseAttributes(context, attrs);
        }
        initPaint();
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        startAngle = ta.getFloat(R.styleable.ArcView_startAngle, DEFAULT_START_ANGLE);
        sweepAngle = ta.getFloat(R.styleable.ArcView_sweepAngle, DEFAULT_SWEEP_ANGLE);
        roundCap = ta.getBoolean(R.styleable.ArcView_roundCap, DEFAULT_ROUND_CAP);
        ringWidth = ta.getDimension(R.styleable.ArcView_ringWidth, DEFAULT_RING_WIDTH);
        ringColor = ta.getColor(R.styleable.ArcView_ringColor, DEFAULT_RING_COLOR);
        ringBackgroungColor = ta.getColor(R.styleable.ArcView_ringBackgroungColor, DEFAULT_RING_BACKGROUND_COLOR);
        ta.recycle();
    }

    private void initPaint() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(ringColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(ringWidth);
        if (roundCap) {
            mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(ringBackgroungColor);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(ringWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) SizeUtil.dp2px(90f);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRectF = new RectF(ringWidth / 2.0f, ringWidth / 2.0f,
                mWidth - ringWidth / 2.0f, mHeight - ringWidth / 2.0f);
        if (rindColorArray != null) { //绘制渐变效果
            mShader = new SweepGradient(mRectF.centerX(), mRectF.centerY(), rindColorArray, null);
            Matrix matrix = new Matrix();
            matrix.setRotate(roundCap ? calculateOffset() : startAngle, mRectF.centerX(), mRectF.centerY());
            mShader.setLocalMatrix(matrix);
        }
    }

    //设置渐变效果时让SweepGradient偏移一定角度来保证圆角凸出部分和圆环进度条颜色一样
    private float calculateOffset() {
        //计算strokeCap为Paint.Cap.ROUND时圆角凸出部分相当于整个圆环占的比例，半圆的直径 = 线的宽度
        float roundPercent = (float) (Math.atan(ringWidth / 2.0 / (mWidth / 2.0 - ringWidth / 2.0)) / (2 * Math.PI));
        float curentPercent = Math.abs(sweepAngle / 360f);  //当前进度
        if (curentPercent + roundPercent >= 1.0f) {
            return startAngle;
        } else if (curentPercent + 2 * roundPercent >= 1.0f) {
//            mArcPaint.strokeCap = Paint.Cap.BUTT
            if (sweepAngle < 0) {
                return startAngle + (1 - curentPercent - roundPercent) * 360f;
            } else {
                return startAngle - (1 - curentPercent - roundPercent) * 360f;
            }
        } else {
            if (sweepAngle < 0) {
                return startAngle + roundPercent * 360f;
            } else {
                return startAngle - roundPercent * 360f;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        if (ringBackgroungColor != Color.TRANSPARENT) {
            canvas.drawArc(mRectF, 0f, 360f, false, mBgPaint);
        }
        //绘制圆弧
        if (rindColorArray != null && rindColorArray.length > 1) { //实现渐变效果
            mArcPaint.setShader(mShader);
        } else {
            mArcPaint.setColor(ringColor);
        }
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mArcPaint);
    }

    //开始旋转
    public void startRotate() {
        startRotate(1000, new LinearInterpolator());
    }

    //开始旋转
    public void startRotate(long duration, Interpolator interpolator) {
        RotateAnimation animation = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(interpolator);
        animation.setDuration(duration);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        startAnimation(animation);
    }

    //停止旋转
    public void stopRotate() {
        clearAnimation();
    }

}
