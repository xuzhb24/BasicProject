package com.android.widget.AlignTextView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AlignmentSpan
import android.text.style.ParagraphStyle
import android.text.style.TabStopSpan
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import com.android.basicproject.R
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Created by xuzhb on 2021/4/14
 * Desc:两端对齐的TextView
 */
class AlignTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAB_INCREMENT = 20
        private val NO_PARA_SPANS = ArrayUtil.emptyArray(ParagraphStyle::class.java)
    }

    var isAlign: Boolean = false

    private var mTempRect: Rect = Rect()

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView)
            isAlign = ta.getBoolean(R.styleable.AlignTextView_align, true)
            ta.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val layout = layout
            val compoundPaddingLeft = compoundPaddingLeft
            val extendedPaddingTop = extendedPaddingTop
            val voffsetText = 0
            it.save()
            it.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop + voffsetText.toFloat())
            val lineRange = getLineRangeForDraw(it, layout)
            val firstLine = unpackRangeStartFromLong(lineRange)
            val lastLine = unpackRangeEndFromLong(lineRange)
            if (lastLine < 0) {
                return
            }
            drawText(it, firstLine, lastLine, layout)
            it.restore()
        }
    }

    private fun drawText(canvas: Canvas, firstLine: Int, lastLine: Int, layout: Layout) {
        var previousLineBottom = layout.getLineTop(firstLine)
        var previousLineEnd = layout.getLineStart(firstLine)
        var spans = NO_PARA_SPANS
        var spanEnd = 0
        val paint = paint
        paint.color = currentTextColor
        val buf = layout.text
        var paraAlign = layout.alignment
        var tabStops: TabStops? = null
        var tabStopsIsInitialized = false
        val tl = TextLine.obtain()
        // Draw the lines, one at a time.
        // The baseline is the top of the following line minus the current line's descent.
        for (i in firstLine..lastLine) {
            val start = previousLineEnd
            previousLineEnd = layout.getLineStart(i + 1)
            val end = layout.getLineVisibleEnd(i)
            val ltop = previousLineBottom
            val lbottom = layout.getLineTop(i + 1)
            previousLineBottom = lbottom
            val lbaseline = lbottom - layout.getLineDescent(i)
            val dir = layout.getParagraphDirection(i)
            val left = 0
            val right = layout.width
            if (text is Spanned) {
                val sp = buf as Spanned
                val textLength = buf.length
                val isFirstParaLine = start == 0 || buf[start - 1] == '\n'
                // New batch of paragraph styles, collect into spans array.
                // Compute the alignment, last alignment style wins.
                // Reset tabStops, we'll rebuild if we encounter a line with
                // tabs.
                // We expect paragraph spans to be relatively infrequent, use
                // spanEnd so that we can check less frequently.  Since
                // paragraph styles ought to apply to entire paragraphs, we can
                // just collect the ones present at the start of the paragraph.
                // If spanEnd is before the end of the paragraph, that's not
                // our problem.
                if (start >= spanEnd && (i == firstLine || isFirstParaLine)) {
                    spanEnd = sp.nextSpanTransition(start, textLength, ParagraphStyle::class.java)
                    spans = getParagraphSpans<ParagraphStyle>(sp, start, spanEnd, ParagraphStyle::class.java)
                    paraAlign = layout.alignment
                    if (spans != null) {
                        for (n in spans.indices.reversed()) {
                            if (spans[n] is AlignmentSpan) {
                                paraAlign = (spans[n] as? AlignmentSpan)?.alignment
                                break
                            }
                        }
                    }
                    tabStopsIsInitialized = false
                }
            }
            val hasTabOrEmoji = layout.getLineContainsTab(i)
            // Can't tell if we have tabs for sure, currently
            if (hasTabOrEmoji && !tabStopsIsInitialized) {
                if (tabStops == null) {
                    tabStops = TabStops(TAB_INCREMENT, spans)
                } else {
                    tabStops.reset(TAB_INCREMENT, spans)
                }
                tabStopsIsInitialized = true
            }
            // Determine whether the line aligns to normal, opposite, or center.
            val align = paraAlign
            var x = 0
            val indentWidth: Int
            if (align == Layout.Alignment.ALIGN_NORMAL) {
                if (dir == Layout.DIR_LEFT_TO_RIGHT) {
                    indentWidth = 0
                    x = left
                } else {
                    indentWidth = 0
                    x = right
                }
            } else {
                indentWidth = 0
                var max = getLineExtent(i, tabStops, false, layout).toInt()
                if (align == Layout.Alignment.ALIGN_OPPOSITE) {
                    x = if (dir == Layout.DIR_LEFT_TO_RIGHT) {
                        right - max
                    } else {
                        left - max
                    }
                } else { // Alignment.ALIGN_CENTER
                    max = max and 1.inv()
                    x = right + left - max shr 1
                }
            }
            val directions = layout.getLineDirections(i)
            if (text !is Spanned && !hasTabOrEmoji && !isAlign) {
                //directions == DIRS_ALL_LEFT_TO_RIGHT && to be true
                // XXX: assumes there's nothing additional to be done
                canvas.drawText(buf, start, end, x.toFloat(), lbaseline.toFloat(), paint)
            } else {
                val needJustify = isJustificationRequired(layout, buf, i) && isAlign
                if (tl != null) {
                    tl[paint, buf, start, end, dir, hasTabOrEmoji] = needJustify
                    if (needJustify) {
                        tl.align(right - left - indentWidth.toFloat())
                    }
                    tl.draw(canvas, x.toFloat(), ltop, lbaseline, lbottom)
                }
            }
        }
        TextLine.recycle(tl)
    }

    private fun isJustificationRequired(layout: Layout, mText: CharSequence, lineNum: Int): Boolean {
        val lineEnd = getLineEnd(layout, lineNum)
        return lineEnd < mText.length && mText[lineEnd - 1] != '\n'
    }

    /**
     * Return the text offset after the last character on the specified line.
     */
    private fun getLineEnd(layout: Layout, line: Int): Int {
        return layout.getLineStart(line + 1)
    }

    private fun <T> getParagraphSpans(text: Spanned, start: Int, end: Int, type: Class<T>): Array<T>? {
        if (start == end && start > 0) {
            return ArrayUtil.emptyArray(type)
        }
        return if (text is SpannableStringBuilder) text.getSpans(start, end, type)
        else text.getSpans(start, end, type)
    }

    /**
     * @param canvas
     * @return The range of lines that need to be drawn, possibly empty.
     * @hide
     */
    private fun getLineRangeForDraw(canvas: Canvas, layout: Layout): Long {
        var dtop: Int
        var dbottom: Int
        synchronized(mTempRect) {
            if (!canvas.getClipBounds(mTempRect)) {
                // Negative range end used as a special flag
                return packRangeInLong(0, -1)
            }
            dtop = mTempRect.top
            dbottom = mTempRect.bottom
        }
        val top = max(dtop, 0)
        val bottom = min(layout.getLineTop(lineCount), dbottom)
        return if (top >= bottom) packRangeInLong(0, -1)
        else packRangeInLong(layout.getLineForVertical(top), layout.getLineForVertical(bottom))
    }

    /**
     * Returns the signed horizontal extent of the specified line, excluding
     * leading margin.  If full is false, excludes trailing whitespace.
     *
     * @param line     the index of the line
     * @param tabStops the tab stops, can be null if we know they're not used.
     * @param full     whether to include trailing whitespace
     * @return the extent of the text on this line
     */
    private fun getLineExtent(line: Int, tabStops: TabStops?, full: Boolean, layout: Layout): Float {
        val start = layout.getLineStart(line)
        val end = if (full) layout.getLineEnd(line) else layout.getLineVisibleEnd(line)
        val hasTabsOrEmoji = layout.getLineContainsTab(line)
        val dir = layout.getParagraphDirection(line)
        var width = 0f
        TextLine.obtain()?.let {
            it[paint, text, start, end, dir, hasTabsOrEmoji] = false
            width = it.metrics(null)
            TextLine.recycle(it)
        }
        return width
    }

    private fun packRangeInLong(start: Int, end: Int): Long {
        return start.toLong() shl 32 or end.toLong()
    }

    private fun unpackRangeStartFromLong(range: Long): Int {
        return (range ushr 32).toInt()
    }

    private fun unpackRangeEndFromLong(range: Long): Int {
        return (range and 0x00000000FFFFFFFFL).toInt()
    }

    class TabStops internal constructor(increment: Int, spans: Array<ParagraphStyle>?) {

        private var mStops: IntArray? = null

        init {
            reset(increment, spans)
        }

        fun reset(increment: Int, spans: Array<ParagraphStyle>?) {
            var ns = 0
            if (spans != null) {
                var stops = mStops
                for (o in spans) {
                    if (o is TabStopSpan) {
                        if (stops == null) {
                            stops = IntArray(10)
                        } else if (ns == stops.size) {
                            val nstops = IntArray(ns * 2)
                            for (i in 0 until ns) {
                                nstops[i] = stops[i]
                            }
                            stops = nstops
                        }
                        stops[ns++] = o.tabStop
                    }
                }
                if (ns > 1) {
                    Arrays.sort(stops, 0, ns)
                }
                if (stops != mStops) {
                    mStops = stops
                }
            }
        }

    }

}