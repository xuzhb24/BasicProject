package com.android.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import com.android.universal.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2022/7/24
 * Desc:虚线
 */
class DashLine @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_DASH_WIDTH = SizeUtil.dp2px(2f)
        private val DEFAULT_DASH_GAP = SizeUtil.dp2px(2f)
        private val DEFAULT_DASH_COLOR = Color.BLACK
        private const val ORIENTATION_HORIZONTAL = 0
    }

    var dashWidth: Float = DEFAULT_DASH_WIDTH      //虚线的宽度
    var dashGap: Float = DEFAULT_DASH_GAP          //虚线的间隔
    var dashColor: Int = DEFAULT_DASH_COLOR        //虚线的颜色
    var orientation: Int = ORIENTATION_HORIZONTAL  //虚线的方向

    private var mPaint: Paint
    private var mPath: Path

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.DashLine)
            dashWidth = ta.getDimension(R.styleable.DashLine_dashWidth, DEFAULT_DASH_WIDTH)
            dashGap = ta.getDimension(R.styleable.DashLine_dashGap, DEFAULT_DASH_GAP)
            dashColor = ta.getColor(R.styleable.DashLine_dashColor, DEFAULT_DASH_COLOR)
            orientation = ta.getInt(R.styleable.DashLine_orientation, ORIENTATION_HORIZONTAL)
            ta.recycle()
        }
        mPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = dashColor
            pathEffect = DashPathEffect(floatArrayOf(dashWidth, dashGap, dashWidth, dashGap), 0f)
        }
        mPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (orientation == ORIENTATION_HORIZONTAL) {
            setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), measure(heightMeasureSpec))
        } else {
            setMeasuredDimension(measure(widthMeasureSpec), getDefaultSize(suggestedMinimumHeight, heightMeasureSpec))
        }
    }

    private fun measure(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = SizeUtil.dp2pxInt(1f)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (orientation == ORIENTATION_HORIZONTAL) {  //水平方向
            mPaint.strokeWidth = height.toFloat()
            mPath.moveTo(0f, height.toFloat() / 2f)
            mPath.lineTo(width.toFloat(), height.toFloat() / 2f)
        } else {  //竖直方向
            mPaint.strokeWidth = width.toFloat()
            mPath.moveTo(width.toFloat() / 2f, 0f)
            mPath.lineTo(width.toFloat() / 2f, height.toFloat())
        }
        canvas?.drawPath(mPath, mPaint)
    }

}