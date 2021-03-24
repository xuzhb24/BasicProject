package com.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil
import java.util.regex.Pattern

/**
 * Created by xuzhb on 2021/3/24
 * Desc:文本两端分散对齐的TextView
 */
class AlignTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SUFFIX_TEXT = "...."
        private val DEFAULT_TEXT = ""
        private val DEFAULT_TEXT_SIZE = SizeUtil.sp2px(15f)
        private val DEFAULT_TEXT_COLOR = Color.GRAY
        private val DEFAULT_MAX_LINES = Int.MAX_VALUE
        private val DEFAULT_LINE_SPACING_EXTRA = SizeUtil.dp2px(1f)
    }

    var text: String = DEFAULT_TEXT
        set(value) {
            field = getFilterText(value)
            bottom = calculateHeight(width)
            requestLayout()
            invalidate()
        }
    var textSize: Float = DEFAULT_TEXT_SIZE
    var textColor: Int = DEFAULT_TEXT_COLOR
    var maxLines: Int = DEFAULT_MAX_LINES
    var lineSpacingExtra: Float = DEFAULT_LINE_SPACING_EXTRA

    private lateinit var mPaint: TextPaint
    private var isAligin = true //文本是否两端分散对齐

    init {
        attrs?.let {
            parseAttribute(getContext(), it)
        }
        initPaint()
    }

    //获取布局属性并设置属性默认值
    private fun parseAttribute(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView)
        text = ta.getString(R.styleable.AlignTextView_android_text) ?: ""
        textSize = ta.getDimension(R.styleable.AlignTextView_android_textSize, DEFAULT_TEXT_SIZE)
        textColor = ta.getColor(R.styleable.AlignTextView_android_textColor, DEFAULT_TEXT_COLOR)
        maxLines = ta.getInteger(R.styleable.AlignTextView_android_maxLines, DEFAULT_MAX_LINES)
        lineSpacingExtra = ta.getDimension(R.styleable.AlignTextView_android_lineSpacingExtra, DEFAULT_LINE_SPACING_EXTRA)
        ta.recycle()
    }

    //初始化画笔
    private fun initPaint() {
        mPaint = TextPaint()
        with(mPaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = textColor
        }
        mPaint.textSize = textSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        when (heightMode) {
            MeasureSpec.EXACTLY -> height = height
            else -> height = calculateHeight(width)
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
        if (TextUtils.isEmpty(text)) {
            canvas.drawText("", 0f, getBaseLine(mPaint, 0f), mPaint)
            return
        }
        val staticLayout = getStaticLayout(text, mPaint, width)
        val lineCount = staticLayout.lineCount
        if (lineCount <= maxLines) {  //全部绘制
            var height = 0f
            for (i in 0 until lineCount) {
                val currentLineText = text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mPaint, width)
                height += currentLineStaticLayout.height
                if (isAligin && i != lineCount - 1) {
                    drawAlignText(canvas, currentLineText, getBaseLine(mPaint, height), mPaint)
                } else {
                    canvas.drawText(currentLineText, 0f, getBaseLine(mPaint, height), mPaint)
                }
                height += lineSpacingExtra
            }
        } else {  //在最后一行绘制...后缀
            var height = 0f
            for (i in 0 until maxLines) {
                //获取当前行内容
                val currentLineText = text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mPaint, width)
                if (i == maxLines - 1) {  //最后一行
                    //需要计算后缀的内容信息
                    val contentSuffixStaticLayout = getStaticLayout(DEFAULT_SUFFIX_TEXT, mPaint, width)
                    val contentSuffixStaticLayoutWidth = contentSuffixStaticLayout.getLineWidth(0)
                    //主内容可以进行绘制的宽度
                    val contentTextWidth = width - contentSuffixStaticLayoutWidth
                    //当前正在绘制的宽度
                    var currentWidth = 0f
                    //临时的一个高度，表达当前主内容的y值
                    val tempHeight = height + currentLineStaticLayout.height
                    for (text in currentLineText) {
                        val currentString = text.toString()
                        val currentStringStaticLayout = getStaticLayout(currentString, mPaint, width)
                        val currentStringStaticLayoutWidth = currentStringStaticLayout.getLineWidth(0)
                        if (currentWidth + currentStringStaticLayoutWidth > contentTextWidth) {
                            //这个文字不能继续绘制了，开始绘制标签
                            //绘制后缀信息
                            canvas.drawText(DEFAULT_SUFFIX_TEXT, currentWidth, getBaseLine(mPaint, tempHeight), mPaint)
                        } else {
                            //绘制主内容
                            canvas.drawText(currentString, currentWidth, getBaseLine(mPaint, tempHeight), mPaint)
                            currentWidth += currentStringStaticLayoutWidth
                        }
                    }
                } else {  //普通行
                    height += currentLineStaticLayout.height
                    if (isAligin) {
                        drawAlignText(canvas, currentLineText, getBaseLine(mPaint, height), mPaint)
                    } else {
                        canvas.drawText(currentLineText, 0f, getBaseLine(mPaint, height), mPaint)
                    }
                    height += lineSpacingExtra
                }
            }
        }
    }

    //绘制文本，两端分散对齐
    private fun drawAlignText(
        canvas: Canvas,
        lindText: String,
        lineY: Float,
        paint: TextPaint
    ) {
        var lineText = lindText
        var x = 0f
        if (lineText.length > 3 && lineText[0] == ' ' && lineText[1] == ' ') {
            val blanks = "  "
            canvas.drawText(blanks, x, lineY, paint)
            val bw = StaticLayout.getDesiredWidth(blanks, paint)
            x += bw
            lineText = lineText.substring(3)
        }
        val gapCount = lineText.length - 1
        var i = 0
        if (lineText.length > 2 && lineText[0].toInt() == 12288 && lineText[1].toInt() == 12288) {
            val substring = lineText.substring(0, 2)
            val cw = StaticLayout.getDesiredWidth(substring, paint)
            canvas.drawText(substring, x, lineY, paint)
            x += cw
            i += 2
        }
        val textWidth = paint.measureText(lineText)
        val d = (width - textWidth) / gapCount
        while (i < lineText.length) {
            val c = lineText[i].toString()
            val cw = StaticLayout.getDesiredWidth(c, paint)
            canvas.drawText(c, x, lineY, paint)
            x += cw + d
            i++
        }
    }

    //获取文本绘制的基准线
    private fun getBaseLine(paint: TextPaint, descent: Float): Float {
        val fm = paint.fontMetrics
        return descent - fm.descent
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
        if (TextUtils.isEmpty(text)) {
            return 0
        }
        if (width <= 0) {
            return 0
        }
        val staticLayout = getStaticLayout(text, mPaint, width)
        val lineCount = staticLayout.lineCount
        var height = 0f
        if (lineCount <= maxLines) {
            height = staticLayout.height + lineCount * lineSpacingExtra - lineSpacingExtra
        } else {
            for (i in 0 until maxLines) {
                //获取当前行内容
                val currentLineText = text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                val currentLineStaticLayout = getStaticLayout(currentLineText, mPaint, width)
                if (i == maxLines - 1) {  //最后一行
                    height += currentLineStaticLayout.height //+ lineSpacingExtra
                } else {  //普通行
                    height += currentLineStaticLayout.height + lineSpacingExtra
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

}