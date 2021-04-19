package com.android.widget.AlignTextView;

import android.text.Spanned;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by xuzhb on 2021/4/13
 */
public class SpanSet<E> {

    private Class<? extends E> mClassType;
    private int mNumberOfSpans;
    private E[] mSpans;
    private int[] mSpanStarts;
    private int[] mSpanEnds;
    private int[] mSpanFlags;

    public SpanSet(Class<? extends E> classType) {
        this.mClassType = classType;
        mNumberOfSpans = 0;
    }

    public int getNumberOfSpans() {
        return mNumberOfSpans;
    }

    public E[] getSpans() {
        return mSpans;
    }

    public int[] getSpanStarts() {
        return mSpanStarts;
    }

    public int[] getSpanEnds() {
        return mSpanEnds;
    }

    public int[] getSpanFlags() {
        return mSpanFlags;
    }

    @SuppressWarnings("unchecked")
    public void init(Spanned spanned, int start, int limit) {
        final E[] allSpans = spanned.getSpans(start, limit, mClassType);
        final int length = allSpans.length;
        if (length > 0 && (mSpans == null || mSpans.length < length)) {
            mSpans = (E[]) Array.newInstance(mClassType, length);
            mSpanStarts = new int[length];
            mSpanEnds = new int[length];
            mSpanFlags = new int[length];
        }
        int prevNumberOfSpans = mNumberOfSpans;
        mNumberOfSpans = 0;
        for (int i = 0; i < length; i++) {
            final E span = allSpans[i];
            final int spanStart = spanned.getSpanStart(span);
            final int spanEnd = spanned.getSpanEnd(span);
            if (spanStart == spanEnd) {
                continue;
            }
            final int spanFlag = spanned.getSpanFlags(span);
            mSpans[mNumberOfSpans] = span;
            mSpanStarts[mNumberOfSpans] = spanStart;
            mSpanEnds[mNumberOfSpans] = spanEnd;
            mSpanFlags[mNumberOfSpans] = spanFlag;
            mNumberOfSpans++;
        }
        if (mNumberOfSpans < prevNumberOfSpans) {
            Arrays.fill(mSpans, mNumberOfSpans, prevNumberOfSpans, null);
        }
    }

    public boolean hasSpansIntersecting(int start, int end) {
        for (int i = 0; i < mNumberOfSpans; i++) {
            if (mSpanStarts[i] >= end || mSpanEnds[i] <= start) {
                continue;
            }
            return true;
        }
        return false;
    }

    public int getNextTransition(int start, int limit) {
        for (int i = 0; i < mNumberOfSpans; i++) {
            final int spanStart = mSpanStarts[i];
            final int spanEnd = mSpanEnds[i];
            if (spanStart > start && spanStart < limit) limit = spanStart;
            if (spanEnd > start && spanEnd < limit) limit = spanEnd;
        }
        return limit;
    }

    public void recycle() {
        if (mSpans != null) {
            Arrays.fill(mSpans, 0, mNumberOfSpans, null);
        }
    }

}
