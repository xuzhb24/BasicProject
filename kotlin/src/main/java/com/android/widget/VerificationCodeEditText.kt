package com.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.InputType
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2022/12/20
 * Desc:带下划线的验证码输入框
 */
class VerificationCodeEditText(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    companion object {
        private val DEFAULT_LINE_COLOR = Color.parseColor("#999999")
        private val DEFAULT_LINE_HEIGHT = SizeUtil.dp2px(1f)
        private val DEFAULT_LINE_PADDING = SizeUtil.dp2px(16f)
        private val DEFAULT_TEXT_PADDING_BOTTOM = SizeUtil.sp2px(8f)
        private val DEFAULT_TEXT_COUNT = 6
    }


    var lineColor: Int = DEFAULT_LINE_COLOR              //下划线的颜色
    var lineHeight: Float = DEFAULT_LINE_HEIGHT          //下划线的高度
    var linePadding: Float = DEFAULT_LINE_PADDING        //下划线与线之间的间隔
    var textPaddingBottom = DEFAULT_TEXT_PADDING_BOTTOM  //文本到下划线的距离
    var textCount: Int = DEFAULT_TEXT_COUNT              //文本个数
        set(value) {
            val count = if (value <= 0) DEFAULT_TEXT_COUNT else value
            field = count
        }

    //清空所有输入的内容
    fun clearText() {
        setText("")
        inputType = InputType.TYPE_CLASS_NUMBER
    }

    private lateinit var mText: String          //输入的内容
    private lateinit var mLinePaint: Paint      //绘制下划线
    private lateinit var mTextPaint: TextPaint  //绘制文本

    private var lineWidth: Float = 0f

    init {
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
    }

    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeEditText)
        lineColor = ta.getColor(R.styleable.VerificationCodeEditText_lineColor, DEFAULT_LINE_COLOR)
        lineHeight = ta.getDimension(R.styleable.VerificationCodeEditText_lineHeight, DEFAULT_LINE_HEIGHT)
        linePadding = ta.getDimension(R.styleable.VerificationCodeEditText_linePadding, DEFAULT_LINE_PADDING)
        textPaddingBottom = ta.getDimension(R.styleable.VerificationCodeEditText_textPaddingBottom, DEFAULT_TEXT_PADDING_BOTTOM)
        textCount = ta.getInt(R.styleable.VerificationCodeEditText_textCount, DEFAULT_TEXT_COUNT)
        if (textCount <= 0) {
            textCount = DEFAULT_TEXT_COUNT
        }
        ta.recycle()
    }

    private fun initPaint() {
        mTextPaint = TextPaint()
        with(mTextPaint) {
            isAntiAlias = true
            color = (this@VerificationCodeEditText).currentTextColor
            textSize = (this@VerificationCodeEditText).textSize
            textAlign = Paint.Align.CENTER
            isFakeBoldText = (this@VerificationCodeEditText).typeface.style == Typeface.BOLD
        }
        mLinePaint = Paint()
        with(mLinePaint) {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = lineColor
            strokeWidth = lineHeight
        }
        mText = ""
        setTextColor(Color.TRANSPARENT)  //设置用户输入的内容透明
        inputType = InputType.TYPE_CLASS_NUMBER
        setBackgroundDrawable(null)
        isLongClickable = false
        setTextIsSelectable(false)
        isCursorVisible = false
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().length <= textCount) {
            mText = text.toString()
        } else {
            setText(mText)
            setSelection(getText().toString().length)  //光标制动到最后
            //调用setText(mText)之后键盘会还原，再次把键盘设置为数字键盘
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        mOnTextChangeListener?.invoke(mText)
        if (text.toString().length == textCount) {
            mOnTextCompleteListener?.invoke(mText)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSpec(widthMeasureSpec, SizeUtil.dp2pxInt(300f)), measureSpec(heightMeasureSpec, SizeUtil.dp2pxInt(40f)))
    }

    private fun measureSpec(measureSpec: Int, minSize: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = minSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        lineWidth = (width.toFloat() - (textCount - 1) * linePadding) / textCount.toFloat()
        //绘制下划线
        for (i in 0 until textCount) {
            canvas.drawLine(i * (lineWidth + linePadding), height.toFloat() - lineHeight / 2f, i * (lineWidth + linePadding) + lineWidth, height.toFloat() - lineHeight / 2f, mLinePaint)
        }
        //绘制文本
        for (i in mText.indices) {
            canvas.drawText(mText[i].toString(), i * (lineWidth + linePadding) + lineWidth / 2f, height.toFloat() - lineHeight - textPaddingBottom, mTextPaint)
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