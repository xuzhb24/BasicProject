package com.android.frame.mvp.extra.LoadingDialog

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2019/12/29
 * Desc:渐变的圆弧
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : View(context, attrs) {

    private var startColor: Int = Color.parseColor("#f05b48")      //渐变开始色
    private var endColor: Int = Color.parseColor("#db4b3c")        //渐变结束色
    private var ringBackground: Int = Color.parseColor("#f7f8f9") //圆环底色
    private var ringWidth: Float = SizeUtil.dp2px(3.5f)               //圆环宽度

    private var mPaint: Paint
    private var mBgPaint: Paint
    private lateinit var mSweepGradient: SweepGradient
    private lateinit var mRectF: RectF

    init {
        mPaint = Paint()
        with(mPaint) {
            isAntiAlias = true
            strokeWidth = ringWidth
            style = Paint.Style.STROKE
            color = ringBackground
        }

        mBgPaint = Paint()
        with(mBgPaint) {
            isAntiAlias = true
            strokeWidth = ringWidth
            style = Paint.Style.STROKE
            color = ringBackground
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mRectF = RectF(
            ringWidth.div(2f), ringWidth.div(2f),
            measuredWidth - ringWidth.div(2f), measuredHeight - ringWidth.div(2f)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(mRectF, 0f, 360f, false, mBgPaint)
        val colors = intArrayOf(ringBackground, startColor, endColor)
        mSweepGradient = SweepGradient(mRectF.centerX(), mRectF.centerY(), colors, null)
        mPaint.setShader(mSweepGradient)
        canvas?.drawArc(mRectF, 270f, -220f, false, mPaint)
    }

}