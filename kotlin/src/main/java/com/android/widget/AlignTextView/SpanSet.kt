package com.android.widget.AlignTextView

import android.text.Spanned
import java.util.*

/**
 * Created by xuzhb on 2021/4/14
 */
class SpanSet<E>(private val mClassType: Class<E>) {

    var numberOfSpans: Int = 0
    var spans: Array<E>? = null
    var spanStarts: IntArray? = null
    var spanEnds: IntArray? = null
    var spanFlags: IntArray? = null

    fun init(spanned: Spanned, start: Int, limit: Int) {
        val allSpans: Array<E> = spanned.getSpans(start, limit, mClassType)
        val length = allSpans.size
        if (length > 0 && (spans == null || spans!!.size < length)) {
            spans = java.lang.reflect.Array.newInstance(mClassType, length) as? Array<E>
            spanStarts = IntArray(length)
            spanEnds = IntArray(length)
            spanFlags = IntArray(length)
        }
        val prevNumberOfSpans = numberOfSpans
        numberOfSpans = 0
        for (i in 0 until length) {
            val span = allSpans[i]
            val spanStart = spanned.getSpanStart(span)
            val spanEnd = spanned.getSpanEnd(span)
            if (spanStart == spanEnd) {
                continue
            }
            val spanFlag = spanned.getSpanFlags(span)
            spans?.let { it[numberOfSpans] = span }
            spanStarts?.let { it[numberOfSpans] = spanStart }
            spanEnds?.let { it[numberOfSpans] = spanEnd }
            spanFlags?.let { it[numberOfSpans] = spanFlag }
            numberOfSpans++
        }
        if (numberOfSpans < prevNumberOfSpans) {
            Arrays.fill(spans, numberOfSpans, prevNumberOfSpans, null)
        }
    }

    fun hasSpansIntersecting(start: Int, end: Int): Boolean {
        for (i in 0 until numberOfSpans) {
            if ((spanStarts != null && spanStarts!![i] >= end) ||
                (spanEnds != null && spanEnds!![i] <= start)
            ) {
                continue
            }
            return true
        }
        return false
    }

    fun getNextTransition(start: Int, limit: Int): Int {
        var limit = limit
        for (i in 0 until numberOfSpans) {
            val spanStart: Int = spanStarts?.get(i) ?: 0
            val spanEnd: Int = spanEnds?.get(i) ?: 0
            if (spanStart in (start + 1) until limit) {
                limit = spanStart
            }
            if (spanEnd in (start + 1) until limit) {
                limit = spanEnd
            }
        }
        return limit
    }

    fun recycle() {
        spans?.let {
            Arrays.fill(it, 0, numberOfSpans, null)
        }
    }

}