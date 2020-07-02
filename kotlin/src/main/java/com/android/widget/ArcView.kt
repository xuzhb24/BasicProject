package com.android.widget

import android.content.Context
import android.graphics.*
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/7/2
 * Desc:自定义圆弧
 */
class ArcView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_START_ANGLE = -90f
        private val DEFAULT_SWEEP_ANGLE = 270f
        private val DEFAULT_ROUND_CAP = true
        private val DEFAULT_RING_WIDTH = SizeUtil.dp2px(5f)
        private val DEFAULT_RING_COLOR = Color.parseColor("#0888FF")
        private val DEFAULT_RING_BACKGROUND_COLOR = Color.TRANSPARENT
    }

    //绘制开始的角度
    var startAngle = DEFAULT_START_ANGLE
    //扫描的角度
    var sweepAngle = DEFAULT_SWEEP_ANGLE
    //是否设置画笔帽为Paint.Cap.ROUND，即圆形样式，默认为true
    var roundCap = DEFAULT_ROUND_CAP
    //圆弧宽度
    var ringWidth = DEFAULT_RING_WIDTH
    //圆弧颜色，若在代码中设置了rindColorArray则无效果
    var ringColor = DEFAULT_RING_COLOR
    //圆弧渐变色，通过代码设置，实现渐变效果，优先级比ringColor高
    var rindColorArray: IntArray? = null
    //圆弧背景色
    var ringBackgroungColor = DEFAULT_RING_BACKGROUND_COLOR

    private lateinit var mArcPaint: Paint        //绘制圆弧
    private lateinit var mBgPaint: Paint         //绘制圆弧背景
    private lateinit var mRectF: RectF           //圆弧对应的RectF
    private lateinit var mShader: SweepGradient  //实现圆弧渐变效果，如果有在代码中设置rindColorArray的话

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    init {
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
    }

    //获取布局属性并设置属性默认值
    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ArcView)
        startAngle = ta.getFloat(R.styleable.ArcView_startAngle, DEFAULT_START_ANGLE)
        sweepAngle = ta.getFloat(R.styleable.ArcView_sweepAngle, DEFAULT_SWEEP_ANGLE)
        roundCap = ta.getBoolean(R.styleable.ArcView_roundCap, DEFAULT_ROUND_CAP)
        ringWidth = ta.getDimension(R.styleable.ArcView_ringWidth, DEFAULT_RING_WIDTH)
        ringColor = ta.getColor(R.styleable.ArcView_ringColor, DEFAULT_RING_COLOR)
        ringBackgroungColor = ta.getColor(R.styleable.ArcView_ringBackgroungColor, DEFAULT_RING_BACKGROUND_COLOR)
        ta.recycle()
    }

    //初始化画笔
    private fun initPaint() {
        mArcPaint = Paint()
        with(mArcPaint) {
            isAntiAlias = true
            color = ringColor
            style = Paint.Style.STROKE
            strokeWidth = ringWidth
            if (roundCap) {
                strokeCap = Paint.Cap.ROUND
            }
        }
        mBgPaint = Paint()
        with(mBgPaint) {
            isAntiAlias = true
            color = ringBackgroungColor
            style = Paint.Style.STROKE
            strokeWidth = ringWidth
        }
    }

    //重写onMeasure支持wrap_content
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))
    }

    private fun measure(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = SizeUtil.dp2px(90f).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth
        mHeight = measuredHeight
        mRectF = RectF(
            ringWidth / 2.0f, ringWidth / 2.0f,
            mWidth - ringWidth / 2.0f, mHeight - ringWidth / 2.0f
        )
        rindColorArray?.let {
            //绘制渐变效果
            mShader = SweepGradient(mRectF.centerX(), mRectF.centerY(), rindColorArray!!, null)
            val matrix = Matrix()
            matrix.setRotate(if (roundCap) calculateOffset() else startAngle, mRectF.centerX(), mRectF.centerY())
            mShader.setLocalMatrix(matrix)
        }
    }

    //设置渐变效果时让SweepGradient偏移一定角度来保证圆角凸出部分和圆环进度条颜色一样
    private fun calculateOffset(): Float {
        //计算strokeCap为Paint.Cap.ROUND时圆角凸出部分相当于整个圆环占的比例，半圆的直径 = 线的宽度
        val roundPercent = (Math.atan(ringWidth / 2.0 / (mWidth / 2.0 - ringWidth / 2.0)) / (2 * Math.PI)).toFloat()
        val curentPercent = Math.abs(sweepAngle / 360f)  //当前进度
        if (curentPercent + roundPercent >= 1.0f) {
            return startAngle
        } else if (curentPercent + 2 * roundPercent >= 1.0f) {
//            mArcPaint.strokeCap = Paint.Cap.BUTT
            if (sweepAngle < 0) {
                return startAngle + (1 - curentPercent - roundPercent) * 360f
            } else {
                return startAngle - (1 - curentPercent - roundPercent) * 360f
            }
        } else {
            if (sweepAngle < 0) {
                return startAngle + roundPercent * 360f
            } else {
                return startAngle - roundPercent * 360f
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBackgroung(it)
            drawArc(it)
        }
    }

    //绘制背景
    private fun drawBackgroung(canvas: Canvas) {
        if (ringBackgroungColor != Color.TRANSPARENT) {
            canvas.drawArc(mRectF, 0f, 360f, false, mBgPaint)
        }
    }

    //绘制圆环
    private fun drawArc(canvas: Canvas) {
        if (rindColorArray != null && rindColorArray!!.size > 1) { //实现渐变效果
            mArcPaint.setShader(mShader)
        } else {
            mArcPaint.setColor(ringColor)
        }
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mArcPaint)
    }

    //开始旋转
    fun startRotate(d: Long = 1000, i: Interpolator = LinearInterpolator()) {
        val animation = RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        startAnimation(animation.apply {
            interpolator = i
            duration = d
            repeatCount = Animation.INFINITE
            repeatMode = Animation.RESTART
        })
    }

    //停止旋转
    fun stopRotate() {
        clearAnimation()
    }

}