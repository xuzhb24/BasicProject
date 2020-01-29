package com.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.widget.AppCompatEditText
import android.text.InputType
import android.util.AttributeSet
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/1/29
 * Desc:密码输入框
 */
class PasswordEditText(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    companion object {
        private val DEFAULT_FRAME_SIZE = SizeUtil.dp2px(50f)
        private val DEFAULT_FRAME_STROKE_WIDTH = SizeUtil.dp2px(1f)
        private val DEFAULT_FRAME_CORNER = SizeUtil.dp2px(3f)
        private val DEFAULT_FRAME_COLOR = Color.parseColor("#D6D8DA")
        private val DEFAULT_FRAME_BACKGROUND_COLOR = Color.TRANSPARENT
        private val DEFAULT_FRAME_MARGIN = SizeUtil.dp2px(3.5f)
        private val DEFAULT_DOT_COLOR = Color.BLACK
        private val DEFAULT_DOT_RADIUS = SizeUtil.dp2px(5f)
        private val DEFAULT_PASSWORD_COUNT = 6
    }

    var frameSize: Float = DEFAULT_FRAME_SIZE                         //边框的大小，即长宽
    var frameStrokeWidth: Float = DEFAULT_FRAME_STROKE_WIDTH         //边框的宽度
    var frameCorner: Float = DEFAULT_FRAME_CORNER                     //边框的圆角
    var frameColor: Int = DEFAULT_FRAME_COLOR                         //边框的颜色
    var frameBackgroundColor: Int = DEFAULT_FRAME_BACKGROUND_COLOR  //边框的背景色
    var frameMargin: Float = DEFAULT_FRAME_MARGIN                    //边框的左右间距
    var dotColor: Int = DEFAULT_DOT_COLOR                            //圆圈颜色
    var dotRadius: Float = DEFAULT_DOT_RADIUS                        //圈圈半径
    var passwordCount: Int = DEFAULT_PASSWORD_COUNT                 //密码个数
        set(value) {
            val count = if (value <= 0) DEFAULT_PASSWORD_COUNT else value
            field = count
        }

    //清空所有输入的内容
    fun clearText() {
        setText("")
        inputType = InputType.TYPE_CLASS_NUMBER
    }

    private lateinit var mFramePaint: Paint              //绘制边框
    private lateinit var mBgPaint: Paint                 //绘制边框内部的背景
    private lateinit var mDotPaint: Paint                //绘制圆圈
    private lateinit var mRectFList: MutableList<RectF>  //各个边框对应的RectF
    private lateinit var mText: String                   //输入的内容

    init {
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
    }

    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)
        frameSize = ta.getDimension(R.styleable.PasswordEditText_frameSize, DEFAULT_FRAME_SIZE)
        frameStrokeWidth = ta.getDimension(R.styleable.PasswordEditText_frameStrokeWidth, DEFAULT_FRAME_STROKE_WIDTH)
        frameCorner = ta.getDimension(R.styleable.PasswordEditText_frameCorner, DEFAULT_FRAME_CORNER)
        frameColor = ta.getColor(R.styleable.PasswordEditText_frameColor, DEFAULT_FRAME_COLOR)
        frameBackgroundColor =
            ta.getColor(R.styleable.PasswordEditText_frameBackgroundColor, DEFAULT_FRAME_BACKGROUND_COLOR)
        frameMargin = ta.getDimension(R.styleable.PasswordEditText_frameMargin, DEFAULT_FRAME_MARGIN)
        dotColor = ta.getColor(R.styleable.PasswordEditText_dotColor, DEFAULT_DOT_COLOR)
        dotRadius = ta.getDimension(R.styleable.PasswordEditText_dotRadius, DEFAULT_DOT_RADIUS)
        passwordCount = ta.getInt(R.styleable.PasswordEditText_passwordCount, DEFAULT_PASSWORD_COUNT)
        if (passwordCount <= 0) {
            passwordCount = DEFAULT_PASSWORD_COUNT
        }
        ta.recycle()
    }

    private fun initPaint() {
        mFramePaint = Paint()
        with(mFramePaint) {
            isAntiAlias = true
            strokeWidth = frameStrokeWidth
            style = Paint.Style.STROKE
            color = frameColor
        }
        mBgPaint = Paint()
        with(mBgPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = frameBackgroundColor
        }
        mDotPaint = Paint()
        with(mDotPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = dotColor
        }
        mRectFList = mutableListOf();
        mText = ""
        for (i in 0..passwordCount - 1) {
            val rectF = RectF(
                i * (frameSize + 2 * frameMargin) + frameMargin + frameStrokeWidth / 2f,
                frameStrokeWidth / 2f,
                i * (frameSize + 2 * frameMargin) + frameMargin + frameStrokeWidth / 2f + frameSize,
                frameStrokeWidth / 2f + frameSize
            )
            mRectFList.add(rectF)
        }
        setTextColor(Color.TRANSPARENT)  //设置用户输入的内容透明
        inputType = InputType.TYPE_CLASS_NUMBER
        setBackgroundDrawable(null)
        isLongClickable = false
        setTextIsSelectable(false)
        isCursorVisible = false
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().length <= passwordCount) {
            mText = text.toString()
        } else {
            setText(mText)
            setSelection(getText().toString().length)  //光标制动到最后
            //调用setText(mText)之后键盘会还原，再次把键盘设置为数字键盘
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        mOnTextChangeListener?.invoke(mText)
        if (text.toString().length == passwordCount) {
            mOnTextCompleteListener?.invoke(mText)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {  //支持wrap_content属性
            widthSize = (passwordCount * (frameSize + 2 * frameMargin) + frameStrokeWidth).toInt() + 1
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.AT_MOST) {  //支持wrap_content属性
            heightSize = Math.max(SizeUtil.dp2px(50f).toInt(), (frameSize + frameStrokeWidth).toInt() + 1)  //不小于50dp
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        if (frameMargin > 0f) {  //如果水平间距小于0，那只显示第一个边框的左上和左下圆角和最后一个边框的右上和右下圆角就好
            for (rectF in mRectFList) {
                if (frameBackgroundColor != Color.TRANSPARENT) {
                    canvas.drawRoundRect(rectF, frameCorner, frameCorner, mBgPaint)  //绘制边框背景颜色
                }
                canvas.drawRoundRect(rectF, frameCorner, frameCorner, mFramePaint)  //绘制边框
            }
        } else {  //如果水平间距小于0，那只显示第一个边框的左上和左下圆角和最后一个边框的右上和右下圆角就好
            if (mRectFList.size == 1) {
                for (rectF in mRectFList) {
                    if (frameBackgroundColor != Color.TRANSPARENT) {
                        canvas.drawRoundRect(rectF, frameCorner, frameCorner, mBgPaint)  //绘制边框背景颜色
                    }
                    canvas.drawRoundRect(rectF, frameCorner, frameCorner, mFramePaint)  //绘制边框
                }
            } else if (mRectFList.size > 1) {
                val rectF = RectF(
                    mRectFList[0].left, mRectFList[0].top,
                    mRectFList[mRectFList.size - 1].right, mRectFList[0].bottom
                )
                if (frameBackgroundColor != Color.TRANSPARENT) {
                    canvas.drawRoundRect(rectF, frameCorner, frameCorner, mBgPaint)  //绘制边框背景颜色
                }
                canvas.drawRoundRect(rectF, frameCorner, frameCorner, mFramePaint)  //绘制边框
                for (i in 0..mRectFList.size - 2) {
                    canvas.drawLine(
                        mRectFList[i].right, mRectFList[i].top,
                        mRectFList[i].right, mRectFList[i].bottom, mFramePaint
                    )
                }
            }
        }
        for (i in 0..mText.length - 1) {
            canvas.drawCircle(mRectFList[i].centerX(), mRectFList[i].centerY(), dotRadius, mDotPaint)  //绘制圆圈
        }
    }

    //输入监听
    private var mOnTextChangeListener: ((text: String) -> Unit)? = null

    fun setOnTextChangeListener(listener: (text: String) -> Unit) {
        this.mOnTextChangeListener = listener
    }

    //输入完成监听
    private var mOnTextCompleteListener: ((text: String) -> Unit)? = null

    fun setOnTextCompleteListener(listener: (text: String) -> Unit) {
        this.mOnTextCompleteListener = listener
    }

}