package com.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil
import java.util.regex.Pattern
import kotlin.math.max

/**
 * Created by xuzhb on 2020/8/22
 * Desc:带有后缀的TextView
 */
class SuffixTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        private val DEFAULT_MAX_SHOW_LINES = 2
        private val DEFAULT_LINE_SPACING = SizeUtil.dp2px(2f)
        private val DEFAULT_CONTENT_SUFFIX_TEXT = "..."
        private val DEFAULT_CONTENT_TEXT_SIZE = SizeUtil.sp2px(15f)
        private val DEFAULT_CONTENT_TEXT_COLOR = Color.BLACK
        private val DEFAULT_SUFFIX_TEXT = "查看详情"
        private val DEFAULT_SUFFIX_TEXT_SIZE = SizeUtil.sp2px(15f)
        private val DEFAULT_SUFFIX_TEXT_COLOR = Color.BLACK
        private val DEFAULT_SUFFIX_MARGIN_LEFT = SizeUtil.dp2px(5f)
        private val DEFAULT_SUFFIX_MARGIN_RIGHT = SizeUtil.dp2px(5f)
    }

    var maxShowLines: Int = DEFAULT_MAX_SHOW_LINES  //最多显示的文本行数
    var lineSpacing: Float = DEFAULT_LINE_SPACING   //文本的行间距
    var contentText: String = ""                    //主内容的文本
        set(value) {
            field = getFilterText(value)
            bottom = calculateHeight(width)
            requestLayout()
            invalidate()
        }
    var contentSuffixText: String = DEFAULT_CONTENT_SUFFIX_TEXT  //主内容的文本后缀，一般是...
    var contentTextSize: Float = DEFAULT_CONTENT_TEXT_SIZE       //主内容的文本字体大小
    var contentTextColor: Int = DEFAULT_CONTENT_TEXT_COLOR       //主内容的文本字体颜色
    var suffixText: String = DEFAULT_SUFFIX_TEXT                 //后缀的文本
    var suffixTextSize: Float = DEFAULT_SUFFIX_TEXT_SIZE         //后缀的文本字体大小
    var suffixTextColor: Int = DEFAULT_SUFFIX_TEXT_COLOR         //后缀的文本字体颜色
    var suffixMarginLeft: Float = DEFAULT_SUFFIX_MARGIN_LEFT     //后缀的文本的左边距
    var suffixMarginRight: Float = DEFAULT_SUFFIX_MARGIN_RIGHT   //后缀的文本的右边距

    private lateinit var mContentPaint: TextPaint
    private lateinit var mSuffixPaint: TextPaint
    private var mSuffixRectF: RectF = RectF()
    private var mOnTexClickListener: OnTextClickListener? = null
    private var isDownInSuffix = false  //手指是否按在后缀内

    init {
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
    }

    //获取布局属性并设置属性默认值
    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SuffixTextView)
        maxShowLines = ta.getInteger(R.styleable.SuffixTextView_maxShowLines, DEFAULT_MAX_SHOW_LINES)
        lineSpacing = ta.getDimension(R.styleable.SuffixTextView_lineSpacing, DEFAULT_LINE_SPACING)
        contentText = ta.getString(R.styleable.SuffixTextView_contentText) ?: ""
        contentSuffixText = ta.getString(R.styleable.SuffixTextView_contentSuffixText) ?: DEFAULT_CONTENT_SUFFIX_TEXT
        contentTextSize = ta.getDimension(R.styleable.SuffixTextView_contentTextSize, DEFAULT_CONTENT_TEXT_SIZE)
        contentTextColor = ta.getColor(R.styleable.SuffixTextView_contentTextColor, DEFAULT_CONTENT_TEXT_COLOR)
        suffixText = ta.getString(R.styleable.SuffixTextView_suffixText) ?: DEFAULT_SUFFIX_TEXT
        suffixTextSize = ta.getDimension(R.styleable.SuffixTextView_suffixTextSize, DEFAULT_SUFFIX_TEXT_SIZE)
        suffixTextColor = ta.getColor(R.styleable.SuffixTextView_suffixTextColor, DEFAULT_SUFFIX_TEXT_COLOR)
        suffixMarginLeft = ta.getDimension(R.styleable.SuffixTextView_suffixMarginLeft, DEFAULT_SUFFIX_MARGIN_LEFT)
        suffixMarginRight = ta.getDimension(R.styleable.SuffixTextView_suffixMarginRight, DEFAULT_SUFFIX_MARGIN_RIGHT)
        ta.recycle()
    }

    //初始化画笔
    private fun initPaint() {
        mContentPaint = TextPaint()
        with(mContentPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = contentTextColor
            textSize = contentTextSize
        }
        mSuffixPaint = TextPaint()
        with(mSuffixPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = suffixTextColor
            textSize = suffixTextSize
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        when (heightMode) {
            MeasureSpec.EXACTLY -> height = height
            else -> height = calculateHeight(width).toInt()
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawText(it)
        }
    }

    private fun drawText(canvas: Canvas) {
        if (TextUtils.isEmpty(contentText)) {
            canvas.drawText("", 0f, getBaseLine(mContentPaint, 0f), mContentPaint)
            return
        }
        val staticLayout = getStaticLayout(contentText, mContentPaint, width)
        val lineCount = staticLayout.lineCount
        val suffixStaticLayout = getStaticLayout(suffixText, mSuffixPaint, width)
        val suffixStaticLayoutWidth = suffixStaticLayout.getLineWidth(0)
        val suffixStaticLayoutHeight = suffixStaticLayout.height
        if (lineCount < maxShowLines) {
            var height = 0f
            for (i in 0 until lineCount) {
                //获取当前行内容
                val currentLineText = contentText.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mContentPaint, width)
                if (i == lineCount - 1) {  //最后一行
                    val currentLineWidth = currentLineStaticLayout.getLineWidth(0)
                    if (currentLineWidth + suffixMarginLeft + suffixStaticLayoutWidth > width) {  //后缀需要换行
                        height += currentLineStaticLayout.height
                        canvas.drawText(currentLineText, 0f, getBaseLine(mContentPaint, height), mContentPaint)  //绘制主内容
                        height += lineSpacing
                        mSuffixRectF.set(0f, height, suffixStaticLayoutWidth, height + suffixStaticLayoutHeight)
                        height += suffixStaticLayoutHeight
                        canvas.drawText(suffixText, 0f, getBaseLine(mSuffixPaint, height), mSuffixPaint)  //绘制后缀
                    } else {
                        var currentWidth = 0f
                        //绘制主内容
                        canvas.drawText(
                            currentLineText,
                            currentWidth,
                            getBaseLine(mContentPaint, height + currentLineStaticLayout.height),
                            mContentPaint
                        )
                        currentWidth += currentLineWidth
                        currentWidth += suffixMarginLeft
                        //绘制后缀
                        canvas.drawText(
                            suffixText,
                            currentWidth,
                            getBaseLine(mSuffixPaint, height + suffixStaticLayoutHeight),
                            mSuffixPaint
                        )
                        mSuffixRectF.set(
                            currentWidth,
                            height,
                            currentWidth + suffixStaticLayoutWidth,
                            height + suffixStaticLayoutHeight
                        )
                    }
                } else {
                    height += currentLineStaticLayout.height
                    canvas.drawText(currentLineText, 0f, getBaseLine(mContentPaint, height), mContentPaint)
                    height += lineSpacing
                }
            }
        } else {
            var height = 0f
            for (i in 0 until maxShowLines) {
                //获取当前行内容
                val currentLineText = contentText.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mContentPaint, width)
                val currentLineWidth = currentLineStaticLayout.getLineWidth(0)
                if (i == maxShowLines - 1) {  //最后一行
                    //需要计算后缀的内容信息
                    val contentSuffixStaticLayout = getStaticLayout(contentSuffixText, mContentPaint, width)
                    val contentSuffixStaticLayoutWidth = contentSuffixStaticLayout.getLineWidth(0)
                    //主内容可以进行绘制的最大宽度
                    val contentTextWidth =
                        width - suffixStaticLayoutWidth - suffixMarginLeft - contentSuffixStaticLayoutWidth - suffixMarginRight
                    //临时的一个高度，表达当前主内容的y值
                    val tempHeight = height + currentLineStaticLayout.height
                    if (contentTextWidth > currentLineWidth) {  //不需要绘制内容文本的后缀
                        //当前正在绘制的宽度
                        var currentWidth = 0f
                        canvas.drawText(currentLineText, currentWidth, getBaseLine(mContentPaint, tempHeight), mContentPaint)
                        //绘制后缀
                        currentWidth += currentLineWidth
                        currentWidth += suffixMarginLeft
                        canvas.drawText(
                            suffixText,
                            currentWidth,
                            getBaseLine(mSuffixPaint, height + suffixStaticLayoutHeight),
                            mSuffixPaint
                        )
                        //设置其响应的区域范围
                        mSuffixRectF.set(
                            currentWidth,
                            height,
                            currentWidth + suffixStaticLayoutWidth,
                            height + suffixStaticLayoutHeight
                        )
                    } else {  //需要绘制内容文本的后缀
                        //当前正在绘制的宽度
                        var currentWidth = 0f
                        for (text in currentLineText) {
                            val currentString = text.toString()
                            val currentStringStaticLayout = getStaticLayout(currentString, mContentPaint, width)
                            val currentStringStaticLayoutWidth = currentStringStaticLayout.getLineWidth(0)
                            if (currentWidth + currentStringStaticLayoutWidth > contentTextWidth) {
                                //这个文字不能继续绘制了，开始绘制内容文本的后缀
                                //绘制内容文本的后缀信息
                                canvas.drawText(
                                    contentSuffixText,
                                    currentWidth,
                                    getBaseLine(mContentPaint, tempHeight),
                                    mContentPaint
                                )
                                currentWidth += contentSuffixStaticLayoutWidth
                                currentWidth += suffixMarginLeft
                                //绘制后缀
                                canvas.drawText(
                                    suffixText,
                                    currentWidth,
                                    getBaseLine(mSuffixPaint, height + suffixStaticLayoutHeight),
                                    mSuffixPaint
                                )
                                //设置其响应的区域范围
                                mSuffixRectF.set(
                                    currentWidth,
                                    height,
                                    currentWidth + suffixStaticLayoutWidth,
                                    height + suffixStaticLayoutHeight
                                )
                                return
                            } else {
                                //绘制主内容
                                canvas.drawText(
                                    currentString,
                                    currentWidth,
                                    getBaseLine(mContentPaint, tempHeight),
                                    mContentPaint
                                )
                                currentWidth += currentStringStaticLayoutWidth
                            }
                        }
                    }
                } else {  //普通行
                    height += currentLineStaticLayout.height
                    canvas.drawText(currentLineText, 0f, getBaseLine(mContentPaint, height), mContentPaint)
                    height += lineSpacing
                }
            }
        }
    }

    //获取文本绘制的基准线
    private fun getBaseLine(paint: TextPaint, descent: Float): Float {
        val fm = paint.fontMetrics
        return descent - fm.descent
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = it.x
            val y = it.y
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (mOnTexClickListener != null) {
                        isDownInSuffix = mSuffixRectF.contains(x, y)
                        return true
                    } else {
                        isDownInSuffix = false
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (isDownInSuffix) {
                        if (mSuffixRectF.contains(x, y)) {
                            mOnTexClickListener?.onSuffixTextClick()  //点击了后缀
                            return true
                        } else {  //滑到其他区域了

                        }
                    } else {
                        mOnTexClickListener?.onContentTextClick()
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    //剔除文本中的一些特殊字符，特殊字符用空字符串来替换
    private fun getFilterText(text: String?): String {
        if (TextUtils.isEmpty(text)) {
            return ""
        }
        val p = Pattern.compile("\\s*|\t|\r|\n")
        val m = p.matcher(text)
        return m.replaceAll("")
    }

    //计算高度
    private fun calculateHeight(width: Int): Int {
        if (TextUtils.isEmpty(contentText)) {  //无内容
            return 0
        }
        if (width <= 0) {
            return 0
        }
        val staticLayout = getStaticLayout(contentText, mContentPaint, width)
        val lineCount = staticLayout.lineCount
        val suffixStaticLayout = getStaticLayout(suffixText, mSuffixPaint, width)
        val suffixStaticLayoutWidth = suffixStaticLayout.getLineWidth(0)
        val suffixStaticLayoutHeight = suffixStaticLayout.height
        var height = 0f
        if (lineCount < maxShowLines) {
            for (i in 0 until lineCount) {
                //获取当前行内容
                val currentLineText = contentText.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mContentPaint, width)
                if (i == lineCount - 1) {  //最后一行

                    val currentLineWidth = currentLineStaticLayout.getLineWidth(0)
                    if (currentLineWidth + suffixMarginLeft + suffixStaticLayoutWidth > width) {  //后缀需要换行
                        //需要另起一行
                        height += currentLineStaticLayout.height + lineSpacing
                        height += suffixStaticLayoutHeight //+ lineSpacing
                    } else {
                        //取最高的那个
                        height += max(currentLineStaticLayout.height, suffixStaticLayoutHeight) //+ lineSpacing
                    }
                } else {  //普通行
                    height += currentLineStaticLayout.height + lineSpacing
                }
            }
        } else {
            for (i in 0 until maxShowLines) {
                //获取当前行内容
                val currentLineText = contentText.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mContentPaint, width)
                if (i == maxShowLines - 1) {  //最后一行
                    //计算最大行那一行的内容
                    height += max(currentLineStaticLayout.height, suffixStaticLayoutHeight) //+ lineSpacing
                } else {  //普通行
                    height += currentLineStaticLayout.height + lineSpacing
                }
            }
        }
        return height.toInt()
    }

    private fun getStaticLayout(text: String, paint: TextPaint, width: Int): StaticLayout =
        StaticLayout(
            text, 0, text.length, paint, width, Layout.Alignment.ALIGN_NORMAL,
            1f, 0f, false
        )

    fun setOnTextClickListener(listener: OnTextClickListener) {
        this.mOnTexClickListener = listener
    }

    interface OnTextClickListener {

        //点击内容文本时回调
        fun onContentTextClick()

        //点击后缀文本时回调
        fun onSuffixTextClick()

    }

}