package com.android.util

import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import android.widget.TextView
import kotlin.apply
import kotlin.ranges.until
import kotlin.text.isEmpty
import kotlin.text.isNullOrBlank
import kotlin.text.substring

/**
 * Created by xuzhb on 2024/8/30
 * Desc:文本测量工具
 */
object MeasureUtil {

    //获取行数，以sp为单位
    fun getLineTextsInSP(text: String?, textSizeSP: Float, outerwidth: Int) = getLineTexts(text, SizeUtil.sp2px(textSizeSP), outerwidth)

    //获取行数，以dp为单位
    fun getLineTextsInDP(text: String?, textSizeDP: Float, outerwidth: Int) = getLineTexts(text, SizeUtil.dp2px(textSizeDP), outerwidth)

    //获取行数
    fun getLineTexts(text: String?, tv: TextView, outerwidth: Int) = getLineTexts(text, tv.textSize, outerwidth)

    //获取每一行的文本内容
    fun getLineTexts(text: String?, textSize: Float, outerwidth: Int): MutableList<String> {
        if (text.isNullOrBlank()) {
            return mutableListOf()
        }
        val paint = TextPaint().apply {
            isAntiAlias = true
            setTextSize(textSize)
        }
        val staticLayout = getStaticLayout(text, paint, outerwidth)
        val lineCount = staticLayout.lineCount
        val list: MutableList<String> = mutableListOf()
        if (lineCount > 1) {
            for (i in 0 until lineCount) {
                val content = text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i))
                LogUtil.i("getLineTexts", "index：$i，content：$content")
                list.add(content)
            }
        }
        return list
    }

    //获取行数，以sp为单位
    fun getCountOfLineTextsInSP(text: String?, textSizeSP: Float, outerwidth: Int) = getCountOfLineTexts(text, SizeUtil.sp2px(textSizeSP), outerwidth)

    //获取行数，以dp为单位
    fun getCountOfLineTextsInDP(text: String?, textSizeDP: Float, outerwidth: Int) = getCountOfLineTexts(text, SizeUtil.dp2px(textSizeDP), outerwidth)

    //获取每一行的字个数
    fun getCountOfLineTexts(text: String?, tv: TextView, outerwidth: Int) = getCountOfLineTexts(text, tv.textSize, outerwidth)

    //获取每一行的字个数
    fun getCountOfLineTexts(text: String?, textSize: Float, outerwidth: Int): MutableList<IntArray> {
        if (text.isNullOrBlank()) {
            return mutableListOf()
        }
        val paint = TextPaint().apply {
            isAntiAlias = true
            setTextSize(textSize)
        }
        val staticLayout = getStaticLayout(text, paint, outerwidth)
        val lineCount = staticLayout.lineCount
        val list: MutableList<IntArray> = mutableListOf()
        if (lineCount > 1) {
            for (i in 0 until lineCount) {
                val start = staticLayout.getLineStart(i)  //每一行文本的起始坐标
                val end = staticLayout.getLineEnd(i) - 1  //每一行文本的结束坐标
                val count = end - start + 1  //每一行文本的字体个数
                LogUtil.i("getCountOfLineTexts", "index：$i，count：$count，start：$start，end：$end")
                list.add(intArrayOf(count, start, end))
            }
        }
        return list
    }

    //获取行数，以sp为单位
    fun getLineCountInSP(text: String?, textSizeSP: Float, outerwidth: Int) = getLineCount(text, SizeUtil.sp2px(textSizeSP), outerwidth)

    //获取行数，以dp为单位
    fun getLineCountInDP(text: String?, textSizeDP: Float, outerwidth: Int) = getLineCount(text, SizeUtil.dp2px(textSizeDP), outerwidth)

    //获取行数
    fun getLineCount(text: String?, tv: TextView, outerwidth: Int) = getLineCount(text, tv.textSize, outerwidth)

    //获取行数
    fun getLineCount(text: String?, textSize: Float, outerwidth: Int): Int {
        if (text.isNullOrBlank()) {
            return 0
        }
        val paint = TextPaint().apply {
            isAntiAlias = true
            setTextSize(textSize)
        }
        val lineCount = getStaticLayout(text, paint, outerwidth).lineCount
        LogUtil.i("getLineCount", "text：$text，textSize：$textSize，lineCount：$lineCount")
        return lineCount
    }

    fun getStaticLayout(text: String, paint: TextPaint, outerwidth: Int, spacingmult: Float = 1f, spacingadd: Float = 0f) = StaticLayout(
        text, 0, text.length, paint, outerwidth,
        Layout.Alignment.ALIGN_NORMAL, spacingmult, spacingadd, false
    )

    //测量文本宽度，以sp为单位
    fun measureTextWidthInSP(text: String, textSizeSP: Float) = measureTextWidth(text, SizeUtil.sp2px(textSizeSP))

    //测量文本宽度，以dp为单位
    fun measureTextWidthInDP(text: String, textSizeDP: Float) = measureTextWidth(text, SizeUtil.dp2px(textSizeDP))

    //测量文本宽度
    fun measureTextWidth(text: String, tv: TextView) = measureTextWidth(text, tv.textSize)

    //测量文本宽度
    fun measureTextWidth(text: String, textSize: Float): Float {
        val paint = Paint().apply {
            isAntiAlias = true
            setTextSize(textSize)
        }
        val width = paint.measureText(text)
        LogUtil.i("measureTextWidth", "text：$text，textSize：$textSize，width：$width")
        return width
    }

    //测量单行文本高度
    fun measureTextHeight(text: String, textSize: Float): Float {
        val paint = Paint().apply {
            isAntiAlias = true
            setTextSize(textSize)
        }
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val textHeight = bounds.height()  //文本的总高度，包括上下的空白区域
        val textHeightSingleLine = paint.descent() - paint.ascent()  //单行文本的高度，不包括上下的空白区域
        LogUtil.i("measureTextHeight", "total：$textHeight，single：$textHeightSingleLine")
        return textHeightSingleLine
    }

    //测量多行文本高度，注：不同手机结果可能不一样
    fun measureMultiLineTextHeight(text: String, textSize: Float, outerwidth: Int, lineSpacing: Float, maxLineCount: Int = -1): Float {
        var lineCount = getLineCount(text, textSize, outerwidth)
        if (maxLineCount in 1 until lineCount) {  //指定了最多行数
            lineCount = maxLineCount
        }
        val singleLineHeight = measureTextHeight("你好", textSize)
        if (text.isEmpty()) {
            return singleLineHeight
        }
        val textHeight = lineCount * (singleLineHeight + lineSpacing) - lineSpacing  //内容文本总高度
        LogUtil.i("measureMultiLineTextHeight", "lineCount：$lineCount，singleLineHeight：$singleLineHeight，textHeight：$textHeight")
        return textHeight
    }

    //测量布局高度，布局高度自适应，及wrap_content
    fun measureWrapContentViewHeight(view: View, widthSize: Int): Int {
        val width = View.MeasureSpec.makeMeasureSpec(widthSize, View.MeasureSpec.EXACTLY)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(width, height)
        return view.measuredHeight
    }

}