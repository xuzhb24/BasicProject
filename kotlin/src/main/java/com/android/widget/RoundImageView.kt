package com.android.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.android.universal.R
import kotlin.math.min

/**
 * Created by xuzhb on 2024/5/13
 * Desc:圆角/圆形ImageView
 */
class RoundImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttrs: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttrs) {

    companion object {
        private const val DEFAULT_IS_CIRCLE = false
        private const val DEFAULT_IS_COVER_SRC = false
        private const val DEFAULT_MASK_COLOR = Color.TRANSPARENT
        private const val DEFAULT_BORDER_WIDTH = 0f
        private const val DEFAULT_BORDER_COLOR = Color.TRANSPARENT
        private const val DEFAULT_INNER_BORDER_WIDTH = 0f
        private const val DEFAULT_INNER_BORDER_COLOR = Color.TRANSPARENT
        private const val DEFAULT_CORNER_RADIUS = 0f
        private const val DEFAULT_CORNER_TOP_LEFT_RADIUS = 0f
        private const val DEFAULT_CORNER_TOP_RIGHT_RADIUS = 0f
        private const val DEFAULT_CORNER_BOTTOM_LEFT_RADIUS = 0f
        private const val DEFAULT_CORNER_BOTTOM_RIGHT_RADIUS = 0f
        private const val DEFAULT_WIDTH_HEIGHT_RATIO = 0f
    }

    private var isCircle = DEFAULT_IS_CIRCLE        //是否显示为圆形，如果为圆形则设置的corner无效
    private var isCoverSrc = DEFAULT_IS_COVER_SRC   //border、inner_border是否覆盖图片
    private var maskColor = DEFAULT_MASK_COLOR      //遮罩颜色
    private var borderWidth = DEFAULT_BORDER_WIDTH  //边框宽度
    private var borderColor = DEFAULT_BORDER_COLOR  //边框颜色
    private var innerBorderWidth = DEFAULT_INNER_BORDER_WIDTH  //内层边框宽度
    private var innerBorderColor = DEFAULT_INNER_BORDER_COLOR  //内层边框颜色
    private var cornerRadius = DEFAULT_CORNER_RADIUS           //统一设置圆角半径，优先级高于单独设置每个角的半径
    private var cornerTopLeftRadius = DEFAULT_CORNER_TOP_LEFT_RADIUS          //左上角圆角半径
    private var cornerTopRightRadius = DEFAULT_CORNER_TOP_RIGHT_RADIUS        //右上角圆角半径
    private var cornerBottomLeftRadius = DEFAULT_CORNER_BOTTOM_LEFT_RADIUS    //左下角圆角半径
    private var cornerBottomRightRadius = DEFAULT_CORNER_BOTTOM_RIGHT_RADIUS  //右下角圆角半径
    private var widthHeightRatio = DEFAULT_WIDTH_HEIGHT_RATIO                 //宽高比

    fun setCircle(isCircle: Boolean) {
        this.isCircle = isCircle
        clearInnerBorderWidth()
        initSrcRectF()
        invalidate()
    }

    fun setCoverSrc(isCoverSrc: Boolean) {
        this.isCoverSrc = isCoverSrc
        initSrcRectF()
        invalidate()
    }

    fun setMaskColor(@ColorInt maskColor: Int) {
        this.maskColor = maskColor
        invalidate()
    }

    fun setBorderWidth(borderWidth: Float) {
        this.borderWidth = borderWidth
        calculateRadiiAndRectF(false)
    }

    fun setBorderColor(@ColorInt borderColor: Int) {
        this.borderColor = borderColor
        invalidate()
    }

    fun setInnerBorderWidth(innerBorderWidth: Float) {
        this.innerBorderWidth = innerBorderWidth
        clearInnerBorderWidth()
        invalidate()
    }

    fun setInnerBorderColor(@ColorInt innerBorderColor: Int) {
        this.innerBorderColor = innerBorderColor
        invalidate()
    }

    fun setCornerRadius(radius: Float) {
        this.cornerRadius = radius
        calculateRadiiAndRectF(false)
    }

    fun setCornerTopLeftRadius(radius: Float) {
        this.cornerTopLeftRadius = radius
        calculateRadiiAndRectF(true)
    }

    fun setCornerTopRightRadius(radius: Float) {
        this.cornerTopRightRadius = radius
        calculateRadiiAndRectF(true)
    }

    fun setCornerBottomLeftRadius(radius: Float) {
        this.cornerBottomLeftRadius = radius
        calculateRadiiAndRectF(true)
    }

    fun setCornerBottomRightRadius(radius: Float) {
        this.cornerBottomRightRadius = radius
        calculateRadiiAndRectF(true)
    }

    fun setWidthHeightRatio(ratio: Float) {
        this.widthHeightRatio = ratio
        invalidate()
    }

    private var mWidth = 0
    private var mHeight = 0
    private var mRadius = 0f
    private val mPaint: Paint
    private val mPath: Path
    private val mSrcPath: Path
    private val mBorderRadii: FloatArray
    private val mSrcRadii: FloatArray
    private val mBorderRectF: RectF
    private var mSrcRectF: RectF
    private val mXfermode: Xfermode

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
            isCircle = ta.getBoolean(R.styleable.RoundImageView_is_circle, DEFAULT_IS_CIRCLE)
            isCoverSrc = ta.getBoolean(R.styleable.RoundImageView_is_cover_src, DEFAULT_IS_COVER_SRC)
            maskColor = ta.getColor(R.styleable.RoundImageView_mask_color, DEFAULT_MASK_COLOR)
            borderWidth = ta.getDimension(R.styleable.RoundImageView_border_width, DEFAULT_BORDER_WIDTH)
            borderColor = ta.getColor(R.styleable.RoundImageView_border_color, DEFAULT_BORDER_COLOR)
            innerBorderWidth = ta.getDimension(R.styleable.RoundImageView_inner_border_width, DEFAULT_INNER_BORDER_WIDTH)
            innerBorderColor = ta.getColor(R.styleable.RoundImageView_inner_border_color, DEFAULT_INNER_BORDER_COLOR)
            cornerRadius = ta.getDimension(R.styleable.RoundImageView_corner_radius, DEFAULT_CORNER_RADIUS)
            cornerTopLeftRadius = ta.getDimension(R.styleable.RoundImageView_corner_top_left_radius, DEFAULT_CORNER_TOP_LEFT_RADIUS)
            cornerTopRightRadius = ta.getDimension(R.styleable.RoundImageView_corner_top_right_radius, DEFAULT_CORNER_TOP_RIGHT_RADIUS)
            cornerBottomLeftRadius = ta.getDimension(R.styleable.RoundImageView_corner_bottom_left_radius, DEFAULT_CORNER_BOTTOM_LEFT_RADIUS)
            cornerBottomRightRadius = ta.getDimension(R.styleable.RoundImageView_corner_bottom_right_radius, DEFAULT_CORNER_BOTTOM_RIGHT_RADIUS)
            widthHeightRatio = ta.getFloat(R.styleable.RoundImageView_width_height_ratio, DEFAULT_WIDTH_HEIGHT_RATIO)
            ta.recycle()
        }
        mPaint = Paint()
        mPath = Path()
        mSrcPath = Path()
        mBorderRadii = FloatArray(8)
        mSrcRadii = FloatArray(8)
        mBorderRectF = RectF()
        mSrcRectF = RectF()
        mXfermode = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        } else {
            PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }

        calculateRadii()
        clearInnerBorderWidth()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        initBorderRectF()
        initSrcRectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (widthHeightRatio > 0 && measuredWidth > 0) {
            setMeasuredDimension(measuredWidth, (measuredWidth / widthHeightRatio).toInt())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(mSrcRectF, null, Canvas.ALL_SAVE_FLAG)
        if (!isCoverSrc) {
            val sx = 1.0f * (mWidth - 2 * borderWidth - 2 * innerBorderWidth) / mWidth
            val sy = 1.0f * (mHeight - 2 * borderWidth - 2 * innerBorderWidth) / mHeight
            // 缩小画布，使图片内容不被border、padding覆盖
            canvas.scale(sx, sy, mWidth / 2.0f, mHeight / 2.0f)
        }
        super.onDraw(canvas)
        mPaint.reset()
        mPath.reset()
        if (isCircle) {
            mPath.addCircle(mWidth / 2.0f, mHeight / 2.0f, mRadius, Path.Direction.CCW)
        } else {
            mPath.addRoundRect(mSrcRectF, mSrcRadii, Path.Direction.CCW)
        }
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
        mPaint.xfermode = mXfermode
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(mPath, mPaint)
        } else {
            mSrcPath.reset()
            mSrcPath.addRect(mSrcRectF, Path.Direction.CCW)
            // 计算tempPath和path的差集
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mSrcPath.op(mPath, Path.Op.DIFFERENCE)
            }
            canvas.drawPath(mSrcPath, mPaint)
        }
        mPaint.xfermode = null

        // 绘制遮罩
        if (maskColor != 0) {
            mPaint.color = maskColor
            canvas.drawPath(mPath, mPaint)
        }
        // 恢复画布
        canvas.restore()
        // 绘制边框
        drawBorder(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        if (isCircle) {
            if (borderWidth > 0) {
                drawCircleBorder(canvas, borderWidth, borderColor, mRadius - borderWidth / 2.0f)
            }
            if (innerBorderWidth > 0) {
                drawCircleBorder(canvas, innerBorderWidth, innerBorderColor, mRadius - borderWidth - innerBorderWidth / 2.0f)
            }
        } else {
            if (borderWidth > 0) {
                drawRectFBorder(canvas, borderWidth, borderColor, mBorderRectF, mBorderRadii)
            }
        }
    }

    private fun drawCircleBorder(canvas: Canvas, borderWidth: Float, borderColor: Int, radius: Float) {
        initBorderPaint(borderWidth, borderColor)
        mPath.addCircle(mWidth / 2.0f, mHeight / 2.0f, radius, Path.Direction.CCW)
        canvas.drawPath(mPath, mPaint)
    }

    private fun drawRectFBorder(canvas: Canvas, borderWidth: Float, borderColor: Int, rectF: RectF, radii: FloatArray) {
        initBorderPaint(borderWidth, borderColor)
        mPath.addRoundRect(rectF, radii, Path.Direction.CCW)
        canvas.drawPath(mPath, mPaint)
    }

    private fun initBorderPaint(borderWidth: Float, borderColor: Int) {
        mPath.reset()
        with(mPaint) {
            strokeWidth = borderWidth
            color = borderColor
            style = Paint.Style.STROKE
        }
    }

    //计算外边框的RectF
    private fun initBorderRectF() {
        if (!isCircle) {
            mBorderRectF.set(borderWidth / 2f, borderWidth / 2f, mWidth - borderWidth / 2f, mHeight - borderWidth / 2f)
        }
    }

    //计算图片原始区域的RectF
    private fun initSrcRectF() {
        if (isCircle) {
            mRadius = min(mWidth, mHeight) / 2f
            mSrcRectF.set(mWidth / 2f - mRadius, mHeight / 2f - mRadius, mWidth / 2f + mRadius, mHeight / 2f + mRadius)
        } else {
            mSrcRectF.set(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
            if (isCoverSrc) {
                mSrcRectF = mBorderRectF
            }
        }
    }

    //计算RectF的圆角半径
    private fun calculateRadii() {
        if (isCircle) {
            return
        }
        if (cornerRadius > 0) {
            for (i in mBorderRadii.indices) {
                mBorderRadii[i] = cornerRadius
                mSrcRadii[i] = cornerRadius - borderWidth / 2f
            }
        } else {
            mBorderRadii[0] = cornerTopLeftRadius.also { mBorderRadii[1] = it }
            mBorderRadii[2] = cornerTopRightRadius.also { mBorderRadii[3] = it }
            mBorderRadii[4] = cornerBottomRightRadius.also { mBorderRadii[5] = it }
            mBorderRadii[6] = cornerBottomLeftRadius.also { mBorderRadii[7] = it }

            mSrcRadii[0] = cornerTopLeftRadius.also { mSrcRadii[1] = it - borderWidth / 2f }
            mSrcRadii[2] = cornerTopRightRadius.also { mSrcRadii[3] = it - borderWidth / 2f }
            mSrcRadii[4] = cornerBottomRightRadius.also { mSrcRadii[5] = it - borderWidth / 2f }
            mSrcRadii[6] = cornerBottomLeftRadius.also { mSrcRadii[7] = it - borderWidth / 2f }
        }
    }

    private fun calculateRadiiAndRectF(reset: Boolean) {
        if (reset) {
            cornerRadius = 0f
        }
        calculateRadii()
        initBorderRectF()
        invalidate()
    }

    //目前圆角矩形情况下不支持inner_border，需要将其置0
    private fun clearInnerBorderWidth() {
        if (!isCircle) {
            innerBorderWidth = 0f
        }
    }

}