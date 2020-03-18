package com.android.widget.LineChart.type1

import android.content.Context
import android.graphics.*
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.android.basicproject.R
import com.android.util.DateUtil
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2019/10/16
 * Desc:曲线图
 */
class LineChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val TOP_DATE_FORMAT = "MM月dd日"
        private val BOTTOM_DATE_FORMAT = "MM/dd"
        private val DEFAULT_MAX_YVALUE = 5000
        private val DEFAULT_VALUE_UNIT = "元"
        private val DEFAULT_XLABEL_COUNT = 6
        private val DEFAULT_YLABEL_COUNT = 5
        private val DEFAULT_XLABEL_TEXT_SIZE = SizeUtil.sp2px(10f)
        private val DEFAULT_XLABEL_TEXT_COLOR = Color.parseColor("#7F7F7F")
        private val DEFAULT_XLABEL_TEXT_MARGIN = SizeUtil.dp2px(7f)
        private val DEFAULT_YLABEL_TEXT_SIZE = SizeUtil.sp2px(11f)
        private val DEFAULT_YLABEL_TEXT_COLOR = Color.BLACK
        private val DEFAULT_YLABEL_TEXT_MARGIN = SizeUtil.dp2px(20f)
        private val DEFAULT_AXIS_WIDTH = SizeUtil.dp2px(1f)
        private val DEFAULT_AXIS_COLOR = Color.parseColor("#DCDCDC")
        private val DEFAULT_SHOW_SCALE = true
        private val DEFAULT_SCALE_LENGTH = SizeUtil.dp2px(4f)
        private val DEFAULT_SHOW_GRID = true
        private val DEFAULT_GRID_WIDTH = SizeUtil.dp2px(0.5f)
        private val DEFAULT_GRID_DASH_INTERVAL = SizeUtil.dp2px(1f)
        private val DEFAULT_GRID_DASH_LENGTH = SizeUtil.dp2px(2f)
        private val DEFAULT_GRID_COLOR = Color.parseColor("#F0F0F0")
        private val DEFAULT_LINE_WIDTH = SizeUtil.dp2px(3f)
        private val DEFAULT_LINE_START_COLOR = Color.parseColor("#0071FF")
        private val DEFAULT_LINE_END_COLOR = Color.parseColor("#6CD0FF")
        private val DEFAULT_BOTTOM_START_COLOR = Color.parseColor("#33FFFFFF")
        private val DEFAULT_BOTTOM_END_COLOR = Color.parseColor("#330071FF")
        private val DEFAULT_LABEL_COLOR = Color.parseColor("#0071FF")
        private val DEFAULT_LABEL_RADIUS = SizeUtil.dp2px(3f)
        private val DEFAULT_SHOW_LABEL_DATE = true
        private val DEFAULT_LABEL_TEXT_SIZE = SizeUtil.sp2px(11f)
        private val DEFAULT_LABEL_TEXT_COLOR = Color.WHITE
        private val DEFAULT_LABEL_TEXT_MARGIN = SizeUtil.dp2px(6f)
        private val DEFAULT_LABEL_TEXT_LINE_SPACING_EXTRA = SizeUtil.dp2px(3f)
        private val DEFAULT_LABEL_ARROW_WIDTH = SizeUtil.dp2px(7f)
        private val DEFAULT_LABEL_ARROW_HEIGHT = SizeUtil.dp2px(4f)
        private val DEFAULT_LABEL_ARROW_OFFSET = SizeUtil.dp2px(11f)
        private val DEFAULT_LABEL_ARROW_MARGIN = SizeUtil.dp2px(5f)
        private val DEFAULT_LABEL_ARROW_GRAVITY = Gravity.LEFT
        private val DEFAULT_CLICKABLE = true
        private val DEFAULT_LEFT_MARGIN = SizeUtil.dp2px(55f)
        private val DEFAULT_TOP_MARGIN = SizeUtil.dp2px(55f)
        private val DEFAULT_RIGHT_MARGIN = SizeUtil.dp2px(20f)
        private val DEFAULT_BOTTOM_MARGIN = SizeUtil.dp2px(36f)
    }

    //Y轴最大值
    var maxYValue: Int = DEFAULT_MAX_YVALUE
    //数值单位：元|万元
    var valueUnit: String = DEFAULT_VALUE_UNIT
    //X轴上的刻度值个数：值为6表示近7天、值为14表示近15天、值为29表示近30天
    var xLabelCount: Int = DEFAULT_XLABEL_COUNT
    //Y轴上的刻度值个数，默认为5
    var yLabelCount: Int = DEFAULT_YLABEL_COUNT
    //X轴刻度值文本字体大小
    var xLabelTextSize: Float = DEFAULT_XLABEL_TEXT_SIZE
    //X轴刻度值文本字体颜色
    var xLabelTextColor: Int = DEFAULT_XLABEL_TEXT_COLOR
    //X轴刻度值文本到屏幕左侧的左边距
    var xLabelTextMargin: Float = DEFAULT_XLABEL_TEXT_MARGIN
    //Y轴刻度值文本字体大小
    var yLabelTextSize: Float = DEFAULT_YLABEL_TEXT_SIZE
    //Y轴刻度值文本字体颜色
    var yLabelTextColor: Int = DEFAULT_YLABEL_TEXT_COLOR
    //Y轴刻度值文本到X轴的上边距
    var yLabelTextMargin: Float = DEFAULT_YLABEL_TEXT_MARGIN
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
    //曲线宽度
    var lineWidth: Float = DEFAULT_LINE_WIDTH
    //曲线颜色渐变起始色
    var lineStartColor: Int = DEFAULT_LINE_START_COLOR
    //曲线颜色渐变结束色
    var lineEndColor: Int = DEFAULT_LINE_END_COLOR
    //曲线下方背景渐变起始色
    var bottomStartColor: Int = DEFAULT_BOTTOM_START_COLOR
    //曲线下方背景渐变结束色
    var bottomEndColor: Int = DEFAULT_BOTTOM_END_COLOR
    //标签颜色
    var labelColor: Int = DEFAULT_LABEL_COLOR
    //标签的矩形圆角
    var labelRadius: Float = DEFAULT_LABEL_RADIUS
    //标签内是否显示日期
    var showLabelDate: Boolean = DEFAULT_SHOW_LABEL_DATE
    //标签内文本字体大小
    var labelTextSize: Float = DEFAULT_LABEL_TEXT_SIZE
    //标签内文本字体颜色
    var labelTextColor: Int = DEFAULT_LABEL_TEXT_COLOR
    //标签内文本四周的边距
    var labelTextMargin: Float = DEFAULT_LABEL_TEXT_MARGIN
    //标签内文本的行间距
    var labelTextLineSpacingExtra: Float = DEFAULT_LABEL_TEXT_LINE_SPACING_EXTRA
    //标签的箭头宽度
    var labelArrowWidth: Float = DEFAULT_LABEL_ARROW_WIDTH
    //标签的箭头高度
    var labelArrowHeight: Float = DEFAULT_LABEL_ARROW_HEIGHT
    //标签的箭头到标签左侧或右侧的偏移量
    var labelArrowOffset: Float = DEFAULT_LABEL_ARROW_OFFSET
    //标签的箭头到坐标轴最上方的下边距
    var labelArrowMargin: Float = DEFAULT_LABEL_ARROW_MARGIN
    //标签的箭头相对于标签矩形底部的位置
    private var labelArrowGravity: Int = DEFAULT_LABEL_ARROW_GRAVITY
    //是否可点击
    var clickAble: Boolean = DEFAULT_CLICKABLE
    //坐标轴到View左侧的边距
    private var leftMargin: Float = DEFAULT_LEFT_MARGIN
    //坐标轴到View顶部的边距
    private var topMargin: Float = DEFAULT_TOP_MARGIN
    //坐标轴到View右侧的边距
    private var rightMargin: Float = DEFAULT_RIGHT_MARGIN
    //坐标轴到View底部的边距
    private var bottomMargin: Float = DEFAULT_BOTTOM_MARGIN

    private lateinit var mAxisPaint: Paint    //绘制轴线和轴线上的小刻度线
    private lateinit var mGridPaint: Paint    //绘制网格线
    private lateinit var mLinePaint: Paint    //绘制曲线
    private lateinit var mBgPaint: Paint       //绘制曲线下方渐变色背景
    private lateinit var mLabelPaint: Paint   //绘制最上方标签
    private lateinit var mTextPaint: Paint    //绘制文本
    private lateinit var mLabelRectF: RectF   //最上方的标签对应的矩形
    private lateinit var mLinePath: Path      //曲线路径
    private lateinit var mBgPath: Path      //曲线路径

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mXPoint: Float = 0f     //原点的X坐标
    private var mYPoint: Float = 0f     //原点的Y坐标
    private var mXScale: Float = 0f     //X轴刻度长度
    private var mYScale: Float = 0f     //Y轴刻度长度
    private var mXLength: Float = 0f    //X轴长度
    private var mYLength: Float = 0f    //Y轴长度
    private var mClickIndex: Int = 0    //点击时的下标
    private var mMaxYPositionIndex: Int = 0  //Y坐标最大值时的下标

    private var mDataList: MutableList<Float> = mutableListOf()     //数据
    private var mYLabelList: MutableList<String> = mutableListOf()  //Y轴刻度值
    //记录每个数据点的X、Y坐标
    private var mDataPointList: MutableList<PointF> = mutableListOf()

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
                text = "0元"
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
        valueUnit = ta.getString(R.styleable.LineChart_valueUnit) ?: DEFAULT_VALUE_UNIT
        xLabelCount = ta.getInt(R.styleable.LineChart_xLabelCount, DEFAULT_XLABEL_COUNT)
        yLabelCount = ta.getInt(R.styleable.LineChart_yLabelCount, DEFAULT_YLABEL_COUNT)
        xLabelTextSize =
            ta.getDimension(R.styleable.LineChart_xLabelTextSize, DEFAULT_XLABEL_TEXT_SIZE)
        xLabelTextColor =
            ta.getColor(R.styleable.LineChart_xLabelTextColor, DEFAULT_XLABEL_TEXT_COLOR)
        xLabelTextMargin =
            ta.getDimension(R.styleable.LineChart_xLabelTextMargin, DEFAULT_XLABEL_TEXT_MARGIN)
        yLabelTextSize =
            ta.getDimension(R.styleable.LineChart_yLabelTextSize, DEFAULT_YLABEL_TEXT_SIZE)
        yLabelTextColor =
            ta.getColor(R.styleable.LineChart_yLabelTextColor, DEFAULT_YLABEL_TEXT_COLOR)
        yLabelTextMargin =
            ta.getDimension(R.styleable.LineChart_yLabelTextMargin, DEFAULT_YLABEL_TEXT_MARGIN)
        axisWidth = ta.getDimension(R.styleable.LineChart_axisWidth, DEFAULT_AXIS_WIDTH)
        axisColor = ta.getColor(R.styleable.LineChart_axisColor, DEFAULT_AXIS_COLOR)
        showScale = ta.getBoolean(R.styleable.LineChart_showScale, DEFAULT_SHOW_SCALE)
        scaleLength = ta.getDimension(R.styleable.LineChart_scaleLength, DEFAULT_SCALE_LENGTH)
        showGrid = ta.getBoolean(R.styleable.LineChart_showGrid, DEFAULT_SHOW_GRID)
        gridWidth = ta.getDimension(R.styleable.LineChart_gridWidth, DEFAULT_GRID_WIDTH)
        gridDashInterval =
            ta.getDimension(R.styleable.LineChart_gridDashInterval, DEFAULT_GRID_DASH_INTERVAL)
        gridDashLength =
            ta.getDimension(R.styleable.LineChart_gridDashLength, DEFAULT_GRID_DASH_LENGTH)
        gridColor = ta.getColor(R.styleable.LineChart_gridColor, DEFAULT_GRID_COLOR)
        lineWidth = ta.getDimension(R.styleable.LineChart_lineWidth, DEFAULT_LINE_WIDTH)
        lineStartColor = ta.getColor(R.styleable.LineChart_lineStartColor, DEFAULT_LINE_START_COLOR)
        lineEndColor = ta.getColor(R.styleable.LineChart_lineEndColor, DEFAULT_LINE_END_COLOR)
        bottomStartColor =
            ta.getColor(R.styleable.LineChart_bottomStartColor, DEFAULT_BOTTOM_START_COLOR)
        bottomEndColor = ta.getColor(R.styleable.LineChart_bottomEndColor, DEFAULT_BOTTOM_END_COLOR)
        labelColor = ta.getColor(R.styleable.LineChart_labelColor, DEFAULT_LABEL_COLOR)
        labelRadius = ta.getDimension(R.styleable.LineChart_labelRadius, DEFAULT_LABEL_RADIUS)
        showLabelDate = ta.getBoolean(R.styleable.LineChart_showLabelDate, DEFAULT_SHOW_LABEL_DATE)
        labelTextSize =
            ta.getDimension(R.styleable.LineChart_labelTextSize, DEFAULT_LABEL_TEXT_SIZE)
        labelTextColor = ta.getColor(R.styleable.LineChart_labelTextColor, DEFAULT_LABEL_TEXT_COLOR)
        labelTextMargin =
            ta.getDimension(R.styleable.LineChart_labelTextMargin, DEFAULT_LABEL_TEXT_MARGIN)
        labelTextLineSpacingExtra = ta.getDimension(
            R.styleable.LineChart_labelTextLineSpacingExtra,
            DEFAULT_LABEL_TEXT_LINE_SPACING_EXTRA
        )
        labelArrowWidth =
            ta.getDimension(R.styleable.LineChart_labelArrowWidth, DEFAULT_LABEL_ARROW_WIDTH)
        labelArrowHeight =
            ta.getDimension(R.styleable.LineChart_labelArrowHeight, DEFAULT_LABEL_ARROW_HEIGHT)
        labelArrowOffset =
            ta.getDimension(R.styleable.LineChart_labelArrowMargin, DEFAULT_LABEL_ARROW_OFFSET)
        labelArrowMargin =
            ta.getDimension(R.styleable.LineChart_labelArrowMargin, DEFAULT_LABEL_ARROW_MARGIN)
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
            setPathEffect(
                DashPathEffect(
                    floatArrayOf(gridDashLength, gridDashInterval),
                    0f
                )
            )  //设置虚线效果
        }
        mLinePaint = Paint()
        with(mLinePaint) {
            isAntiAlias = true
            color = lineStartColor
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
        }
        mBgPaint = Paint()
        with(mBgPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mLabelPaint = Paint()
        with(mLabelPaint) {
            isAntiAlias = true
            color = labelColor
        }
        mTextPaint = Paint()
        with(mTextPaint) {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        mLabelRectF = RectF()
        mLinePath = Path()
        mBgPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = SizeUtil.dp2px(275f).toInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(measuredWidth, height)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.getX() ?: 0f
        for (i in 0..mDataPointList.size - 1) {
            val centerX = mXPoint + i * mXScale
            var beginX = centerX - mXScale / 2f
            var endX = centerX + mXScale / 2f
            if (i == 0) {
                beginX = yLabelTextMargin
            }
            if (i == xLabelCount) {
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
            initSize(width, height)  //初始化尺寸信息
            drawBackGround(it)  //绘制曲线下方背景渐变效果
            drawCoordinate(it)  //绘制坐标轴
            drawLine(it)   //绘制曲线
            drawLabel(it)  //绘制点击后的效果
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
        mXScale = mXLength / xLabelCount
        mYScale = mYLength / yLabelCount
        mDataPointList.clear()
        for (i in 0..mDataList.size - 1) {
            if (mDataList.get(mMaxYPositionIndex) <= mDataList.get(i)) {
                mMaxYPositionIndex = i
            }
            mDataPointList.add(PointF(mXPoint + i * mXScale, calculateYPosition(mDataList.get(i))))
        }
    }

    //绘制曲线下方渐变色背景
    private fun drawBackGround(canvas: Canvas) {
        if (mDataList == null || mDataList.size <= 1) {
            return
        }
        mBgPath = getCurvePath(mDataPointList, true, mYPoint)
        //绘制背景渐变色
        mBgPaint.setShader(
            LinearGradient(
                mDataPointList[mMaxYPositionIndex].x,
                mYPoint,
                mDataPointList[mMaxYPositionIndex].x,
                mDataPointList[mMaxYPositionIndex].y,
                bottomStartColor,
                bottomEndColor,
                Shader.TileMode.CLAMP
            )
        )
        canvas.drawPath(mBgPath, mBgPaint)
    }

    //绘制坐标轴
    private fun drawCoordinate(canvas: Canvas) {
        //绘制X轴
        canvas.drawLine(
            mXPoint - axisWidth / 2f,
            mYPoint,
            mXPoint + mXLength + axisWidth / 2f,
            mYPoint,
            mAxisPaint
        )
        //绘制网格线：竖刻线
        if (showGrid) {
            //绘制首尾的竖刻线
            mGridPaint.color = gridColor
            if (mClickIndex != 0 || mDataList.size == 0) {
                canvas.drawLine(mXPoint, mYPoint - scaleLength, mXPoint, topMargin, mGridPaint)
            }
            if (mClickIndex != xLabelCount) {
                canvas.drawLine(
                    mXPoint + mXLength,
                    mYPoint - scaleLength,
                    mXPoint + mXLength,
                    topMargin,
                    mGridPaint
                )
            }
        }
        //绘制X轴首尾的刻度值
        with(mTextPaint) {
            textSize = xLabelTextSize
            color = xLabelTextColor
        }
        val fm = mTextPaint.getFontMetrics()
        val yOffset = mYPoint + xLabelTextMargin - fm.ascent
        mTextPaint.textAlign = Paint.Align.LEFT
        val beginDate = DateUtil.getDistanceDateByDay(-xLabelCount, BOTTOM_DATE_FORMAT)
        canvas.drawText(beginDate, mXPoint, yOffset, mTextPaint)
        mTextPaint.textAlign = Paint.Align.RIGHT
        val endDate = DateUtil.getDistanceDateByDay(0, BOTTOM_DATE_FORMAT)
        canvas.drawText(endDate, mXPoint + mXLength, yOffset, mTextPaint)
        if (xLabelCount <= 6) {  //近7天
            for (i in 0..xLabelCount) {
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
                //绘制X轴上的其余刻度值
                if (i != xLabelCount - 1 && i != xLabelCount) {
                    mTextPaint.textAlign = Paint.Align.CENTER
                    val text =
                        DateUtil.getDistanceDateByDay(-xLabelCount + i + 1, BOTTOM_DATE_FORMAT)
                    canvas.drawText(text, mXPoint + (i + 1) * mXScale, yOffset, mTextPaint)
                }
            }
        } else {  //近15天|近30天
            //绘制X轴上的小刻度线
            canvas.drawLine(mXPoint, mYPoint, mXPoint, mYPoint - scaleLength, mAxisPaint)
            val center = Math.round((xLabelCount / 2f).toDouble())
            val centerOffset = center * mXScale
            canvas.drawLine(
                mXPoint + centerOffset,
                mYPoint,
                mXPoint + centerOffset,
                mYPoint - scaleLength,
                mAxisPaint
            )
            canvas.drawLine(
                mXPoint + mXLength,
                mYPoint,
                mXPoint + mXLength,
                mYPoint - scaleLength,
                mAxisPaint
            )
            //绘制X轴上的其余刻度值
            mTextPaint.textAlign = Paint.Align.CENTER
            val text = DateUtil.getDistanceDateByDay(-(xLabelCount / 2), BOTTOM_DATE_FORMAT)
            canvas.drawText(text, mXPoint + centerOffset, yOffset, mTextPaint)
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
            with(mTextPaint) {
                textSize = yLabelTextSize
                color = yLabelTextColor
                textAlign = Paint.Align.LEFT
            }
            if (i == 0) {
                canvas.drawText(mYLabelList[i], yLabelTextMargin, mYPoint, mTextPaint)
            }
            val yLabelFm = mTextPaint.getFontMetrics()
            val yLabelYOffset =
                mYPoint + (yLabelFm.descent - yLabelFm.ascent) / 2f - yLabelFm.descent - (i + 1) * mYScale
            canvas.drawText(mYLabelList[i + 1], yLabelTextMargin, yLabelYOffset, mTextPaint)
        }
    }

    //绘制曲线
    private fun drawLine(canvas: Canvas) {
        if (mDataList == null || mDataList.size <= 1) {
            return
        }
        //绘制曲线
        mLinePath = getCurvePath(mDataPointList, false, mYPoint)
        mLinePaint.setShader(
            LinearGradient(
                mXPoint + mXLength * 6 / 7,    //渐变开始的位置：X轴6/7位置
                mYPoint,
                mXPoint + mXLength,
                mYPoint,
                lineStartColor,
                lineEndColor,
                Shader.TileMode.CLAMP
            )
        )
        canvas.drawPath(mLinePath, mLinePaint)
    }

    //获取曲线的路径，isClose:是否闭合
    private fun getCurvePath(
        pointList: MutableList<PointF>,
        isClose: Boolean,
        yPoint: Float
    ): Path {
        val path = Path()
        return path.apply {
            for (i in 0..pointList.size - 1) {
                if (i == 0) {
                    moveTo(pointList[0].x, pointList[0].y)
                }
                if (i != pointList.size - 1) {
                    cubicTo(
                        (pointList[i].x + pointList[i + 1].x) / 2f,
                        pointList[i].y,
                        (pointList[i].x + pointList[i + 1].x) / 2f,
                        pointList[i + 1].y,
                        pointList[i + 1].x,
                        pointList[i + 1].y
                    )
                } else {
                    if (isClose) {
                        lineTo(pointList[i].x, yPoint)
                        lineTo(pointList[0].x, yPoint)
                        lineTo(pointList[0].x, pointList[0].y)
                    }
                }
            }
        }
    }

    //计算数值对应的Y坐标
    private fun calculateYPosition(data: Float): Float =
        mYPoint - data / maxYValue * mYLength

    //绘制点击后的详情展示
    private fun drawLabel(canvas: Canvas) {
        if (clickAble && mDataList.size > 0) {
            //绘制点击后的竖刻线
            mGridPaint.color = labelColor
            canvas.drawLine(
                mXPoint + mClickIndex * mXScale,
                mYPoint,
                mXPoint + mClickIndex * mXScale,
                topMargin,
                mGridPaint
            )
            //绘制点击后的曲线交点
            mLabelPaint.color = labelColor
            canvas.drawCircle(
                mDataPointList[mClickIndex].x,
                mDataPointList[mClickIndex].y,
                lineWidth * 1.5f,
                mLabelPaint
            )
            mLabelPaint.color = Color.WHITE
            canvas.drawCircle(
                mDataPointList[mClickIndex].x,
                mDataPointList[mClickIndex].y,
                lineWidth * 0.5f,
                mLabelPaint
            )
            //绘制最上方标签信息
            mLabelPaint.color = labelColor
            //绘制箭头
            val arrowPath = Path()
            with(arrowPath) {
                moveTo(mDataPointList[mClickIndex].x, topMargin - labelArrowMargin)
                val baseY = topMargin - labelArrowMargin - labelArrowHeight - SizeUtil.dp2px(1f)
                lineTo(mDataPointList[mClickIndex].x - labelArrowWidth / 2f, baseY)
                lineTo(mDataPointList[mClickIndex].x + labelArrowWidth / 2f, baseY)
                close()
            }
            canvas.drawPath(arrowPath, mLabelPaint)
            //计算文本的宽高
            with(mTextPaint) {
                color = labelTextColor
                textSize = labelTextSize
                textAlign = Paint.Align.LEFT
            }
            var dateText =
                DateUtil.getDistanceDateByDay(-xLabelCount + mClickIndex, TOP_DATE_FORMAT)
            if (dateText.startsWith("0")) {
                dateText = dateText.substring(1)
            }
            val dateRect = Rect()  //日期
            mTextPaint.getTextBounds(dateText, 0, dateText.length, dateRect)
            val valueText = formatValue(mDataList.get(mClickIndex))
            val valueRect = Rect()  //数值
            mTextPaint.getTextBounds(valueText, 0, valueText.length, valueRect)
            var maxTextWidth = valueRect.width()
            if (showLabelDate) {
                if (maxTextWidth < dateRect.width()) {
                    maxTextWidth = dateRect.width()
                }
            }
            with(mLabelRectF) {
                bottom = topMargin - labelArrowMargin - labelArrowHeight
                if (showLabelDate) {
                    top =
                        bottom - labelTextMargin * 2 - labelTextLineSpacingExtra - dateRect.height() - valueRect.height()
                } else {
                    top = bottom - labelTextMargin * 2 - valueRect.height()
                }
                if (labelArrowGravity == Gravity.LEFT) {
                    left = mDataPointList[mClickIndex].x - labelArrowWidth / 2f - labelArrowOffset
                    right = left + labelTextMargin * 2 + maxTextWidth
                } else if (labelArrowGravity == Gravity.RIGHT) {
                    right = mDataPointList[mClickIndex].x + labelArrowWidth / 2f + labelArrowOffset
                    left = right - labelTextMargin * 2 - maxTextWidth
                } else {  //居中
                    left = mDataPointList[mClickIndex].x - maxTextWidth / 2f - labelTextMargin
                    right = left + maxTextWidth + labelTextMargin * 2
                }
                //处理点击最后一项出现标签偏离整个曲线图现象
                if (right > mWidth) {
                    right = mWidth.toFloat() - SizeUtil.dp2px(5f)
                    left = right - labelTextMargin * 2 - maxTextWidth
                }
            }
            //绘制圆角矩形
            canvas.drawRoundRect(mLabelRectF, labelRadius, labelRadius, mLabelPaint)
            //绘制文字
            if (showLabelDate) {
                canvas.drawText(
                    dateText,
                    mLabelRectF.left + labelTextMargin,
                    mLabelRectF.top + labelTextMargin + dateRect.height(),
                    mTextPaint
                )
                canvas.drawText(
                    valueText,
                    mLabelRectF.left + labelTextMargin,
                    mLabelRectF.bottom - labelTextMargin,
                    mTextPaint
                )
            } else {
                val fm = mTextPaint.getFontMetrics()
                canvas.drawText(
                    valueText,
                    mLabelRectF.left + labelTextMargin,
                    mLabelRectF.centerY() + (fm.descent - fm.ascent) / 2f - fm.descent,
                    mTextPaint
                )
            }

        }
    }

    //格式化标签内的数值文本
    private fun formatValue(value: Float): String {
        val scale = maxYValue / yLabelCount.toFloat()
        if (scale < 10 && (value != value.toInt().toFloat()) && (value >= 0.01f)) {
            return "${(value * 100).toInt().toFloat() / 100}$valueUnit"  //保留2位小数，但不四舍五入
        }
        return "${value.toInt()}$valueUnit"
    }

    //设置数据
    fun setData(list: MutableList<Float>) {
        var maxValue = 0f
        for (item in list) {
            if (maxValue <= item) {
                maxValue = item
            }
        }
        if (valueUnit.equals("万元") && maxValue < 1) {
            return
        }
        if (valueUnit.equals("元") && maxValue < 10) {
            return
        }
        this.mDataList = list
        maxYValue = calculateMaxValue(maxValue)
        setYLable()  //重新设置Y轴刻度值
        invalidate()
    }

    //计算Y轴最大值和单位，计算规则：最高位数加1取整
    private fun calculateMaxValue(value: Float): Int {
        val valueStr = value.toLong().toString()
        val length = valueStr.length  //整数的位数
        val unit = Math.pow(10.0, (length - 1).toDouble()).toInt()
        if (value % unit == 0f) {
            return value.toInt()
        } else {
            return ((value / unit).toInt() + 1) * unit
        }
    }

}