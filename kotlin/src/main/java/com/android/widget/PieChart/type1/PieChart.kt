package com.android.widget.PieChart.type1

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil
import com.android.widget.PieChart.PieData

/**
 * Created by xuzhb on 2019/10/15
 * Desc:带延长线的饼状图
 */
class PieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val TOTAL_ANGLE = 360f
        private val DEFAULT_BEGIN_ANGLE = 0f
        private val DEFAULT_SHOW_INSIDE = true
        private val DEFAULT_INSIDE_RATIO = 0.5f
        private val DEFAULT_INSIDE_COLOR = Color.WHITE
        private val DEFAULT_SHOW_DIVIDER_LINE = true
        private val DEFAULT_DIVIDER_COLOR = Color.WHITE
        private val DEFAULT_DIVIDER_WIDTH = SizeUtil.dp2px(1f)
        private val DEFAULT_POINT_RADIUS = SizeUtil.dp2px(2f)
        private val DEFAULT_POINT_DISTANCE = SizeUtil.dp2px(8f)
        private val DEFAULT_POINT_LENGTH = SizeUtil.dp2px(8f)
        private val DEFAULT_LINE_WIDTH = SizeUtil.dp2px(0.5f)
        private val DEFAULT_VALUE_TEXT_SIZE = SizeUtil.sp2px(13f)
        private val DEFAULT_VALUE_TEXT_COLOR = Color.BLACK
        private val DEFAULT_VALUE_TEXT_MARGIN = SizeUtil.dp2px(3f)
        private val DEFAULT_LABEL_TEXT_SIZE = SizeUtil.sp2px(11f)
        private val DEFAULT_LABEL_TEXT_COLOR = Color.parseColor("#999999")
        private val DEFAULT_LABEL_TEXT_MARGIN = SizeUtil.dp2px(3f)
        private val DEFAULT_EMPTY_PIE_COLOR = Color.parseColor("#A3A3A3")
        private val DEFAULT_EMPTY_TEXT = "暂无数据"
        private val DEFAULT_EMPTY_TEXT_SIZE = SizeUtil.sp2px(14f)
        private val DEFAULT_EMPTY_TEXT_COLOR = Color.BLACK
        private val DEFAULT_HORIZONTAL_EXTEND = SizeUtil.dp2px(80f)
    }

    //绘制开始的角度
    var beginAngle: Float = DEFAULT_BEGIN_ANGLE

    //是否显示中间的圆圈
    var showInside: Boolean = DEFAULT_SHOW_INSIDE

    //中间圆圈占整个饼状图的比例
    var insideRatio: Float = DEFAULT_INSIDE_RATIO

    //中间圆圈的背景颜色
    var insideColor: Int = DEFAULT_INSIDE_COLOR

    //是否显示各个扇形的分界线
    var showDividerLine: Boolean = DEFAULT_SHOW_DIVIDER_LINE

    //分界线的颜色
    var dividerColor: Int = DEFAULT_DIVIDER_COLOR

    //分界线的宽度
    var dividerWidth: Float = DEFAULT_DIVIDER_WIDTH

    //延长线起始点圆圈的半径
    var pointRadius: Float = DEFAULT_POINT_RADIUS

    //延长线起始点和饼状图边缘的距离
    var pointDistance: Float = DEFAULT_POINT_DISTANCE

    //延长线起始点到延长线拐点的长度
    var pointLength: Float = DEFAULT_POINT_LENGTH

    //延长线的宽度
    var lineWidth: Float = DEFAULT_LINE_WIDTH

    //延长线上方文本的字体大小
    var valueTextSize: Float = DEFAULT_VALUE_TEXT_SIZE

    //延长线上方文本的字体颜色
    var valueTextColor: Int = DEFAULT_VALUE_TEXT_COLOR

    //延长线上方文本到延长线的边距
    var valueTextMargin: Float = DEFAULT_VALUE_TEXT_MARGIN

    //延长线下方文本的字体大小
    var labelTextSize: Float = DEFAULT_LABEL_TEXT_SIZE

    //延长线下方文本的字体颜色
    var labelTextColor: Int = DEFAULT_LABEL_TEXT_COLOR

    //延长线下方文本到延长线的边距
    var labelTextMargin: Float = DEFAULT_LABEL_TEXT_MARGIN

    //数据为空时饼图的颜色
    var emptyPieColor: Int = DEFAULT_EMPTY_PIE_COLOR

    //数据为空时的文本描述
    var emptyText: String = DEFAULT_EMPTY_TEXT

    //数据为空时的文本字体大小
    var emptyTextSize: Float = DEFAULT_EMPTY_TEXT_SIZE

    //数据为空时的文本字体颜色
    var emptyTextColor: Int = DEFAULT_EMPTY_TEXT_COLOR

    //饼图左右两侧的预留空间，用来绘制延长线和文本
    var horizontalExtend: Float = DEFAULT_HORIZONTAL_EXTEND

    private var mPiePaint: Paint        //绘制各个扇形
    private var mCirclePaint: Paint     //绘制圆圈，包括饼状图中间的圆圈和延长线起始点的圆圈
    private var mDividerPaint: Paint    //绘制各个扇形的分界线
    private var mLinePaint: Paint       //绘制延长线
    private var mValueTextPaint: Paint  //绘制延长线上方文本
    private var mLabelTextPaint: Paint  //绘制延长线下方文本
    private lateinit var mRectF: RectF  //饼状图对应的矩形RectF

    private var mCenterX: Float = 0f
    private var mCenterY: Float = 0f
    private var mWidth: Float = 0f
    private var mHeight: Float = 0f
    private var mRadius: Float = 0f     //饼状图的半径
    private var mProgress: Float = beginAngle + TOTAL_ANGLE       //开启动画时饼状图绘制的进度
    private var mIsDataEmpty: Boolean = false  //数据是否为空
    private var mNotEmptyItemCount: Int = 0    //不为0的数据个数

    private var mDataList: MutableList<PieData> = mutableListOf() //填充的数据
    private var mPieRatio: MutableList<Float> = mutableListOf()   //各个扇形所占的比例

    //避免延长线上的文本相互遮挡
    private var mLastYTop: Float = -1f  //前一个延长线文本高度的顶部Y坐标
    private var mLastYBottom: Float = -1f  //前一个延长线文本高度底部Y坐标
    private var mLastX: Float = -1f  //前一个延长线文本X坐标

    init {
        attrs?.let {
            //读取布局属性和设置默认值
            val ta = getContext().obtainStyledAttributes(attrs, R.styleable.PieChart)
            beginAngle = ta.getFloat(R.styleable.PieChart_beginAngle, DEFAULT_BEGIN_ANGLE)
            showInside = ta.getBoolean(R.styleable.PieChart_showInside, DEFAULT_SHOW_INSIDE)
            insideRatio = ta.getFloat(R.styleable.PieChart_insideRatio, DEFAULT_INSIDE_RATIO)
            if (insideRatio < 0) {
                insideRatio = 0f
            }
            if (insideRatio > 1) {
                insideRatio = 1f
            }
            insideColor = ta.getColor(R.styleable.PieChart_insideColor, DEFAULT_INSIDE_COLOR)
            showDividerLine = ta.getBoolean(R.styleable.PieChart_showDividerLine, DEFAULT_SHOW_DIVIDER_LINE)
            dividerColor = ta.getColor(R.styleable.PieChart_dividerColor, DEFAULT_DIVIDER_COLOR)
            dividerWidth = ta.getDimension(R.styleable.PieChart_dividerWidth, DEFAULT_DIVIDER_WIDTH)
            pointRadius = ta.getDimension(R.styleable.PieChart_pointRadius, DEFAULT_POINT_RADIUS)
            pointDistance = ta.getDimension(R.styleable.PieChart_pointDistance, DEFAULT_POINT_DISTANCE)
            pointLength = ta.getDimension(R.styleable.PieChart_pointLength, DEFAULT_POINT_LENGTH)
            lineWidth = ta.getDimension(R.styleable.PieChart_lineWidth, DEFAULT_LINE_WIDTH)
            valueTextSize = ta.getDimension(R.styleable.PieChart_valueTextSize, DEFAULT_VALUE_TEXT_SIZE)
            valueTextColor = ta.getColor(R.styleable.PieChart_valueTextColor, DEFAULT_VALUE_TEXT_COLOR)
            valueTextMargin = ta.getDimension(R.styleable.PieChart_valueTextMargin, DEFAULT_VALUE_TEXT_MARGIN)
            labelTextSize = ta.getDimension(R.styleable.PieChart_labelTextSize, DEFAULT_LABEL_TEXT_SIZE)
            labelTextColor = ta.getColor(R.styleable.PieChart_labelTextColor, DEFAULT_LABEL_TEXT_COLOR)
            labelTextMargin = ta.getDimension(R.styleable.PieChart_labelTextMargin, DEFAULT_LABEL_TEXT_MARGIN)
            emptyPieColor = ta.getColor(R.styleable.PieChart_emptyPieColor, DEFAULT_EMPTY_PIE_COLOR)
            emptyText = ta.getString(R.styleable.PieChart_emptyText) ?: DEFAULT_EMPTY_TEXT
            emptyTextSize = ta.getDimension(R.styleable.PieChart_emptyTextSize, DEFAULT_EMPTY_TEXT_SIZE)
            emptyTextColor = ta.getColor(R.styleable.PieChart_emptyTextColor, DEFAULT_EMPTY_TEXT_COLOR)
            ta.recycle()
        }

        //初始化画笔
        mPiePaint = Paint()
        mPiePaint.isAntiAlias = true
        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mDividerPaint = Paint()
        with(mDividerPaint) {
            isAntiAlias = true
            color = dividerColor
            strokeWidth = dividerWidth
        }
        mLinePaint = Paint()
        with(mLinePaint) {
            isAntiAlias = true
            strokeWidth = lineWidth
        }
        mValueTextPaint = Paint()
        with(mValueTextPaint) {
            isAntiAlias = true
            color = valueTextColor
            textSize = valueTextSize
        }
        mLabelTextPaint = Paint()
        with(mLabelTextPaint) {
            isAntiAlias = true
            color = labelTextColor
            textSize = labelTextSize
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var width = 0
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width = SizeUtil.dp2px(TOTAL_ANGLE).toInt()
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize)
            }
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = SizeUtil.dp2px(240f).toInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initSize(w, h)
    }

    //初始化尺寸信息
    private fun initSize(width: Int, height: Int) {
        mWidth = width.toFloat()
        mHeight = height.toFloat()
        mCenterX = width / 2f
        mCenterY = height / 2f
        val valueFm = mValueTextPaint.getFontMetrics()
        //饼图上方最大预留空间，用来绘制延长线和文本
        val topExtend = valueFm.descent - valueFm.ascent + valueTextMargin + pointDistance + pointLength
        val labelFm = mLabelTextPaint.getFontMetrics()
        //饼图下方最大预留空间，用来绘制延长线和文本
        val bottomExtend = labelFm.descent - labelFm.ascent + labelTextMargin + pointDistance + pointLength
        val maxVertivalExtend = if (topExtend > bottomExtend) topExtend else bottomExtend
        val pieWidth = width - 2 * horizontalExtend
        val pieHeight = height - 2 * maxVertivalExtend
        mRadius = if (pieWidth > pieHeight) pieHeight / 2f else pieWidth / 2f
        mRectF = RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawPie(it)
            drawInside(it)
        }
    }

    //绘制扇形
    private fun drawPie(canvas: Canvas) {
        if (mIsDataEmpty) {  //暂无数据
            mPiePaint.color = emptyPieColor
            canvas.drawArc(mRectF, beginAngle, mProgress - beginAngle, true, mPiePaint)
            with(mValueTextPaint) {
                color = emptyTextColor
                textSize = emptyTextSize
                textAlign = Paint.Align.RIGHT
            }
            val fm = mValueTextPaint.getFontMetrics()
            canvas.drawText(emptyText, mWidth, -fm.top, mValueTextPaint)
            return
        }
        var startAngle = beginAngle
        for (i in mDataList.indices) {
            var sweepAngle = 0f
            if (i != mDataList.size - 1) {
                sweepAngle = mPieRatio[i] * TOTAL_ANGLE
            } else {
                sweepAngle = beginAngle + TOTAL_ANGLE + -startAngle
            }
            //跳过占比为0%的部分
            if (sweepAngle == 0f) {
                continue
            }
            mPiePaint.color = mDataList.get(i).color
            if (mProgress - startAngle >= 0) {
                //绘制各个扇形
                canvas.drawArc(mRectF, startAngle, mProgress - startAngle, true, mPiePaint)
                //每段扇形中心点所对应的角度
                val centerAngle = startAngle + sweepAngle / 2f
                //绘制延长线和文字
                drawLineAndText(canvas, centerAngle, mDataList.get(i))
                //每段扇形最终的角度
                val endAngle = startAngle + sweepAngle
                //绘制各个扇形分界线
                drawPieDivider(canvas, startAngle, endAngle)
            }
            startAngle += sweepAngle
        }
    }

    //绘制内部的圆圈
    private fun drawInside(canvas: Canvas) {
        if (showInside) {
            mCirclePaint.color = insideColor
            canvas.drawCircle(mCenterX, mCenterY, mRadius * insideRatio, mCirclePaint)
        }
    }

    //绘制延长线和文字
    private fun drawLineAndText(canvas: Canvas, centerAngle: Float, data: PieData) {
        mCirclePaint.color = data.color
        mLinePaint.color = data.color
        //(x1,y1):延长线起始点;(x2,y2):延长线拐点
        val x1 = (mCenterX + Math.cos(Math.toRadians(centerAngle.toDouble())) * (mRadius + pointDistance)).toFloat()
        val y1 = (mCenterY + Math.sin(Math.toRadians(centerAngle.toDouble())) * (mRadius + pointDistance)).toFloat()
        val x2 =
            (mCenterX + Math.cos(Math.toRadians(centerAngle.toDouble())) * (mRadius + pointDistance + pointLength)).toFloat()
        val y2 =
            (mCenterY + Math.sin(Math.toRadians(centerAngle.toDouble())) * (mRadius + pointDistance + pointLength)).toFloat()
        val valueRect = Rect()
        mValueTextPaint.getTextBounds(formatValue(data.value), 0, formatValue(data.value).length, valueRect)
        val yTop = y2 - valueRect.height() - valueTextMargin
        val labelRect = Rect()
        mLabelTextPaint.getTextBounds(data.label, 0, data.label.length, labelRect)
        val yBottom = y2 + labelRect.height() + labelTextMargin
        val xPos = getLastX(centerAngle)
        if (mLastYTop != -1f && mLastYBottom != -1f && xPos != -1f) {
            //判断延长线的文本是否相互遮挡
            if (isIntersect(mLastYTop, mLastYBottom, mLastX, yTop, yBottom, xPos)) {
                return
            }
        }
        mLastX = xPos
        mLastYTop = yTop
        mLastYBottom = yBottom
        when (Math.abs(centerAngle % 360)) {
            //第四象限
            in 0f..90f -> {
                if (mProgress >= centerAngle) {
                    canvas.drawCircle(x1, y1, pointRadius, mCirclePaint)  //绘制延长线起始点的圆圈
                    canvas.drawLine(x1, y1, x2, y2, mLinePaint)  //绘制延长线起始点到拐点的直线
                    canvas.drawLine(x2, y2, mWidth, y2, mLinePaint)  //绘制延长线拐点到View边缘的直线
                    mValueTextPaint.textAlign = Paint.Align.RIGHT
                    //绘制延长线上方的文字
                    canvas.drawText(formatValue(data.value), mWidth, y2 - valueTextMargin, mValueTextPaint)
                    mLabelTextPaint.textAlign = Paint.Align.RIGHT
                    val labelFm = mLabelTextPaint.getFontMetrics()
                    //绘制延长线下方的文字
                    canvas.drawText(
                        data.label,
                        mWidth,
                        y2 + labelTextMargin + labelRect.height() / 2f + (labelFm.descent - labelFm.ascent) / 2f - labelFm.descent,
                        mLabelTextPaint
                    )
                }
            }
            //第三象限
            in 90f..180f -> {
                if (mProgress >= centerAngle) {
                    canvas.drawCircle(x1, y1, pointRadius, mCirclePaint)
                    canvas.drawLine(x1, y1, x2, y2, mLinePaint)
                    canvas.drawLine(x2, y2, 0f, y2, mLinePaint)
                    mValueTextPaint.textAlign = Paint.Align.LEFT
                    canvas.drawText(formatValue(data.value), 0f, y2 - valueTextMargin, mValueTextPaint)
                    mLabelTextPaint.textAlign = Paint.Align.LEFT
                    val labelFm = mLabelTextPaint.getFontMetrics()
                    canvas.drawText(
                        data.label,
                        0f,
                        y2 + labelTextMargin + labelRect.height() / 2f + (labelFm.descent - labelFm.ascent) / 2f - labelFm.descent,
                        mLabelTextPaint
                    )
                }
            }
            //第二象限
            in 180f..270f -> {
                if (mProgress >= centerAngle) {
                    canvas.drawCircle(x1, y1, pointRadius, mCirclePaint)
                    canvas.drawLine(x1, y1, x2, y2, mLinePaint)
                    canvas.drawLine(x2, y2, 0f, y2, mLinePaint)
                    mValueTextPaint.textAlign = Paint.Align.LEFT
                    canvas.drawText(formatValue(data.value), 0f, y2 - valueTextMargin, mValueTextPaint)
                    mLabelTextPaint.textAlign = Paint.Align.LEFT
                    val labelFm = mLabelTextPaint.getFontMetrics()
                    canvas.drawText(
                        data.label,
                        0f,
                        y2 + labelTextMargin + labelRect.height() / 2f + (labelFm.descent - labelFm.ascent) / 2f - labelFm.descent,
                        mLabelTextPaint
                    )
                }
            }
            //第一象限
            in 270f..360f -> {
                if (mProgress >= centerAngle) {
                    canvas.drawCircle(x1, y1, pointRadius, mCirclePaint)
                    canvas.drawLine(x1, y1, x2, y2, mLinePaint)
                    canvas.drawLine(x2, y2, mWidth, y2, mLinePaint)
                    mValueTextPaint.textAlign = Paint.Align.RIGHT
                    canvas.drawText(formatValue(data.value), mWidth, y2 - valueTextMargin, mValueTextPaint)
                    mLabelTextPaint.textAlign = Paint.Align.RIGHT
                    val labelFm = mLabelTextPaint.getFontMetrics()
                    canvas.drawText(
                        data.label,
                        mWidth,
                        y2 + labelTextMargin + labelRect.height() / 2f + (labelFm.descent - labelFm.ascent) / 2f - labelFm.descent,
                        mLabelTextPaint
                    )
                }
            }
        }
    }

    //判断文本是否相交
    private fun isIntersect(
        y1Top: Float,
        y1Bottom: Float,
        x1: Float,
        y2Top: Float,
        y2Bottom: Float,
        x2: Float
    ): Boolean {
        return (y1Top < y2Bottom && y1Top > y2Top && x1 == x2) || (y2Top < y1Bottom && y2Top > y1Top && x1 == x2)
    }

    private fun getLastX(centerAngle: Float): Float {
        val angle = Math.abs(centerAngle % 360)
        if (angle > 90 && angle <= 270) {
            return 0f
        }
        return mWidth
    }

    //绘制各个扇形的分界线
    private fun drawPieDivider(canvas: Canvas, startAngle: Float, endAngle: Float) {
        for (item in mPieRatio) {
            val rate = item
            //当某扇形的占比非常小，以至于比分界线更小时不绘制分界线
            val minRate =
                Math.toDegrees(Math.asin((dividerWidth / (mRadius * insideRatio)).toDouble())) / 180f
            if (rate <= minRate && rate != 0f) {
                showDividerLine = false
            }
        }
        if (!mIsDataEmpty && mNotEmptyItemCount > 1) {  //两条或两条以上数据才绘制分界线
            if (showDividerLine && startAngle < mProgress) {
                val x1 = (mCenterX + Math.cos(Math.toRadians(startAngle.toDouble())) * mRadius).toFloat()
                val y1 = (mCenterY + Math.sin(Math.toRadians(startAngle.toDouble())) * mRadius).toFloat()
                canvas.drawLine(mCenterX, mCenterY, x1, y1, mDividerPaint)
            }
            if (showDividerLine && mProgress >= endAngle) {
                val x2 = (mCenterX + Math.cos(Math.toRadians(endAngle.toDouble())) * mRadius).toFloat()
                val y2 = (mCenterY + Math.sin(Math.toRadians(endAngle.toDouble())) * mRadius).toFloat()
                canvas.drawLine(mCenterX, mCenterY, x2, y2, mDividerPaint)
            }
        }
    }

    private fun formatValue(value: Float): String = String.format("%.2f", value)


    //设置数据
    fun setData(list: MutableList<PieData>) {
        this.mDataList = list
        var sum = 0f
        mNotEmptyItemCount = 0
        mIsDataEmpty = true
        mPieRatio.clear()
        for (item in list) {
            if (item.value != 0f) {
                mNotEmptyItemCount++
            }
            sum += item.value  //计算总和
        }
        if (sum > 0f) {
            mIsDataEmpty = false
        }
        if (!mIsDataEmpty) {
            for (item in list) {
                val rate = item.value / sum
                mPieRatio.add(rate)
            }
        }
        invalidate()
    }

    /**
     * 加载动画
     *
     * @param isNeedAnimation 是否加载动画
     * @param duration 动画时长
     */
    fun startAnimation(isNeedAnimation: Boolean, duration: Long = 2000) {
        if (isNeedAnimation) {
            val animator = ValueAnimator.ofFloat(0f, TOTAL_ANGLE)
            with(animator) {
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    mProgress = beginAngle + it.animatedValue as Float
                    invalidate()
                }
                setDuration(duration)
                start()
            }
        } else {
            mProgress = beginAngle + TOTAL_ANGLE
        }
    }

}