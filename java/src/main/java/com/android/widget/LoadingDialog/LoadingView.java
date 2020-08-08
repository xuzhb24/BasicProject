package com.android.widget.LoadingDialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.android.java.R;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2020/8/8
 * Desc:仿iOS菊花加载
 */
class LoadingView extends View {

    private static final int DEFAULT_PETAL_COLOR = Color.WHITE;
    private static final float DEFAULT_PETAL_LENGTH = SizeUtil.dp2px(4.5f);
    private static final float DEFAULT_PETAL_WIDTH = SizeUtil.dp2px(2f);
    private static final int DEFAULT_PETAL_COUNT = 12;

    private int petalColor = DEFAULT_PETAL_COLOR;      //花瓣颜色
    private float petalLength = DEFAULT_PETAL_LENGTH;  //花瓣长度
    private float petalWidth = DEFAULT_PETAL_WIDTH;    //花瓣宽度
    private int petalCount = DEFAULT_PETAL_COUNT;      //花瓣个数

    public void setPetalColor(int petalColor) {
        this.petalColor = petalColor;
    }

    public void setPetalLength(float petalLength) {
        this.petalLength = petalLength;
    }

    public void setPetalWidth(float petalWidth) {
        this.petalWidth = petalWidth;
    }

    public void setPetalCount(int petalCount) {
        this.petalCount = petalCount;
    }

    private Paint mPaint;
    private ValueAnimator mAnimator;
    private float mCenterX;
    private float mCenterY;
    private int mCurrentIndex = 1;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            parseAttribute(context, attrs);
        }
        initPaint();
    }

    //获取布局属性并设置属性默认值
    private void parseAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        petalColor = ta.getColor(R.styleable.LoadingView_petalColor, DEFAULT_PETAL_COLOR);
        petalLength = ta.getDimension(R.styleable.LoadingView_petalLength, DEFAULT_PETAL_LENGTH);
        petalWidth = ta.getDimension(R.styleable.LoadingView_petalWidth, DEFAULT_PETAL_WIDTH);
        petalCount = ta.getInteger(R.styleable.LoadingView_petalCount, DEFAULT_PETAL_COUNT);
        ta.recycle();
    }

    //初始化画笔
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(petalColor);
        mPaint.setStrokeWidth(petalWidth);
    }

    //重写onMeasure支持wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSpec(widthMeasureSpec, (int) SizeUtil.dp2px(30)),
                measureSpec(heightMeasureSpec, (int) SizeUtil.dp2px(30)));
    }

    private int measureSpec(int measureSpec, int minSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = minSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = getMeasuredWidth() / 2.0f;
        mCenterY = getMeasuredHeight() / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < petalCount; i++) {
            mPaint.setAlpha((i + 1 + mCurrentIndex) % petalCount * 255 / petalCount);
            canvas.drawLine(mCenterX, petalWidth / 2f + 1, mCenterX, petalLength + petalWidth / 2f + 1, mPaint);
            canvas.rotate(360f / petalCount, mCenterX, mCenterY);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimator = ValueAnimator.ofInt(petalCount, 1);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(animation -> {
            mCurrentIndex = (int) animation.getAnimatedValue();
            invalidate();
        });
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        mAnimator.cancel();
        super.onDetachedFromWindow();
    }

}
