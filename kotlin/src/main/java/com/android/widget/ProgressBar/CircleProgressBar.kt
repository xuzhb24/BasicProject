package com.android.widget.ProgressBar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.View
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/3/25
 * Desc:圆形进度条
 */
class CircleProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_MAX = 100
        private val DEFAULT_PROGRESS = 0
        private val DEFAULT_START_ANGLE = -90f
        private val DEFAULT_REVERSE = false
        private val DEFAULT_ROUND_CAP = true
        private val DEFAULT_RING_WIDTH = SizeUtil.dp2px(5f)
        private val DEFAULT_RING_COLOR = Color.parseColor("#0888FF")
        private val DEFAULT_RING_BACKGROUND_COLOR = Color.parseColor("#EFEFEF")
        private val DEFAULT_INNER_BACKGROUND_COLOR = Color.TRANSPARENT
        private val DEFAULT_DESC_TEXT = ""
        private val DEFAULT_DESC_TEXT_SIZE = SizeUtil.sp2px(12f)
        private val DEFAULT_DESC_TEXT_COLOR = Color.parseColor("#0888FF")
        private val DEFAULT_PERCENT_TEXT_SIZE = SizeUtil.sp2px(20f)
        private val DEFAULT_PERCENT_TEXT_COLOR = Color.parseColor("#0888FF")
    }

    //进度最大值
    var max = DEFAULT_MAX
    //当前进度
    var progress = DEFAULT_PROGRESS
        get() = field
        set(value) {
            field = correctProgress(value)
            invalidate()
        }
    //绘制开始的角度
    var startAngle = DEFAULT_START_ANGLE
    //进度条是否逆时针滚动，默认为false，即顺时针滚动
    var reverse = DEFAULT_REVERSE
    //是否设置画笔帽为Paint.Cap.ROUND，即圆形样式，默认为true
    var roundCap = DEFAULT_ROUND_CAP
    //圆环宽度
    var ringWidth = DEFAULT_RING_WIDTH
    //圆环颜色，若在代码中设置了rindColorArray则无效果
    var ringColor = DEFAULT_RING_COLOR
    //圆环渐变色，通过代码设置，实现渐变效果，优先级比ringColor高
    var rindColorArray: IntArray? = null
    //圆环背景色
    var ringBackgroungColor = DEFAULT_RING_BACKGROUND_COLOR
    //内部圆圈背景色
    var innerBackgroundColor = DEFAULT_INNER_BACKGROUND_COLOR
    //描述文本
    var descText = DEFAULT_DESC_TEXT
    //描述文本字体大小
    var descTextSize = DEFAULT_DESC_TEXT_SIZE
    //描述文本字体颜色
    var descTextColor = DEFAULT_DESC_TEXT_COLOR
    //进度文本字体大小
    var percentTextSize = DEFAULT_PERCENT_TEXT_SIZE
    //进度文本字体颜色
    var percentTextColor = DEFAULT_PERCENT_TEXT_COLOR

    //更新当前进度，带动画效果
    fun startAnim(progress: Int, duration: Long = 2000) {
        val percent = correctProgress(progress)
        val animator = ValueAnimator.ofInt(0, percent)
        animator.setDuration(duration)
        animator.addUpdateListener {
            this.progress = it.getAnimatedValue() as Int
        }
        animator.start()
    }

    private fun correctProgress(progress: Int): Int {
        var progress = progress
        if (progress > max) {  //处理错误输入progress大于max的情况
            if (progress % max == 0) {
                progress = max
            } else {
                progress %= max
            }
        }
        return progress
    }

    private lateinit var mArcPaint: Paint        //绘制圆环
    private lateinit var mBgPaint: Paint         //绘制内部圆圈
    private lateinit var mTextPaint: Paint       //绘制文字
    private lateinit var mRectF: RectF           //圆环对应的RectF
    private lateinit var mShader: SweepGradient  //实现圆环渐变效果，如果有在代码中设置rindColorArray的话

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
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
        max = ta.getInt(R.styleable.CircleProgressBar_max, DEFAULT_MAX)
        if (max <= 0) {
            max = DEFAULT_MAX
        }
        startAngle = ta.getFloat(R.styleable.CircleProgressBar_startAngle, DEFAULT_START_ANGLE)
        reverse = ta.getBoolean(R.styleable.CircleProgressBar_reverse, DEFAULT_REVERSE)
        roundCap = ta.getBoolean(R.styleable.CircleProgressBar_roundCap, DEFAULT_ROUND_CAP)
        progress = ta.getInt(R.styleable.CircleProgressBar_progress, DEFAULT_PROGRESS)
        ringWidth = ta.getDimension(R.styleable.CircleProgressBar_ringWidth, DEFAULT_RING_WIDTH)
        ringColor = ta.getColor(R.styleable.CircleProgressBar_ringColor, DEFAULT_RING_COLOR)
        ringBackgroungColor = ta.getColor(R.styleable.CircleProgressBar_ringBackgroungColor, DEFAULT_RING_BACKGROUND_COLOR)
        innerBackgroundColor = ta.getColor(R.styleable.CircleProgressBar_innerBackgroundColor, DEFAULT_INNER_BACKGROUND_COLOR)
        descText = ta.getString(R.styleable.CircleProgressBar_descText) ?: DEFAULT_DESC_TEXT
        descTextSize = ta.getDimension(R.styleable.CircleProgressBar_descTextSize, DEFAULT_DESC_TEXT_SIZE)
        descTextColor = ta.getColor(R.styleable.CircleProgressBar_descTextColor, DEFAULT_DESC_TEXT_COLOR)
        percentTextSize = ta.getDimension(R.styleable.CircleProgressBar_percentTextSize, DEFAULT_PERCENT_TEXT_SIZE)
        percentTextColor = ta.getColor(R.styleable.CircleProgressBar_percentTextColor, DEFAULT_PERCENT_TEXT_COLOR)
        ta.recycle()
    }

    //初始化画笔
    private fun initPaint() {
        mArcPaint = Paint()
        mArcPaint.isAntiAlias = true
        mBgPaint = Paint()
        with(mBgPaint) {
            isAntiAlias = true
            color = innerBackgroundColor
        }
        mTextPaint = Paint()
        with(mTextPaint) {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
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
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBackgroung(it)
            drawArc(it)
            drawText(it)
        }
    }

    //绘制背景
    private fun drawBackgroung(canvas: Canvas) {
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), mWidth / 2.0f, mBgPaint)
    }

    //绘制圆环
    private fun drawArc(canvas: Canvas) {
        with(mArcPaint) {
            color = ringBackgroungColor
            style = Paint.Style.STROKE
            strokeWidth = ringWidth
            setShader(null)
        }
        //绘制圆环背景
        canvas.drawArc(mRectF, 0f, 360f, false, mArcPaint)
        with(mArcPaint) {
            color = ringColor
            //strokeCap取值：ROUND(圆形)、SQUARE(方形)和BUTT，默认是BUTT
            //SQUARE和BUTT显示效果一样，但是SQUARE会凸出一部分，和ROUND凸出的圆角一样
            if (roundCap) {
                strokeCap = Paint.Cap.ROUND
            }
        }
        //绘制圆环
        rindColorArray?.let {
            //绘制渐变效果
            mShader = SweepGradient(mRectF.centerX(), mRectF.centerY(), rindColorArray!!, null)
            val matrix = Matrix()
            matrix.setRotate(if (roundCap) calculateOffset() else startAngle, mRectF.centerX(), mRectF.centerY())
            mShader.setLocalMatrix(matrix)
            mArcPaint.setShader(mShader)
        }
        var sweepAngle = progress.toFloat() / max * 360f
        if (reverse) {
            sweepAngle = -sweepAngle  //逆时针滚动
        }
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mArcPaint)
    }

    //设置渐变效果时让SweepGradient偏移一定角度来保证圆角凸出部分和圆环进度条颜色一样
    private fun calculateOffset(): Float {
        //计算strokeCap为Paint.Cap.ROUND时圆角凸出部分相当于整个圆环占的比例，半圆的直径 = 线的宽度
        val roundPercent = (Math.atan(ringWidth / 2.0 / (mWidth / 2.0 - ringWidth / 2.0)) / (2 * Math.PI)).toFloat()
        val curentPercent = progress / max.toFloat()  //当前进度
        if (curentPercent + roundPercent >= 1.0f) {
            return startAngle
        } else if (curentPercent + 2 * roundPercent >= 1.0f) {
//            mArcPaint.strokeCap = Paint.Cap.BUTT
            if (reverse) {
                return startAngle + (1 - curentPercent - roundPercent) * 360f
            } else {
                return startAngle - (1 - curentPercent - roundPercent) * 360f
            }
        } else {
            if (reverse) {
                return startAngle + roundPercent * 360f
            } else {
                return startAngle - roundPercent * 360f
            }
        }
    }

    //绘制文字
    private fun drawText(canvas: Canvas) {
        with(mTextPaint) {
            color = descTextColor
            textSize = descTextSize
        }
        //绘制描述文本
        canvas.drawText(descText, mRectF.centerX(), mHeight * 0.42f, mTextPaint)
        with(mTextPaint) {
            color = percentTextColor
            textSize = percentTextSize
        }
        //绘制进度文本
        canvas.drawText(
            "${(progress / max.toFloat() * 100).toInt()}%",
            mRectF.centerX(),
            mHeight * 0.69f,
            mTextPaint
        )
    }

}