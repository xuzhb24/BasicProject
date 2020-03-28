package com.android.widget.LineChart.type2

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/3/27
 * Desc:双折线图
 */
class LineChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_MAX_YVALUE = 5000
        private val DEFAULT_YLABEL_COUNT = 4
        private val DEFAULT_XLABEL_TEXT_SIZE = SizeUtil.sp2px(10f)
        private val DEFAULT_XLABEL_TEXT_COLOR = Color.parseColor("#999999")
        private val DEFAULT_XLABEL_TEXT_MARGIN_TOP = SizeUtil.dp2px(10f)
        private val DEFAULT_SHOW_YLABEL_TEXT = false
        private val DEFAULT_YLABEL_TEXT_SIZE = SizeUtil.sp2px(11f)
        private val DEFAULT_YLABEL_TEXT_COLOR = Color.BLACK
        private val DEFAULT_YLABEL_TEXT_MARGIN_LEFT = SizeUtil.dp2px(15f)
        private val DEFAULT_AXIS_WIDTH = SizeUtil.dp2px(0.5f)
        private val DEFAULT_AXIS_COLOR = Color.parseColor("#F1F1F1")
        private val DEFAULT_SHOW_SCALE = true
        private val DEFAULT_SCALE_LENGTH = SizeUtil.dp2px(4f)
        private val DEFAULT_SHOW_GRID = true
        private val DEFAULT_GRID_WIDTH = SizeUtil.dp2px(0.5f)
        private val DEFAULT_GRID_DASH_INTERVAL = SizeUtil.dp2px(1f)
        private val DEFAULT_GRID_DASH_LENGTH = SizeUtil.dp2px(2f)
        private val DEFAULT_GRID_COLOR = Color.parseColor("#F1F1F1")
        private val DEFAULT_LINE_WIDTH = SizeUtil.dp2px(1.5f)
        private val DEFAULT_LINE_COLOR1 = Color.parseColor("#60BF56")
        private val DEFAULT_LINE_COLOR2 = Color.parseColor("#108EE9")
        private val DEFAULT_LABEL_WIDTH = SizeUtil.dp2px(135f)
        private val DEFAULT_LABEL_HEIGHT = SizeUtil.dp2px(78f)
        private val DEFAULT_LABEL_BACKGROUND_COLOR = Color.WHITE
        private val DEFAULT_LABEL_RADIUS = SizeUtil.dp2px(3f)
        private val DEFAULT_LABEL_TEXT_SIZE = SizeUtil.sp2px(11f)
        private val DEFAULT_LABEL_TEXT_COLOR = Color.parseColor("#333333")
        private val DEFAULT_LABEL_ARROW_WIDTH = SizeUtil.dp2px(8f)
        private val DEFAULT_LABEL_ARROW_HEIGHT = SizeUtil.dp2px(2f)
        private val DEFAULT_LABEL_ARROW_OFFSET = SizeUtil.dp2px(31f)
        private val DEFAULT_LABEL_ARROW_MARGIN = SizeUtil.dp2px(14.5f)
        private val DEFAULT_CLICKABLE = true
        private val DEFAULT_LEFT_MARGIN = SizeUtil.dp2px(15f)
        private val DEFAULT_TOP_MARGIN = SizeUtil.dp2px(118f)
        private val DEFAULT_RIGHT_MARGIN = SizeUtil.dp2px(15f)
        private val DEFAULT_BOTTOM_MARGIN = SizeUtil.dp2px(70f)
    }

    //Y轴最大值
    var maxYValue: Int = DEFAULT_MAX_YVALUE
    //Y轴上的刻度值个数
    var yLabelCount: Int = DEFAULT_YLABEL_COUNT
    //X轴刻度值文本字体大小
    var xLabelTextSize: Float = DEFAULT_XLABEL_TEXT_SIZE
    //X轴刻度值文本字体颜色
    var xLabelTextColor: Int = DEFAULT_XLABEL_TEXT_COLOR
    //X轴刻度值文本到X轴的上边距
    var xLabelTextMarginTop: Float = DEFAULT_XLABEL_TEXT_MARGIN_TOP
    //是否显示Y轴刻度值文本
    var showYLabelText: Boolean = DEFAULT_SHOW_YLABEL_TEXT
    //Y轴刻度值文本字体大小
    var yLabelTextSize: Float = DEFAULT_YLABEL_TEXT_SIZE
    //Y轴刻度值文本字体颜色
    var yLabelTextColor: Int = DEFAULT_YLABEL_TEXT_COLOR
    //Y轴刻度值文本到屏幕左侧的左边距
    var yLabelTextMarginLeft: Float = DEFAULT_YLABEL_TEXT_MARGIN_LEFT
    //X轴宽度
    var axisWidth: Float = DEFAULT_AXIS_WIDTH
    //X轴颜色
    var axisColor: Int = DEFAULT_AXIS_COLOR
    //是否显示轴线上的小刻度线，默认显示
    var showScale: Boolean = DEFAULT_SHOW_SCALE
    //X轴上的小刻度线长度
    var scaleLength: Float = DEFAULT_SCALE_LENGTH
    //是否显示网格，默认显示
    var showGrid: Boolean = DEFAULT_SHOW_GRID
    //网格线宽度
    var gridWidth: Float = DEFAULT_GRID_WIDTH
    //网格线组成虚线的线段之间的间隔
    var gridDashInterval: Float = DEFAULT_GRID_DASH_INTERVAL
    //网格线组成虚线的线段长度
    var gridDashLength: Float = DEFAULT_GRID_DASH_LENGTH
    //网格线颜色
    var gridColor: Int = DEFAULT_GRID_COLOR
    //折线宽度
    var lineWidth: Float = DEFAULT_LINE_WIDTH
    //折线一颜色
    var lineColor1: Int = DEFAULT_LINE_COLOR1
    //折线二颜色
    var lineColor2: Int = DEFAULT_LINE_COLOR2
    //标签的矩形宽度
    var labelWidth: Float = DEFAULT_LABEL_WIDTH
    //标签的矩形高度
    var labelHeight: Float = DEFAULT_LABEL_HEIGHT
    //标签背景颜色
    var labelBackgroundColor = DEFAULT_LABEL_BACKGROUND_COLOR
    //标签的矩形圆角
    var labelRadius: Float = DEFAULT_LABEL_RADIUS
    //标签内文本字体大小
    var labelTextSize: Float = DEFAULT_LABEL_TEXT_SIZE
    //标签内文本字体颜色
    var labelTextColor: Int = DEFAULT_LABEL_TEXT_COLOR
    //标签的箭头宽度
    var labelArrowWidth: Float = DEFAULT_LABEL_ARROW_WIDTH
    //标签的箭头高度
    var labelArrowHeight: Float = DEFAULT_LABEL_ARROW_HEIGHT
    //标签的箭头到标签左侧或右侧的偏移量
    var labelArrowOffset: Float = DEFAULT_LABEL_ARROW_OFFSET
    //标签的箭头到坐标轴最上方的下边距
    var labelArrowMargin: Float = DEFAULT_LABEL_ARROW_MARGIN
    //是否可点击
    var clickAble: Boolean = DEFAULT_CLICKABLE
    //坐标轴到View左侧的边距，多出来的空间可以用来绘制Y轴刻度文本
    var leftMargin: Float = DEFAULT_LEFT_MARGIN
    //坐标轴到View顶部的边距，多出来的空间可以用来绘制标签信息
    var topMargin: Float = DEFAULT_TOP_MARGIN
    //坐标轴到View右侧的边距
    var rightMargin: Float = DEFAULT_RIGHT_MARGIN
    //坐标轴到View底部的边距，多出来的空间可以用来绘制X轴刻度文本
    var bottomMargin: Float = DEFAULT_BOTTOM_MARGIN

    private var mCurrentDrawIndex = 0

    private lateinit var mAxisPaint: Paint     //绘制轴线和轴线上的小刻度线
    private lateinit var mGridPaint: Paint     //绘制网格线
    private lateinit var mLinePaint: Paint     //绘制折线
    private lateinit var mLabelPaint: Paint    //绘制最上方标签
    private lateinit var mLabelBgPaint: Paint  //绘制标签背景，带阴影效果
    private lateinit var mTextPaint: Paint     //绘制文本
    private lateinit var mLabelRectF: RectF    //最上方的标签对应的矩形

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mXPoint: Float = 0f   //原点的X坐标
    private var mYPoint: Float = 0f   //原点的Y坐标
    private var mXScale: Float = 0f   //X轴刻度长度
    private var mYScale: Float = 0f   //Y轴刻度长度
    private var mXLength: Float = 0f  //X轴长度
    private var mYLength: Float = 0f  //Y轴长度
    private var mClickIndex: Int = 0  //点击时的下标

    private var mDataList1: MutableList<Float> = mutableListOf()     //折线一(交易收益)对应数据
    private var mDataList2: MutableList<Float> = mutableListOf()     //折线二(返现收益)对应数据
    //记录每个数据点的X、Y坐标
    private var mDataPointList1: MutableList<PointF> = mutableListOf()
    private var mDataPointList2: MutableList<PointF> = mutableListOf()
    private var mXLabelList: MutableList<String> = mutableListOf()  //X轴刻度值
    private var mYLabelList: MutableList<String> = mutableListOf()  //Y轴刻度值

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)  //关闭硬件加速，解决在部分手机无法实现虚线效果
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
        setYLable()
    }

    //初始化Y轴刻度值
    private fun setYLable() {
        mYLabelList.clear()
        val increment = maxYValue / yLabelCount.toFloat()
        for (i in 0..yLabelCount) {
            var text = ""
            if (i == 0) {
                text = "0"
            } else {
                val value = (increment * i * 100).toInt() / 100f
                if (value == value.toInt().toFloat()) {
                    text = value.toInt().toString()
                } else {
                    text = value.toString()
                }
            }
            mYLabelList.add(text)
        }
    }

    //获取布局属性并设置属性默认值
    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LineChart)
        maxYValue = ta.getInt(R.styleable.LineChart_maxYValue, DEFAULT_MAX_YVALUE)
        yLabelCount = ta.getInt(R.styleable.LineChart_yLabelCount, DEFAULT_YLABEL_COUNT)
        xLabelTextSize = ta.getDimension(R.styleable.LineChart_xLabelTextSize, DEFAULT_XLABEL_TEXT_SIZE)
        xLabelTextColor = ta.getColor(R.styleable.LineChart_xLabelTextColor, DEFAULT_XLABEL_TEXT_COLOR)
        xLabelTextMarginTop = ta.getDimension(R.styleable.LineChart_xLabelTextMarginTop, DEFAULT_XLABEL_TEXT_MARGIN_TOP)
        showYLabelText = ta.getBoolean(R.styleable.LineChart_showYLabelText, DEFAULT_SHOW_YLABEL_TEXT)
        yLabelTextSize = ta.getDimension(R.styleable.LineChart_yLabelTextSize, DEFAULT_YLABEL_TEXT_SIZE)
        yLabelTextColor = ta.getColor(R.styleable.LineChart_yLabelTextColor, DEFAULT_YLABEL_TEXT_COLOR)
        yLabelTextMarginLeft = ta.getDimension(R.styleable.LineChart_yLabelTextMarginLeft, DEFAULT_YLABEL_TEXT_MARGIN_LEFT)
        axisWidth = ta.getDimension(R.styleable.LineChart_axisWidth, DEFAULT_AXIS_WIDTH)
        axisColor = ta.getColor(R.styleable.LineChart_axisColor, DEFAULT_AXIS_COLOR)
        showScale = ta.getBoolean(R.styleable.LineChart_showScale, DEFAULT_SHOW_SCALE)
        scaleLength = ta.getDimension(R.styleable.LineChart_scaleLength, DEFAULT_SCALE_LENGTH)
        showGrid = ta.getBoolean(R.styleable.LineChart_showGrid, DEFAULT_SHOW_GRID)
        gridWidth = ta.getDimension(R.styleable.LineChart_gridWidth, DEFAULT_GRID_WIDTH)
        gridDashInterval = ta.getDimension(R.styleable.LineChart_gridDashInterval, DEFAULT_GRID_DASH_INTERVAL)
        gridDashLength = ta.getDimension(R.styleable.LineChart_gridDashLength, DEFAULT_GRID_DASH_LENGTH)
        gridColor = ta.getColor(R.styleable.LineChart_gridColor, DEFAULT_GRID_COLOR)
        lineWidth = ta.getDimension(R.styleable.LineChart_lineWidth, DEFAULT_LINE_WIDTH)
        lineColor1 = ta.getColor(R.styleable.LineChart_lineColor1, DEFAULT_LINE_COLOR1)
        lineColor2 = ta.getColor(R.styleable.LineChart_lineColor2, DEFAULT_LINE_COLOR2)
        labelWidth = ta.getDimension(R.styleable.LineChart_labelWidth, DEFAULT_LABEL_WIDTH)
        labelHeight = ta.getDimension(R.styleable.LineChart_labelHeight, DEFAULT_LABEL_HEIGHT)
        labelBackgroundColor = ta.getColor(R.styleable.LineChart_labelBackgroundColor, DEFAULT_LABEL_BACKGROUND_COLOR)
        labelRadius = ta.getDimension(R.styleable.LineChart_labelRadius, DEFAULT_LABEL_RADIUS)
        labelTextSize = ta.getDimension(R.styleable.LineChart_labelTextSize, DEFAULT_LABEL_TEXT_SIZE)
        labelTextColor = ta.getColor(R.styleable.LineChart_labelTextColor, DEFAULT_LABEL_TEXT_COLOR)
        labelArrowWidth = ta.getDimension(R.styleable.LineChart_labelArrowWidth, DEFAULT_LABEL_ARROW_WIDTH)
        labelArrowHeight = ta.getDimension(R.styleable.LineChart_labelArrowHeight, DEFAULT_LABEL_ARROW_HEIGHT)
        labelArrowOffset = ta.getDimension(R.styleable.LineChart_labelArrowMargin, DEFAULT_LABEL_ARROW_OFFSET)
        labelArrowMargin = ta.getDimension(R.styleable.LineChart_labelArrowMargin, DEFAULT_LABEL_ARROW_MARGIN)
        clickAble = ta.getBoolean(R.styleable.LineChart_clickAble, DEFAULT_CLICKABLE)
        leftMargin = ta.getDimension(R.styleable.LineChart_leftMargin, DEFAULT_LEFT_MARGIN)
        topMargin = ta.getDimension(R.styleable.LineChart_topMargin, DEFAULT_TOP_MARGIN)
        rightMargin = ta.getDimension(R.styleable.LineChart_rightMargin, DEFAULT_RIGHT_MARGIN)
        bottomMargin = ta.getDimension(R.styleable.LineChart_bottomMargin, DEFAULT_BOTTOM_MARGIN)
        ta.recycle()
    }

    //初始化画笔
    private fun initPaint() {
        mAxisPaint = Paint()
        with(mAxisPaint) {
            isAntiAlias = true
            color = axisColor
            strokeWidth = axisWidth
        }
        mGridPaint = Paint()
        with(mGridPaint) {
            isAntiAlias = true
            color = gridColor
            strokeWidth = gridWidth
            setPathEffect(DashPathEffect(floatArrayOf(gridDashLength, gridDashInterval), 0f))  //设置虚线效果
        }
        mLinePaint = Paint()
        with(mLinePaint) {
            isAntiAlias = true
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
        }
        mLabelPaint = Paint()
        with(mLabelPaint) {
            isAntiAlias = true
        }
        mLabelBgPaint = Paint()
        with(mLabelBgPaint) {
            isAntiAlias = true
            color = labelBackgroundColor
        }
        mTextPaint = Paint()
        with(mTextPaint) {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        mLabelRectF = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = SizeUtil.dp2px(308f).toInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(measuredWidth, height)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.getX() ?: 0f
        for (i in 0..mDataPointList1.size - 1) {
            val centerX = mDataPointList1[i].x
            var beginX = centerX - mXScale / 2f
            var endX = centerX + mXScale / 2f
            if (i == 0) {
                beginX = 0f
            }
            if (i == mDataPointList1.size - 1) {
                endX = mWidth.toFloat()
            }
            if (beginX < touchX && touchX < endX) {
                mClickIndex = i
                invalidate()
                break
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            initSize(width, height)    //初始化尺寸信息
            drawCoordinate(it)         //绘制坐标轴
            drawLine(it)               //绘制折线
            drawLabel(it)              //绘制点击后的效果
            drawBottomDescription(it)  //绘制底部类型说明
        }
    }

    //初始化尺寸信息
    private fun initSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        mXLength = mWidth - leftMargin - rightMargin
        mYLength = mHeight - topMargin - bottomMargin
        mXPoint = leftMargin
        mYPoint = mHeight - bottomMargin
        mXScale = mXLength / (mXLabelList.size - 1)
        mYScale = mYLength / yLabelCount
        mDataPointList1.clear()
        if (hasOnlyOneData()) {
            mDataPointList1.add(PointF(mXPoint + mXLength / 2f, calculateYPosition(mDataList1.get(0))))  //居中
        } else {
            for (i in 0..mDataList1.size - 1) {
                mDataPointList1.add(PointF(mXPoint + i * mXScale, calculateYPosition(mDataList1.get(i))))
            }
        }
        mDataPointList2.clear()
        if (hasOnlyOneData()) {
            mDataPointList2.add(PointF(mXPoint + mXLength / 2f, calculateYPosition(mDataList2.get(0))))  //居中
        } else {
            for (i in 0..mDataList2.size - 1) {
                mDataPointList2.add(PointF(mXPoint + i * mXScale, calculateYPosition(mDataList2.get(i))))
            }
        }
    }

    //绘制坐标轴
    private fun drawCoordinate(canvas: Canvas) {
        //绘制X轴
        canvas.drawLine(mXPoint - axisWidth / 2f, mYPoint, mXPoint + mXLength + axisWidth / 2f, mYPoint, mAxisPaint)
        with(mTextPaint) {
            textSize = xLabelTextSize
            color = xLabelTextColor
        }
        val fm = mTextPaint.getFontMetrics()
        val yOffset = mYPoint + xLabelTextMarginTop - fm.ascent
        for (i in 0..mXLabelList.size - 1) {
            //绘制X轴的刻度值文本
            if (i == 0) {  //第一个刻度值文本
                if (hasOnlyOneData()) {  //只有一条数据时居中显示
                    mTextPaint.textAlign = Paint.Align.CENTER
                    canvas.drawText(mXLabelList[i], mDataPointList1[i].x, yOffset, mTextPaint)
                } else {
                    mTextPaint.textAlign = Paint.Align.LEFT
                    canvas.drawText(mXLabelList[i], mXPoint, yOffset, mTextPaint)
                }
            } else if (i == mXLabelList.size - 1) {  //最后一个刻度值文本
                mTextPaint.textAlign = Paint.Align.RIGHT
                canvas.drawText(mXLabelList[i], mXPoint + mXLength, yOffset, mTextPaint)
            } else {
                mTextPaint.textAlign = Paint.Align.CENTER
                canvas.drawText(mXLabelList[i], mXPoint + i * mXScale, yOffset, mTextPaint)
            }
            //绘制X轴上的小刻度线
            if (showScale) {
                canvas.drawLine(
                    mXPoint + i * mXScale,
                    mYPoint,
                    mXPoint + i * mXScale,
                    mYPoint - scaleLength,
                    mAxisPaint
                )
            }
        }
        for (i in 0..yLabelCount - 1) {
            //绘制网格线：横刻线
            if (showGrid) {
                mGridPaint.color = gridColor
                canvas.drawLine(
                    mXPoint,
                    mYPoint - (i + 1) * mYScale,
                    mXPoint + mXLength,
                    mYPoint - (i + 1) * mYScale,
                    mGridPaint
                )
            }
            //绘制Y轴上的刻度值
            if (showYLabelText) {
                with(mTextPaint) {
                    textSize = yLabelTextSize
                    color = yLabelTextColor
                    textAlign = Paint.Align.LEFT
                }
                if (i == 0) {
                    canvas.drawText(mYLabelList[i], yLabelTextMarginLeft, mYPoint, mTextPaint)
                }
                val yLabelFm = mTextPaint.getFontMetrics()
                val yLabelYOffset = mYPoint + (yLabelFm.descent - yLabelFm.ascent) / 2f - yLabelFm.descent - (i + 1) * mYScale
                canvas.drawText(mYLabelList[i + 1], yLabelTextMarginLeft, yLabelYOffset, mTextPaint)
            }
        }
    }

    //绘制折线
    private fun drawLine(canvas: Canvas) {
        if (mDataList1 == null || mDataList1.size <= 0 || mDataList2 == null || mDataList2.size <= 0) {
            return
        }
        if (hasOnlyOneData()) {  //处理只有一条数据的情况
            //绘制第一条直线
            mLinePaint.color = lineColor1
            canvas.drawLine(mXPoint, mDataPointList1[0].y, mXPoint + mXLength, mDataPointList1[0].y, mLinePaint)
            //绘制第二条直线
            mLinePaint.color = lineColor2
            canvas.drawLine(mXPoint, mDataPointList2[0].y, mXPoint + mXLength, mDataPointList2[0].y, mLinePaint)
            return
        }
        for (i in 0..mDataPointList1.size - 2) {
            if (i <= mCurrentDrawIndex) {
                //绘制第一条折线
                //绘制折线
                mLinePaint.color = lineColor1
                canvas.drawLine(
                    mDataPointList1[i].x, mDataPointList1[i].y,
                    mDataPointList1[i + 1].x, mDataPointList1[i + 1].y, mLinePaint
                )
                //绘制折线交点
                canvas.drawCircle(mDataPointList1[i].x, mDataPointList1[i].y, lineWidth * 1.5f, mLinePaint)
                mLinePaint.color = Color.WHITE
                canvas.drawCircle(mDataPointList1[i].x, mDataPointList1[i].y, lineWidth * 0.5f, mLinePaint)

                //绘制第二条折线
                //绘制折线
                mLinePaint.color = lineColor2
                canvas.drawLine(
                    mDataPointList2[i].x, mDataPointList2[i].y,
                    mDataPointList2[i + 1].x, mDataPointList2[i + 1].y, mLinePaint
                )
                //绘制折线交点
                canvas.drawCircle(mDataPointList2[i].x, mDataPointList2[i].y, lineWidth * 1.5f, mLinePaint)
                mLinePaint.color = Color.WHITE
                canvas.drawCircle(mDataPointList2[i].x, mDataPointList2[i].y, lineWidth * 0.5f, mLinePaint)

                //绘制最后一个折线交点
                if (i == mDataPointList1.size - 2) {
                    mLinePaint.color = lineColor1
                    canvas.drawCircle(
                        mDataPointList1[mDataPointList1.size - 1].x,
                        mDataPointList1[mDataPointList1.size - 1].y,
                        lineWidth * 1.5f,
                        mLinePaint
                    )
                    mLinePaint.color = Color.WHITE
                    canvas.drawCircle(
                        mDataPointList1[mDataPointList1.size - 1].x,
                        mDataPointList1[mDataPointList1.size - 1].y,
                        lineWidth * 0.5f,
                        mLinePaint
                    )

                    mLinePaint.color = lineColor2
                    canvas.drawCircle(
                        mDataPointList2[mDataPointList2.size - 1].x,
                        mDataPointList2[mDataPointList2.size - 1].y,
                        lineWidth * 1.5f,
                        mLinePaint
                    )
                    mLinePaint.color = Color.WHITE
                    canvas.drawCircle(
                        mDataPointList2[mDataPointList2.size - 1].x,
                        mDataPointList2[mDataPointList2.size - 1].y,
                        lineWidth * 0.5f,
                        mLinePaint
                    )
                }
            }
        }
    }

    //计算数值对应的Y坐标
    private fun calculateYPosition(data: Float): Float = mYPoint - data / maxYValue * mYLength

    //绘制点击后的详情展示
    private fun drawLabel(canvas: Canvas) {
        if (clickAble && mDataList1.size > 0) {
            //绘制点击后的竖刻线
            mLabelPaint.color = Color.parseColor("#EBEBEB")
            mLabelPaint.strokeWidth = DEFAULT_GRID_WIDTH * 2
            canvas.drawLine(
                mDataPointList1[mClickIndex].x,
                mYPoint,
                mDataPointList1[mClickIndex].x,
                topMargin - labelArrowMargin,
                mLabelPaint
            )
            //绘制点击后的折线交点
            mLabelPaint.color = lineColor1
            canvas.drawCircle(
                mDataPointList1[mClickIndex].x,
                mDataPointList1[mClickIndex].y,
                lineWidth * 2.3f,
                mLabelPaint
            )
            mLabelPaint.color = lineColor2
            canvas.drawCircle(
                mDataPointList2[mClickIndex].x,
                mDataPointList2[mClickIndex].y,
                lineWidth * 2.3f,
                mLabelPaint
            )
            //绘制最上方标签信息
            with(mLabelRectF) {
                bottom = topMargin - labelArrowMargin - labelArrowHeight
                top = bottom - labelHeight;
                left = mDataPointList1[mClickIndex].x - labelArrowWidth / 2f - labelArrowOffset
                right = left + labelWidth
                //处理点击第一项出现标签偏离整个折线图现象
                if (left < 0) {
                    left = SizeUtil.dp2px(5f)
                    right = left + labelWidth
                }
                //处理点击最后一项出现标签偏离整个折线图现象
                if (right > mWidth) {
                    right = mWidth.toFloat() - SizeUtil.dp2px(5f)
                    left = right - labelWidth
                }
            }
            //绘制圆角矩形
            mLabelBgPaint.setShadowLayer(
                SizeUtil.dp2px(12f),  //阴影效果
                SizeUtil.dp2px(2.5f),
                SizeUtil.dp2px(1.5f),
                Color.parseColor("#C7C7C7")
            )
            canvas.drawRoundRect(mLabelRectF, labelRadius, labelRadius, mLabelBgPaint)
            //绘制箭头
            val arrowPath = Path()
            with(arrowPath) {
                moveTo(mDataPointList1[mClickIndex].x, topMargin - labelArrowMargin)
                val baseY = topMargin - labelArrowMargin - labelArrowHeight - SizeUtil.dp2px(1f)
                lineTo(mDataPointList1[mClickIndex].x - labelArrowWidth / 2f, baseY)
                lineTo(mDataPointList1[mClickIndex].x + labelArrowWidth / 2f, baseY)
                close()
            }
            mLabelPaint.color = labelBackgroundColor
            canvas.drawPath(arrowPath, mLabelPaint)
            mLabelPaint.color = Color.parseColor("#F1F1F1")
            mLabelPaint.strokeWidth = gridWidth
            canvas.drawLine(
                mLabelRectF.left + SizeUtil.dp2px(10f),
                mLabelRectF.bottom - SizeUtil.dp2px(52f),
                mLabelRectF.right - SizeUtil.dp2px(10f),
                mLabelRectF.bottom - SizeUtil.dp2px(52f), mLabelPaint
            )
            //绘制文字
            with(mTextPaint) {
                color = labelTextColor
                textSize = labelTextSize
                textAlign = Paint.Align.LEFT
            }
            canvas.drawText(
                mXLabelList[mClickIndex],
                mLabelRectF.left + SizeUtil.dp2px(9.5f),
                mLabelRectF.bottom - SizeUtil.dp2px(61f), mTextPaint
            )
            canvas.drawText(
                "交易收益  ¥${mDataList1[mClickIndex]}",
                mLabelRectF.left + SizeUtil.dp2px(19.5f),
                mLabelRectF.bottom - SizeUtil.dp2px(32.5f), mTextPaint
            )
            canvas.drawText(
                "返现收益  ¥${mDataList2[mClickIndex]}",
                mLabelRectF.left + SizeUtil.dp2px(19.5f),
                mLabelRectF.bottom - SizeUtil.dp2px(12.5f), mTextPaint
            )
            mTextPaint.color = lineColor1
            canvas.drawCircle(
                mLabelRectF.left + SizeUtil.dp2px(12.5f),
                mLabelRectF.bottom - SizeUtil.dp2px(36f),
                SizeUtil.dp2px(2.5f), mTextPaint
            )
            mTextPaint.color = lineColor2
            canvas.drawCircle(
                mLabelRectF.left + SizeUtil.dp2px(12.5f),
                mLabelRectF.bottom - SizeUtil.dp2px(16f),
                SizeUtil.dp2px(2.5f), mTextPaint
            )
        }
    }

    //绘制底部类型说明
    private fun drawBottomDescription(canvas: Canvas) {
        if (mDataList1 == null || mDataList1.size == 0 || mDataList2 == null || mDataList2.size == 0) {
            return
        }
        mTextPaint.color = lineColor1
        val centerX1 = mWidth / 2f - SizeUtil.dp2px(75.5f)
        canvas.drawCircle(
            centerX1, mHeight - SizeUtil.dp2px(20f),
            SizeUtil.dp2px(3.5f), mTextPaint
        )
        mTextPaint.color = lineColor2
        val centerX2 = mWidth / 2f + SizeUtil.dp2px(16f)
        canvas.drawCircle(
            centerX2, mHeight - SizeUtil.dp2px(20f),
            SizeUtil.dp2px(3.5f), mTextPaint
        )
        mTextPaint.color = Color.WHITE
        canvas.drawCircle(
            centerX1, mHeight - SizeUtil.dp2px(20f),
            SizeUtil.dp2px(2.2f), mTextPaint
        )
        canvas.drawCircle(
            centerX2, mHeight - SizeUtil.dp2px(20f),
            SizeUtil.dp2px(2.2f), mTextPaint
        )
        with(mTextPaint) {
            color = labelTextColor
            textSize = SizeUtil.sp2px(12f)
        }
        canvas.drawText(
            "交易收益", centerX1 + SizeUtil.dp2px(8f),
            mHeight - SizeUtil.dp2px(15.5f), mTextPaint
        )
        canvas.drawText(
            "返现收益", centerX2 + SizeUtil.dp2px(8f),
            mHeight - SizeUtil.dp2px(15.5f), mTextPaint
        )
    }

    //格式化标签内的数值文本
    private fun formatValue(value: Float): String {
        val scale = maxYValue / yLabelCount.toFloat()
        if (scale < 10 && (value != value.toInt().toFloat()) && (value >= 0.01f)) {
            return "${(value * 100).toInt().toFloat() / 100}"  //保留2位小数，但不四舍五入
        }
        return "${value.toInt()}"
    }

    //是否只有一条数据
    private fun hasOnlyOneData(): Boolean = mDataList1.size == 1 && mDataList2.size == 1 && mXLabelList.size == 1

    //设置数据，startAnim：是否开启动画，动画默认一条一条折线的画
    //list1和list2的数据个数要相同，dateList的数据个数大于等于list1和list2的数据个数
    fun drawData(list1: MutableList<Float>, list2: MutableList<Float>, dateList: MutableList<String>, startAnim: Boolean = false) {
        if (list1.size != list2.size) {
            throw RuntimeException("the size of list1 must be equal to the size of list2")
        }
        if (dateList.size < list1.size) {
            throw RuntimeException("the size of dateList can not less than the size of list1")
        }
        var maxValue = 0f
        for (item in list1) {
            if (maxValue <= item) {
                maxValue = item
            }
        }
        for (item in list2) {
            if (maxValue <= item) {
                maxValue = item
            }
        }
        mDataList1 = list1
        mDataList2 = list2
        mXLabelList = dateList
        maxYValue = calculateMaxValue(maxValue)
        mClickIndex = 0
        setYLable()  //重新设置Y轴刻度值
        if (startAnim) {
            val animator = ValueAnimator.ofInt(0, mDataList1.size - 2)
            animator.setDuration(1500)
            animator.addUpdateListener {
                mCurrentDrawIndex = it.getAnimatedValue() as Int
                invalidate()
            }
            animator.interpolator = LinearInterpolator()
            animator.start()
        } else {
            mCurrentDrawIndex = mDataList1.size - 2
            invalidate()
        }
    }

    //计算Y轴最大值和单位，计算规则：最高位数加1取整
    private fun calculateMaxValue(value: Float): Int {
        val valueStr = value.toLong().toString()
        val length = valueStr.length  //整数的位数
        val unit = Math.pow(10.0, (length - 1).toDouble()).toInt()
        if (value == 0f) {
            return DEFAULT_MAX_YVALUE  //如果最大值是0，即所有数据都是0，取默认的最大值
        } else if (value % unit == 0f) {
            return value.toInt()
        } else {
            return ((value / unit).toInt() + 1) * unit
        }
    }

}