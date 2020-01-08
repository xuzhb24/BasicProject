package com.android.frame.mvp.extra.LoadingDialog;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:渐变的圆弧
 */
public class LoadingView extends View {

    private int startColor = Color.parseColor("#f05b48");      //渐变开始色
    private int endColor = Color.parseColor("#db4b3c");        //渐变结束色
    private int ringBackground = Color.parseColor("#f7f8f9");  //圆环底色
    private float ringWidth = SizeUtil.dp2px(3.5f);               //圆环宽度

    private Paint mPaint;
    private Paint mBgPaint;
    private SweepGradient mGradient;
    private RectF mRectF;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(ringWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ringBackground);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStrokeWidth(ringWidth);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setColor(ringBackground);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRectF = new RectF(ringWidth / 2f, ringWidth / 2f,
                getMeasuredWidth() - ringWidth / 2f, getMeasuredHeight() - ringWidth / 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF, 0, 360, false, mBgPaint);
        int[] colors = {ringBackground, startColor, endColor};
        mGradient = new SweepGradient(mRectF.centerX(), mRectF.centerY(), colors, null);
        mPaint.setShader(mGradient);
        canvas.drawArc(mRectF, 270, -220, false, mPaint);
    }
}
