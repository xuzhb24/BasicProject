package com.android.widget.PieChart.type2

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil
import com.android.widget.PieChart.PieData

/**
 * Created by xuzhb on 2019/10/16
 * Desc:饼状图
 */
class PieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        private val TOTAL_ANGLE = 360f
        private val DEFAULT_BEGIN_ANGLE = 0f
        private val DEFAULT_SHOW_INSIDE = true
        private val DEFAULT_INSIDE_RATIO = 0.457f
        private val DEFAULT_INSIDE_COLOR = Color.WHITE
        private val DEFAULT_SHOW_DIVIDER_LINE = true
        private val DEFAULT_DIVIDER_COLOR = Color.WHITE
        private val DEFAULT_DIVIDER_WIDTH = SizeUtil.dp2px(1f)
        private val DEFAULT_ITEM_MARGIN_TOP = SizeUtil.dp2px(20f)
        private val DEFAULT_ITEM_MARGIN_BOTTOM = SizeUtil.dp2px(20f)
        private val DEFAULT_ITEM_SPACING_EXTRA = SizeUtil.dp2px(8f)
        private val DEFAULT_POINT_SIZE = SizeUtil.dp2px(12f)
        private val DEFAULT_POINT_INSIDE_COLOR = Color.WHITE
        private val DEFAULT_POINT_MARGIN_LEFT = SizeUtil.dp2px(20f)
        private val DEFAULT_POINT_MARGIN_RIGHT = SizeUtil.dp2px(8f)
        private val DEFAULT_LABEL_TEXT_SIZE = SizeUtil.sp2px(12f)
        private val DEFAULT_LABEL_TEXT_COLOR = Color.parseColor("#666666")
        private val DEFAULT_VALUE_TEXT_SIZE = SizeUtil.sp2px(13f)
        private val DEFAULT_VALUE_TEXT_COLOR = Color.parseColor("#010101")
        private val DEFAULT_EMPTY_PIE_COLOR = Color.parseColor("#DEDEDE")
        private val DEFAULT_EMPTY_TEXT = "暂无数据"
        private val DEFAULT_EMPTY_TEXT_SIZE = SizeUtil.sp2px(14f)
        private val DEFAULT_EMPTY_TEXT_COLOR = Color.parseColor("#666666")
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

    //底部各部分的上边距
    var itemMarginTop: Float = DEFAULT_ITEM_MARGIN_TOP

    //底部各部分的下边距
    var itemMarginBottom: Float = DEFAULT_ITEM_MARGIN_BOTTOM

    //底部各部分的内部边距
    var itemSpacingExtra: Float = DEFAULT_ITEM_SPACING_EXTRA

    //底部各部分左侧圆弧大小
    var pointSize: Float = DEFAULT_POINT_SIZE

    //底部各部分左侧圆弧的内圈颜色
    var pointInsideColor: Int = DEFAULT_POINT_INSIDE_COLOR

    //底部各部分左侧圆弧的左边距
    var pointMarginLeft: Float = DEFAULT_POINT_MARGIN_LEFT

    //底部各部分的右边距
    var pointMarginRight: Float = DEFAULT_POINT_MARGIN_RIGHT

    //底部各部分标签文本的字体大小
    var labelTextSize: Float = DEFAULT_LABEL_TEXT_SIZE

    //底部各部分标签文本的字体颜色
    var labelTextColor: Int = DEFAULT_LABEL_TEXT_COLOR

    //底部各部分数值文本的字体大小
    var valueTextSize: Float = DEFAULT_VALUE_TEXT_SIZE

    //底部各部分数值文本的字体颜色
    var valueTextColor: Int = DEFAULT_VALUE_TEXT_COLOR

    //数据为空时饼图的颜色
    var emptyPieColor: Int = DEFAULT_EMPTY_PIE_COLOR

    //数据为空时的文本描述
    var emptyText: String = DEFAULT_EMPTY_TEXT

    //数据为空时的文本字体大小
    var emptyTextSize: Float = DEFAULT_EMPTY_TEXT_SIZE

    //数据为空时的文本字体颜色
    var emptyTextColor: Int = DEFAULT_EMPTY_TEXT_COLOR

    private lateinit var mArcPaint: Paint        //绘制各个扇形和底部各部分左侧圆弧
    private lateinit var mInsidePaint: Paint     //绘制内圈，包括饼状图中间的圆圈和底部各部分左侧圆弧的内圈
    private lateinit var mDividerPaint: Paint    //绘制各个扇形的分界线
    private lateinit var mLabelTextPaint: Paint  //绘制底部各部分标签文本
    private lateinit var mValueTextPaint: Paint  //绘制底部各部分数值文本
    private lateinit var mEmptyTextPaint: Paint  //绘制暂无数据时的文本
    private lateinit var mRectF: RectF            //饼状图对应的矩形RectF

    private var mCenterX: Float = 0f
    private var mCenterY: Float = 0f
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRadius: Float = 0f     //饼状图的半径
    private var mProgress: Float = beginAngle + TOTAL_ANGLE       //开启动画时饼状图绘制的进度
    private var mIsDataEmpty: Boolean = true
    private var mNotEmptyItemCount: Int = 0    //不为0的数据个数
    private var mItemHeight: Float = 0f   //底部各部分每一行的高度
    private var mBottomHeight: Float = 0f    //底部的高度

    private var mDataList: MutableList<PieData> = mutableListOf() //填充的数据
    private var mPieRatio: MutableList<Float> = mutableListOf()   //各个扇形所占的比例

    init {
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
    }

    //获取布局属性并设置属性默认值
    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PieChart)
        beginAngle = ta.getFloat(R.styleable.PieChart_beginAngle, DEFAULT_BEGIN_ANGLE)
        showInside = ta.getBoolean(R.styleable.PieChart_showInside, DEFAULT_SHOW_INSIDE)
        insideRatio = ta.getFloat(R.styleable.PieChart_insideRatio, DEFAULT_INSIDE_RATIO)
        insideColor = ta.getColor(R.styleable.PieChart_insideColor, DEFAULT_INSIDE_COLOR)
        showDividerLine = ta.getBoolean(R.styleable.PieChart_showDividerLine, DEFAULT_SHOW_DIVIDER_LINE)
        dividerColor = ta.getColor(R.styleable.PieChart_dividerColor, DEFAULT_DIVIDER_COLOR)
        dividerWidth = ta.getDimension(R.styleable.PieChart_dividerWidth, DEFAULT_DIVIDER_WIDTH)
        itemMarginTop = ta.getDimension(R.styleable.PieChart_itemMarginTop, DEFAULT_ITEM_MARGIN_TOP)
        itemMarginBottom = ta.getDimension(R.styleable.PieChart_itemMarginBottom, DEFAULT_ITEM_MARGIN_BOTTOM)
        itemSpacingExtra = ta.getDimension(R.styleable.PieChart_itemSpacingExtra, DEFAULT_ITEM_SPACING_EXTRA)
        pointSize = ta.getDimension(R.styleable.PieChart_pointSize, DEFAULT_POINT_SIZE)
        pointInsideColor = ta.getColor(R.styleable.PieChart_pointInsideColor, DEFAULT_POINT_INSIDE_COLOR)
        pointMarginLeft = ta.getDimension(R.styleable.PieChart_pointMarginLeft, DEFAULT_POINT_MARGIN_LEFT)
        pointMarginRight = ta.getDimension(R.styleable.PieChart_pointMarginRight, DEFAULT_POINT_MARGIN_RIGHT)
        labelTextSize = ta.getDimension(R.styleable.PieChart_labelTextSize, DEFAULT_LABEL_TEXT_SIZE)
        labelTextColor = ta.getColor(R.styleable.PieChart_labelTextColor, DEFAULT_LABEL_TEXT_COLOR)
        valueTextSize = ta.getDimension(R.styleable.PieChart_valueTextSize, DEFAULT_VALUE_TEXT_SIZE)
        valueTextColor = ta.getColor(R.styleable.PieChart_valueTextColor, DEFAULT_VALUE_TEXT_COLOR)
        emptyPieColor = ta.getColor(R.styleable.PieChart_emptyPieColor, DEFAULT_EMPTY_PIE_COLOR)
        emptyText = ta.getString(R.styleable.PieChart_emptyText) ?: DEFAULT_EMPTY_TEXT
        emptyTextSize = ta.getDimension(R.styleable.PieChart_emptyTextSize, DEFAULT_EMPTY_TEXT_SIZE)
        emptyTextColor = ta.getColor(R.styleable.PieChart_emptyTextColor, DEFAULT_EMPTY_TEXT_COLOR)
        ta.recycle()
    }

    private fun initPaint() {
        //初始化画笔
        mArcPaint = Paint()
        mArcPaint.isAntiAlias = true
        mInsidePaint = Paint()
        mInsidePaint.isAntiAlias = true
        mDividerPaint = Paint()
        with(mDividerPaint) {
            isAntiAlias = true
            color = dividerColor
            strokeWidth = dividerWidth
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
        mEmptyTextPaint = Paint()
        with(mEmptyTextPaint) {
            isAntiAlias = true
            color = emptyTextColor
            textSize = emptyTextSize
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = SizeUtil.dp2px(250f).toInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(measuredWidth, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initSize(width, height)
        canvas?.let {
            drawPie(it)
            drawInside(it)
            drawBottomDescription(it)
        }
    }

    private fun initSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        mItemHeight = pointSize
        if (!mIsDataEmpty) {
            mBottomHeight = itemMarginTop + itemMarginBottom + mItemHeight * Math.round(mNotEmptyItemCount / 2f) +
                    itemSpacingExtra * (Math.round(mNotEmptyItemCount / 2f) - 1)
        } else {
            val emptyRect = Rect()
            mEmptyTextPaint.getTextBounds(emptyText, 0, emptyText.length, emptyRect)
            mBottomHeight = itemMarginTop + itemMarginBottom + emptyRect.height()
        }
        mCenterX = mWidth / 2f
        mCenterY = (mHeight - mBottomHeight) / 2f
        mRadius = (mHeight - mBottomHeight) / 2f
        mRectF = RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius)
    }

    //绘制扇形
    private fun drawPie(canvas: Canvas) {
        if (mIsDataEmpty) {  //暂无数据
            mArcPaint.color = emptyPieColor
            canvas.drawArc(mRectF, beginAngle, mProgress - beginAngle, true, mArcPaint)
        } else {
            var startAngle = beginAngle
            for (i in mDataList.indices) {
                var sweepAngle = 0f
                if (i != mDataList.size - 1) {
                    sweepAngle = mPieRatio[i] * TOTAL_ANGLE
                } else {
                    sweepAngle = beginAngle + TOTAL_ANGLE - startAngle
                }
                //跳过占比为0%的部分
                if (sweepAngle == 0f) {
                    continue
                }
                mArcPaint.color = mDataList.get(i).color
                if (mProgress - startAngle >= 0) {
                    //绘制各个扇形
                    canvas.drawArc(mRectF, startAngle, mProgress - startAngle, true, mArcPaint)
                    //每段扇形最终的角度
                    val endAngle = startAngle + sweepAngle
                    //绘制各个扇形分界线
                    drawPieDivider(canvas, startAngle, endAngle)
                }
                startAngle += sweepAngle
            }
        }
    }

    //绘制各个扇形的分界线
    private fun drawPieDivider(canvas: Canvas, startAngle: Float, endAngle: Float) {
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

    //绘制内部的圆圈
    private fun drawInside(canvas: Canvas) {
        if (showInside) {
            mInsidePaint.color = insideColor
            canvas.drawCircle(mCenterX, mCenterY, mRadius * insideRatio, mInsidePaint)
        }
    }

    //绘制底部各部分标签和数值
    private fun drawBottomDescription(canvas: Canvas) {
        if (mProgress == beginAngle + TOTAL_ANGLE) {
            if (mIsDataEmpty) {
                val fm = mEmptyTextPaint.getFontMetrics()
                canvas.drawText(
                    emptyText,
                    mWidth / 2f,
                    mHeight - mBottomHeight + itemMarginTop - fm.ascent,
                    mEmptyTextPaint
                )
            } else {
                var index = 0
                for (i in mDataList.indices) {
                    //跳过占比为0%的部分
                    if (mDataList.get(i).value == 0f) {
                        continue
                    }
                    mArcPaint.color = mDataList.get(i).color
                    mInsidePaint.color = pointInsideColor
                    if (index % 2 == 0) {
                        val y =
                            mHeight - mBottomHeight + itemMarginTop + (mItemHeight / 2f) + (index / 2) * (mItemHeight + itemSpacingExtra)
                        //绘制左侧圆弧
                        canvas.drawCircle(pointMarginLeft + pointSize / 2f, y, pointSize / 2f, mArcPaint)
                        canvas.drawCircle(pointMarginLeft + pointSize / 2f, y, pointSize / 4f, mInsidePaint)
                        //绘制标签
                        val labelFm = mLabelTextPaint.getFontMetrics()
                        val label = "${mDataList.get(i).label}:"
                        canvas.drawText(
                            label,
                            pointMarginLeft + pointSize + pointMarginRight,
                            y + (labelFm.descent - labelFm.ascent) / 2f - labelFm.descent, mLabelTextPaint
                        )
                        //绘制数值
                        val valueFm = mValueTextPaint.getFontMetrics()
                        canvas.drawText(
                            formatValue(mDataList.get(i).value),
                            pointMarginLeft + pointSize + pointMarginRight + mLabelTextPaint.measureText(label),
                            y + (valueFm.descent - valueFm.ascent) / 2f - valueFm.descent,
                            mValueTextPaint
                        )
                    } else {
                        val y =
                            mHeight - mBottomHeight + itemMarginTop + (mItemHeight / 2f) + (index - 1) / 2 * (mItemHeight + itemSpacingExtra)
                        //绘制左侧圆弧
                        canvas.drawCircle(mWidth / 2f + pointMarginLeft + pointSize / 2f, y, pointSize / 2f, mArcPaint)
                        canvas.drawCircle(
                            mWidth / 2f + pointMarginLeft + pointSize / 2f,
                            y,
                            pointSize / 4f,
                            mInsidePaint
                        )
                        //绘制标签
                        val labelFm = mLabelTextPaint.getFontMetrics()
                        val label = "${mDataList.get(i).label}:"
                        canvas.drawText(
                            label,
                            mWidth / 2f + pointMarginLeft + pointSize + pointMarginRight,
                            y + (labelFm.descent - labelFm.ascent) / 2f - labelFm.descent, mLabelTextPaint
                        )
                        //绘制数值
                        val valueFm = mValueTextPaint.getFontMetrics()
                        canvas.drawText(
                            formatValue(mDataList.get(i).value),
                            mWidth / 2f + pointMarginLeft + pointSize + pointMarginRight + mLabelTextPaint.measureText(
                                label
                            ),
                            y + (valueFm.descent - valueFm.ascent) / 2f - valueFm.descent,
                            mValueTextPaint
                        )
                    }
                    index++
                }
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
                //当某扇形的占比非常小，以至于比分界线更小时不绘制分界线
                val minRate =
                    Math.toDegrees(Math.asin((dividerWidth / (mRadius * insideRatio)).toDouble())) / 180f
                if (rate <= minRate && rate != 0f) {
                    showDividerLine = false
                }
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

    //设置高度
    fun setHeight(height: Int) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.height = height
        requestLayout()
    }

}