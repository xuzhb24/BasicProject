package com.android.widget.AlignTextView

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.text.style.MetricAffectingSpan
import android.text.style.ReplacementSpan
import android.util.Log
import com.android.util.LogUtil
import kotlin.math.max
import kotlin.math.min

/**
 * Created by xuzhb on 2021/4/14
 */
class TextLine {

    companion object {
        private const val TAG = "TextLine"
        private const val DEBUG = false
        private val mCache = arrayOfNulls<TextLine>(3)

        /**
         * Returns a new TextLine from the shared pool.
         *
         * @return an uninitialized TextLine
         */
        @JvmStatic
        fun obtain(): TextLine? {
            var tl: TextLine?
            synchronized(mCache) {
                var i = mCache.size
                while (--i >= 0) {
                    if (mCache[i] != null) {
                        tl = mCache[i]
                        mCache[i] = null
                        return tl
                    }
                }
            }
            tl = TextLine()
            if (DEBUG) {
                LogUtil.i(TAG, "new: $tl")
            }
            return tl
        }

        /**
         * Puts a TextLine back into the shared pool. Do not use this TextLine once it has been returned.
         *
         * @param tl the textLine
         * @return null, as a convenience from clearing references to the provided TextLine
         */
        @JvmStatic
        fun recycle(tl: TextLine?): TextLine? {
            if (tl == null) {
                return null
            }
            tl.mText = null
            tl.mPaint = null
            tl.mMetricAffectingSpanSpanSet.recycle()
            tl.mCharacterStyleSpanSet.recycle()
            tl.mReplacementSpanSpanSet.recycle()
            synchronized(mCache) {
                for (i in mCache.indices) {
                    if (mCache[i] == null) {
                        mCache[i] = tl
                        break
                    }
                }
            }
            return null
        }
    }

    private var mStart: Int = 0
    private var mLen: Int = 0
    private var mDir: Int = 0
    private var mWidth: Float = 0f
    private var mLastUseExtraWidth: Float = 0f
    private var mCharsValid: Boolean = false
    private var mNeedJustify: Boolean = false
    private var mChars: CharArray? = null
    private var mText: CharSequence? = null
    private var mSpanned: Spanned? = null
    private var mPaint: TextPaint? = null
    private val mWorkPaint = TextPaint()
    private val mMetricAffectingSpanSpanSet = SpanSet(MetricAffectingSpan::class.java)
    private val mCharacterStyleSpanSet = SpanSet(CharacterStyle::class.java)
    private val mReplacementSpanSpanSet = SpanSet(ReplacementSpan::class.java)

    /**
     * Initializes a TextLine and prepares it for use.
     *
     * @param paint   the base paint for the line
     * @param text    the text, can be Styled
     * @param start   the start of the line relative to the text
     * @param limit   the limit of the line relative to the text
     * @param dir     the paragraph direction of this line
     * @param hasTabs true if the line might contain tabs or emoji
     */
    operator fun set(paint: TextPaint, text: CharSequence, start: Int, limit: Int, dir: Int, hasTabs: Boolean, needJustify: Boolean) {
        mPaint = paint
        mText = text
        mStart = start
        mLen = limit - start
        mDir = dir
        mSpanned = null
        mNeedJustify = needJustify
        mLastUseExtraWidth = 0f
        var hasReplacement = false
        if (text is Spanned) {
            mSpanned = text
            mSpanned?.let { mReplacementSpanSpanSet.init(it, start, limit) }
            hasReplacement = mReplacementSpanSpanSet.numberOfSpans > 0
        }
        mCharsValid = hasReplacement || hasTabs
        if (mCharsValid) {
            if (mChars == null || mChars!!.size < mLen) {
                mChars = CharArray(mLen)
            }
            TextUtils.getChars(text, start, limit, mChars, 0)
            if (hasReplacement) {
                // Handle these all at once so we don't have to do it as we go.
                // Replace the first character of each replacement run with the
                // object-replacement character and the remainder with zero width
                // non-break space aka BOM.  Cursor movement code skips these
                // zero-width characters.
                val chars = mChars!!
                var i = start
                var inext: Int
                while (i < limit) {
                    inext = mReplacementSpanSpanSet.getNextTransition(i, limit)
                    if (mReplacementSpanSpanSet.hasSpansIntersecting(i, inext)) {
                        // transition into a span
                        chars[i - start] = '\ufffc'
                        var j = i - start + 1
                        val e = inext - start
                        while (j < e) {
                            chars[j] = '\ufeff' // used as ZWNBS, marks positions to skip
                            ++j
                        }
                    }
                    i = inext
                }
            }
        }
    }

    fun align(width: Float) {
        mWidth = width
    }

    /**
     * Renders the TextLine.
     *
     * @param c      the canvas to render on
     * @param x      the leading margin position
     * @param top    the top of the line
     * @param y      the baseline
     * @param bottom the bottom of the line
     */
    fun draw(c: Canvas, x: Float, top: Int, y: Int, bottom: Int) {
        drawRun(c, 0, mLen, false, x, top, y, bottom, false)
    }

    /**
     * Returns metrics information for the entire line.
     *
     * @param fmi receives font metrics information, can be null
     * @return the signed width of the line
     */
    fun metrics(fmi: Paint.FontMetricsInt?): Float {
        return measure(mLen, false, fmi)
    }

    /**
     * Returns information about a position on the line.
     *
     * @param offset   the line-relative character offset, between 0 and the
     *                 line length, inclusive
     * @param trailing true to measure the trailing edge of the character
     *                 before offset, false to measure the leading edge of the character
     *                 at offset.
     * @param fmi      receives metrics information about the requested
     *                 character, can be null.
     * @return the signed offset from the leading margin to the requested character edge.
     */
    private fun measure(offset: Int, trailing: Boolean, fmi: Paint.FontMetricsInt?): Float {
        val target = if (trailing) offset - 1 else offset
        return if (target < 0) 0f else measureRun(0, offset, mLen, false, fmi)
    }

    /**
     * Draws a unidirectional (but possibly multi-styled) run of text.
     *
     * @param c         the canvas to draw on
     * @param start     the line-relative start
     * @param limit     the line-relative limit
     * @param runIsRtl  true if the run is right-to-left
     * @param x         the position of the run that is closest to the leading margin
     * @param top       the top of the line
     * @param y         the baseline
     * @param bottom    the bottom of the line
     * @param needWidth true if the width value is required.
     * @return the signed width of the run, based on the paragraph direction.
     * Only valid if needWidth is true.
     */
    private fun drawRun(
        c: Canvas, start: Int, limit: Int, runIsRtl: Boolean,
        x: Float, top: Int, y: Int, bottom: Int, needWidth: Boolean
    ): Float {
        if (mDir == Layout.DIR_LEFT_TO_RIGHT == runIsRtl) {
            val w = -measureRun(start, limit, limit, runIsRtl, null)
            handleRun(start, limit, limit, runIsRtl, c, x + w, top, y, bottom, null, false)
            return w
        }
        return handleRun(start, limit, limit, runIsRtl, c, x, top, y, bottom, null, needWidth)
    }

    /**
     * Measures a unidirectional (but possibly multi-styled) run of text.
     *
     * @param start    the line-relative start of the run
     * @param offset   the offset to measure to, between start and limit inclusive
     * @param limit    the line-relative limit of the run
     * @param runIsRtl true if the run is right-to-left
     * @param fmi      receives metrics information about the requested
     *                 run, can be null.
     * @return the signed width from the start of the run to the leading edge
     * of the character at offset, based on the run (not paragraph) direction
     */
    private fun measureRun(start: Int, offset: Int, limit: Int, runIsRtl: Boolean, fmi: Paint.FontMetricsInt?): Float {
        return handleRun(start, offset, limit, runIsRtl, null, 0f, 0, 0, 0, fmi, true)
    }

    /**
     * Utility function for handling a unidirectional run.  The run must not contain tabs or emoji but can contain styles.
     *
     * @param start        the line-relative start of the run
     * @param measureLimit the offset to measure to, between start and limit inclusive
     * @param limit        the limit of the run
     * @param runIsRtl     true if the run is right-to-left
     * @param c            the canvas, can be null
     * @param x            the end of the run closest to the leading margin
     * @param top          the top of the line
     * @param y            the baseline
     * @param bottom       the bottom of the line
     * @param fmi          receives metrics information, can be null
     * @param needWidth    true if the width is required
     * @return the signed width of the run based on the run direction; only
     * valid if needWidth is true
     */
    private fun handleRun(
        start: Int, measureLimit: Int, limit: Int, runIsRtl: Boolean, c: Canvas?, x: Float,
        top: Int, y: Int, bottom: Int, fmi: Paint.FontMetricsInt?, needWidth: Boolean
    ): Float {
        // Case of an empty line, make sure we update fmi according to mPaint
        var x = x
        if (start == measureLimit) {
            val wp = mWorkPaint
            wp.set(mPaint)
            fmi?.let { expandMetricsFromPaint(it, wp) }
            return 0f
        }
        if (mSpanned == null) {
            val wp = mWorkPaint
            wp.set(mPaint)
            return handleText(
                wp, start, measureLimit, start, limit, runIsRtl, c, x, top,
                y, bottom, fmi, needWidth || measureLimit < measureLimit
            )
        }
        mSpanned?.let {
            mMetricAffectingSpanSpanSet.init(it, mStart + start, mStart + limit)
            mCharacterStyleSpanSet.init(it, mStart + start, mStart + limit)
        }
        // Shaping needs to take into account context up to metric boundaries,
        // but rendering needs to take into account character style boundaries.
        // So we iterate through metric runs to get metric bounds,
        // then within each metric run iterate through character style runs
        // for the run bounds.
        val originalX = x
        var i = start
        var inext: Int
        while (i < measureLimit) {
            val wp = mWorkPaint
            wp.set(mPaint)
            inext = mMetricAffectingSpanSpanSet.getNextTransition(mStart + i, mStart + limit) - mStart
            val mlimit = Math.min(inext, measureLimit)
            var replacement: ReplacementSpan? = null
            for (j in 0 until mMetricAffectingSpanSpanSet.numberOfSpans) {
                // Both intervals [spanStarts..spanEnds] and [mStart + i..mStart + mlimit] are NOT
                // empty by construction. This special case in getSpans() explains the >= & <= tests
                if ((mMetricAffectingSpanSpanSet.spanStarts != null && mMetricAffectingSpanSpanSet.spanStarts!![j] >= mStart + mlimit) ||
                    (mMetricAffectingSpanSpanSet.spanEnds != null && mMetricAffectingSpanSpanSet.spanEnds!![j] <= mStart + i)
                ) continue
                val span = mMetricAffectingSpanSpanSet.spans!![j]
                if (span is ReplacementSpan) {
                    replacement = span
                } else {
                    // We might have a replacement that uses the draw
                    // state, otherwise measure state would suffice.
                    span.updateDrawState(wp)
                }
            }
            if (replacement != null) {
                x += handleReplacement(
                    replacement, wp, i, mlimit, runIsRtl, c, x, top, y,
                    bottom, fmi, needWidth || mlimit < measureLimit
                )
                i = inext
                continue
            }
            if (c == null) {
                x += handleText(
                    wp, i, mlimit, i, inext, runIsRtl, c, x, top,
                    y, bottom, fmi, needWidth || mlimit < measureLimit
                )
            } else {
                var j = i
                var jnext: Int
                while (j < mlimit) {
                    jnext = mCharacterStyleSpanSet.getNextTransition(mStart + j, mStart + mlimit) - mStart
                    wp.set(mPaint)
                    for (k in 0 until mCharacterStyleSpanSet.numberOfSpans) {
                        // Intentionally using >= and <= as explained above
                        if ((mCharacterStyleSpanSet.spanStarts != null && mCharacterStyleSpanSet.spanStarts!![k] >= mStart + jnext) ||
                            (mCharacterStyleSpanSet.spanEnds != null && mCharacterStyleSpanSet.spanEnds!![k] <= mStart + j)
                        ) continue
                        if (mCharacterStyleSpanSet.spans != null) {
                            val span = mCharacterStyleSpanSet.spans!![k]
                            span.updateDrawState(wp)
                        }
                    }
                    x += handleText(
                        wp, j, jnext, i, inext, runIsRtl, c, x,
                        top, y, bottom, fmi, needWidth || jnext < measureLimit
                    )
                    j = jnext
                }
            }
            i = inext
        }
        return x - originalX
    }

    /**
     * Utility function for measuring and rendering a replacement.
     *
     * @param replacement the replacement
     * @param wp          the work paint
     * @param start       the start of the run
     * @param limit       the limit of the run
     * @param runIsRtl    true if the run is right-to-left
     * @param c           the canvas, can be null if not rendering
     * @param x           the edge of the replacement closest to the leading margin
     * @param top         the top of the line
     * @param y           the baseline
     * @param bottom      the bottom of the line
     * @param fmi         receives metrics information, can be null
     * @param needWidth   true if the width of the replacement is needed
     * @return the signed width of the run based on the run direction; only valid if needWidth is true
     */
    private fun handleReplacement(
        replacement: ReplacementSpan, wp: TextPaint, start: Int, limit: Int, runIsRtl: Boolean,
        c: Canvas?, x: Float, top: Int, y: Int, bottom: Int, fmi: Paint.FontMetricsInt?, needWidth: Boolean
    ): Float {
        var x = x
        var ret = 0f
        val textStart = mStart + start
        val textLimit = mStart + limit
        if (needWidth || c != null && runIsRtl) {
            var previousTop = 0
            var previousAscent = 0
            var previousDescent = 0
            var previousBottom = 0
            var previousLeading = 0
            val needUpdateMetrics = fmi != null
            if (needUpdateMetrics && fmi != null) {
                previousTop = fmi.top
                previousAscent = fmi.ascent
                previousDescent = fmi.descent
                previousBottom = fmi.bottom
                previousLeading = fmi.leading
            }
            ret = replacement.getSize(wp, mText, textStart, textLimit, fmi).toFloat()
            if (needUpdateMetrics && fmi != null) {
                updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading)
            }
        }
        if (c != null) {
            if (runIsRtl) {
                x -= ret
            }
            replacement.draw(c, mText, textStart, textLimit, x, top, y, bottom, wp)
        }
        return if (runIsRtl) -ret else ret
    }

    /**
     * Utility function for measuring and rendering text.  The text must not include a tab or emoji.
     *
     * @param wp        the working paint
     * @param start     the start of the text
     * @param end       the end of the text
     * @param runIsRtl  true if the run is right-to-left
     * @param c         the canvas, can be null if rendering is not needed
     * @param x         the edge of the run closest to the leading margin
     * @param top       the top of the line
     * @param y         the baseline
     * @param bottom    the bottom of the line
     * @param fmi       receives metrics information, can be null
     * @param needWidth true if the width of the run is needed
     * @return the signed width of the run based on the run direction; only valid if needWidth is true
     */
    private fun handleText(
        wp: TextPaint, start: Int, end: Int, contextStart: Int, contextEnd: Int, runIsRtl: Boolean,
        c: Canvas?, x: Float, top: Int, y: Int, bottom: Int, fmi: Paint.FontMetricsInt?, needWidth: Boolean
    ): Float {
        // Get metrics first (even for empty strings or "0" width runs)
        var x = x
        fmi?.let { expandMetricsFromPaint(it, wp) }
        val runLen = end - start
        // No need to do anything if the run width is "0"
        if (runLen == 0) {
            return 0f
        }
        var ret = 0f
        val contextLen = contextEnd - contextStart
        if (needWidth || c != null && (wp.bgColor != 0 || runIsRtl)) {
            val flags = if (runIsRtl) 1 else 0
            ret = if (mCharsValid) {
                //这里是当有多个spannable的时候 需要计算出每个spannable占的宽度 计算出下一个spannable的位置
                wp.measureText(mChars, start, runLen)
            } else {
                val delta = mStart
                wp.measureText(mText, delta + start, delta + end)
            }
        }
        if (c != null) {
            if (runIsRtl) {
                x -= ret
            }
            if (wp.bgColor != 0) {
                val previousColor = wp.color
                val previousStyle = wp.style
                wp.color = wp.bgColor
                wp.style = Paint.Style.FILL
                c.drawRect(x, top.toFloat(), x + ret, bottom.toFloat(), wp)
                wp.style = previousStyle
                wp.color = previousColor
            }
            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl, x, y + wp.baselineShift)
        }
        return if (runIsRtl) -ret else ret
    }

    /**
     * Render a text run with the set-up paint.
     *
     * @param c            the canvas
     * @param wp           the paint used to render the text
     * @param start        the start of the run
     * @param end          the end of the run
     * @param contextStart the start of context for the run
     * @param contextEnd   the end of the context for the run
     * @param runIsRtl     true if the run is right-to-left
     * @param x            the x position of the left edge of the run
     * @param y            the baseline of the run
     */
    private fun drawTextRun(
        c: Canvas, wp: TextPaint, start: Int, end: Int, contextStart: Int,
        contextEnd: Int, runIsRtl: Boolean, x: Float, y: Int
    ) {
        if (DEBUG) {
            Log.i(TAG, "start:$start end:$end mStart:$mStart")
        }
        LogUtil.i(TAG, "layout drawTextRun mCharsValid:$mCharsValid")
        if (mCharsValid) {
            val count = end - start
            val contextCount = contextEnd - contextStart
            mChars?.let { c.drawText(it, start, count, x, y.toFloat(), wp) }
        } else {
            val delta = mStart
            val count = end - start
            if (mNeedJustify) {
                var useWidth = mWidth
                var charLen = x
                var charTotalLen = 0f
                val chars_ = mText?.subSequence(delta + start, delta + end).toString()
                LogUtil.i(TAG, "layout drawTextRun mNeedJustify:$chars_")
                val widths = FloatArray(chars_.length)
                if (isNormalString(chars_)) {
                    mPaint?.getTextWidths(chars_, widths)
                    for (width in widths) {
                        charTotalLen += width.toInt()
                    }
                } else {
                    for (i in chars_.indices) {
                        val charWidth = mPaint?.measureText(chars_[i].toString()) ?: 0f
                        widths[i] = charWidth
                        charTotalLen += charWidth
                    }
                }
                if (mLen > count) {
                    useWidth = mWidth * count / mLen
                    if (useWidth < charTotalLen) {
                        useWidth = charTotalLen
                    }
                }
                LogUtil.i(TAG, "layout count:$count mLen:$mLen")
                LogUtil.i(TAG, "layout useWidth:$useWidth charTotalLen:$charTotalLen")
                val mAddedWidth = (useWidth - charTotalLen) / count
                LogUtil.i(TAG, "layout mAddedWidth:$mAddedWidth")
                if (start != 0) {
                    LogUtil.i(TAG, "layout drawTextRun start != mStart before:$charLen")
                    charLen += mLastUseExtraWidth
                    LogUtil.i(TAG, "layout drawTextRun start != mStart after:$charLen")
                }
                for (j in 0 until count) {
                    val char_ = chars_.substring(j, j + 1)
                    val drawX = charLen + mAddedWidth * j
                    c.drawText(char_, drawX, y.toFloat(), wp)
                    charLen += widths[j]
                    mLastUseExtraWidth += mAddedWidth
                }
            } else {
                mText?.let { c.drawText(it, delta + start, delta + end, x, y.toFloat(), wp) }
            }
        }
    }

    private fun isNormalString(str: String?): Boolean {
        if (str.isNullOrBlank()) {
            return true
        }
        if (str.contains("——")) {  //Paint.getTextWidths测量中文破折号——宽度时会出错，导致绘制出错
            return false
        }
        return true
    }

    private fun expandMetricsFromPaint(fmi: Paint.FontMetricsInt, wp: TextPaint) {
        val previousTop = fmi.top
        val previousAscent = fmi.ascent
        val previousDescent = fmi.descent
        val previousBottom = fmi.bottom
        val previousLeading = fmi.leading
        wp.getFontMetricsInt(fmi)
        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading)
    }

    private fun updateMetrics(
        fmi: Paint.FontMetricsInt, previousTop: Int, previousAscent: Int,
        previousDescent: Int, previousBottom: Int, previousLeading: Int
    ) {
        fmi.top = min(fmi.top, previousTop)
        fmi.ascent = min(fmi.ascent, previousAscent)
        fmi.descent = max(fmi.descent, previousDescent)
        fmi.bottom = max(fmi.bottom, previousBottom)
        fmi.leading = max(fmi.leading, previousLeading)
    }


}