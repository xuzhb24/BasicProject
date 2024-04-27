package com.android.widget.AlignTextView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.android.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2021/4/13
 */
public class TextLine {

    private static final String TAG = "TextLine";
    private static final boolean DEBUG = false;
    private static final TextLine[] mCache = new TextLine[3];

    private int mStart;
    private int mLen;
    private int mDir;
    private float mWidth;
    private float mLastUseExtraWidth;
    private boolean mCharsValid;
    private boolean mNeedJustify = false;
    private char[] mChars;
    private CharSequence mText;
    private Spanned mSpanned;
    private TextPaint mPaint;
    private final TextPaint mWorkPaint = new TextPaint();
    private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet<>(MetricAffectingSpan.class);
    private final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet<>(CharacterStyle.class);
    private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet<>(ReplacementSpan.class);

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
    public void set(TextPaint paint, CharSequence text, int start, int limit, int dir, boolean hasTabs, boolean needJustify) {
        mPaint = paint;
        mText = text;
        mStart = start;
        mLen = limit - start;
        mDir = dir;
        mSpanned = null;
        mNeedJustify = needJustify;
        mLastUseExtraWidth = 0;
        boolean hasReplacement = false;
        if (text instanceof Spanned) {
            mSpanned = (Spanned) text;
            mReplacementSpanSpanSet.init(mSpanned, start, limit);
            hasReplacement = mReplacementSpanSpanSet.getNumberOfSpans() > 0;
        }
        mCharsValid = hasReplacement || hasTabs;
        if (mCharsValid) {
            if (mChars == null || mChars.length < mLen) {
                mChars = new char[mLen];
            }
            TextUtils.getChars(text, start, limit, mChars, 0);
            if (hasReplacement) {
                // Handle these all at once so we don't have to do it as we go.
                // Replace the first character of each replacement run with the
                // object-replacement character and the remainder with zero width
                // non-break space aka BOM.  Cursor movement code skips these
                // zero-width characters.
                char[] chars = mChars;
                for (int i = start, inext; i < limit; i = inext) {
                    inext = mReplacementSpanSpanSet.getNextTransition(i, limit);
                    if (mReplacementSpanSpanSet.hasSpansIntersecting(i, inext)) {
                        // transition into a span
                        chars[i - start] = '\ufffc';
                        for (int j = i - start + 1, e = inext - start; j < e; ++j) {
                            chars[j] = '\ufeff'; // used as ZWNBS, marks positions to skip
                        }
                    }
                }
            }
        }
    }

    public void align(float width) {
        mWidth = width;
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
    public void draw(Canvas c, float x, int top, int y, int bottom) {
        drawRun(c, 0, mLen, false, x, top, y, bottom, false);
    }

    /**
     * Returns metrics information for the entire line.
     *
     * @param fmi receives font metrics information, can be null
     * @return the signed width of the line
     */
    public float metrics(Paint.FontMetricsInt fmi) {
        return measure(mLen, false, fmi);
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
    private float measure(int offset, boolean trailing, Paint.FontMetricsInt fmi) {
        int target = trailing ? offset - 1 : offset;
        if (target < 0) {
            return 0;
        }
        return measureRun(0, offset, mLen, false, fmi);
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
    private float drawRun(Canvas c, int start, int limit, boolean runIsRtl, float x, int top, int y, int bottom, boolean needWidth) {
        if ((mDir == Layout.DIR_LEFT_TO_RIGHT) == runIsRtl) {
            float w = -measureRun(start, limit, limit, runIsRtl, null);
            handleRun(start, limit, limit, runIsRtl, c, x + w, top, y, bottom, null, false);
            return w;
        }
        return handleRun(start, limit, limit, runIsRtl, c, x, top, y, bottom, null, needWidth);
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
    private float measureRun(int start, int offset, int limit, boolean runIsRtl, Paint.FontMetricsInt fmi) {
        return handleRun(start, offset, limit, runIsRtl, null, 0, 0, 0, 0, fmi, true);
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
    private float handleRun(int start, int measureLimit, int limit, boolean runIsRtl, Canvas c, float x,
                            int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {
        // Case of an empty line, make sure we update fmi according to mPaint
        if (start == measureLimit) {
            TextPaint wp = mWorkPaint;
            wp.set(mPaint);
            if (fmi != null) {
                expandMetricsFromPaint(fmi, wp);
            }
            return 0f;
        }
        if (mSpanned == null) {
            TextPaint wp = mWorkPaint;
            wp.set(mPaint);
            final int mlimit = measureLimit;
            return handleText(wp, start, mlimit, start, limit, runIsRtl, c, x, top, y, bottom, fmi, needWidth || mlimit < measureLimit);
        }
        mMetricAffectingSpanSpanSet.init(mSpanned, mStart + start, mStart + limit);
        mCharacterStyleSpanSet.init(mSpanned, mStart + start, mStart + limit);
        // Shaping needs to take into account context up to metric boundaries,
        // but rendering needs to take into account character style boundaries.
        // So we iterate through metric runs to get metric bounds,
        // then within each metric run iterate through character style runs
        // for the run bounds.
        final float originalX = x;
        for (int i = start, inext; i < measureLimit; i = inext) {
            TextPaint wp = mWorkPaint;
            wp.set(mPaint);
            inext = mMetricAffectingSpanSpanSet.getNextTransition(mStart + i, mStart + limit) - mStart;
            int mlimit = Math.min(inext, measureLimit);
            ReplacementSpan replacement = null;
            for (int j = 0; j < mMetricAffectingSpanSpanSet.getNumberOfSpans(); j++) {
                // Both intervals [spanStarts..spanEnds] and [mStart + i..mStart + mlimit] are NOT
                // empty by construction. This special case in getSpans() explains the >= & <= tests
                if ((mMetricAffectingSpanSpanSet.getSpanStarts()[j] >= mStart + mlimit) ||
                        (mMetricAffectingSpanSpanSet.getSpanEnds()[j] <= mStart + i)) continue;
                MetricAffectingSpan span = mMetricAffectingSpanSpanSet.getSpans()[j];
                if (span instanceof ReplacementSpan) {
                    replacement = (ReplacementSpan) span;
                } else {
                    // We might have a replacement that uses the draw
                    // state, otherwise measure state would suffice.
                    span.updateDrawState(wp);
                }
            }
            if (replacement != null) {
                x += handleReplacement(replacement, wp, i, mlimit, runIsRtl, c, x, top, y,
                        bottom, fmi, needWidth || mlimit < measureLimit);
                continue;
            }
            if (c == null) {
                x += handleText(wp, i, mlimit, i, inext, runIsRtl, c, x, top,
                        y, bottom, fmi, needWidth || mlimit < measureLimit);
            } else {
                for (int j = i, jnext; j < mlimit; j = jnext) {
                    jnext = mCharacterStyleSpanSet.getNextTransition(mStart + j, mStart + mlimit) -
                            mStart;

                    wp.set(mPaint);
                    for (int k = 0; k < mCharacterStyleSpanSet.getNumberOfSpans(); k++) {
                        // Intentionally using >= and <= as explained above
                        if ((mCharacterStyleSpanSet.getSpanStarts()[k] >= mStart + jnext) ||
                                (mCharacterStyleSpanSet.getSpanEnds()[k] <= mStart + j)) continue;

                        CharacterStyle span = mCharacterStyleSpanSet.getSpans()[k];
                        span.updateDrawState(wp);
                    }

                    x += handleText(wp, j, jnext, i, inext, runIsRtl, c, x,
                            top, y, bottom, fmi, needWidth || jnext < measureLimit);
                }
            }
        }
        return x - originalX;
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
    private float handleReplacement(ReplacementSpan replacement, TextPaint wp, int start, int limit, boolean runIsRtl, Canvas c,
                                    float x, int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {
        float ret = 0;
        int textStart = mStart + start;
        int textLimit = mStart + limit;
        if (needWidth || (c != null && runIsRtl)) {
            int previousTop = 0;
            int previousAscent = 0;
            int previousDescent = 0;
            int previousBottom = 0;
            int previousLeading = 0;
            boolean needUpdateMetrics = (fmi != null);
            if (needUpdateMetrics) {
                previousTop = fmi.top;
                previousAscent = fmi.ascent;
                previousDescent = fmi.descent;
                previousBottom = fmi.bottom;
                previousLeading = fmi.leading;
            }
            ret = replacement.getSize(wp, mText, textStart, textLimit, fmi);
            if (needUpdateMetrics) {
                updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
            }
        }
        if (c != null) {
            if (runIsRtl) {
                x -= ret;
            }
            replacement.draw(c, mText, textStart, textLimit, x, top, y, bottom, wp);
        }
        return runIsRtl ? -ret : ret;
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
    private float handleText(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl,
                             Canvas c, float x, int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {
        // Get metrics first (even for empty strings or "0" width runs)
        if (fmi != null) {
            expandMetricsFromPaint(fmi, wp);
        }
        int runLen = end - start;
        // No need to do anything if the run width is "0"
        if (runLen == 0) {
            return 0f;
        }
        float ret = 0;
        int contextLen = contextEnd - contextStart;
        if (needWidth || (c != null && (wp.bgColor != 0 || runIsRtl))) {
            int flags = runIsRtl ? 1 : 0;
            if (mCharsValid) {
                //这里是当有多个spannable的时候 需要计算出每个spannable占的宽度 计算出下一个spannable的位置
                ret = wp.measureText(mChars, start, runLen);
            } else {
                int delta = mStart;
                ret = wp.measureText(mText, delta + start, delta + end);
            }
        }
        if (c != null) {
            if (runIsRtl) {
                x -= ret;
            }
            if (wp.bgColor != 0) {
                int previousColor = wp.getColor();
                Paint.Style previousStyle = wp.getStyle();
                wp.setColor(wp.bgColor);
                wp.setStyle(Paint.Style.FILL);
                c.drawRect(x, top, x + ret, bottom, wp);
                wp.setStyle(previousStyle);
                wp.setColor(previousColor);
            }
            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl, x, y + wp.baselineShift);
        }

        return runIsRtl ? -ret : ret;
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
    private void drawTextRun(Canvas c, TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, float x, int y) {
        if (DEBUG) {
            Log.i(TAG, "start:" + start + " end:" + end + " mStart:" + mStart);
        }
        LogUtil.i(TAG, "layout drawTextRun mCharsValid:" + mCharsValid);
        if (mCharsValid) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawText(mChars, start, count, x, y, wp);
        } else {
            int delta = mStart;
            int count = end - start;
            if (mNeedJustify) {
                float useWidth = mWidth;
                float charLen = x;
                int charTotalLen = 0;
                String chars_ = mText.subSequence(delta + start, delta + end).toString();
                LogUtil.i(TAG, "layout drawTextRun mNeedJustify:" + chars_);
                float[] widths = new float[chars_.length()];
                for (int i = 0; i < chars_.length(); i++) {
                    float charWidth = mPaint.measureText(String.valueOf(chars_.charAt(i)));
                    widths[i] = charWidth;
                    charTotalLen += charWidth;
                }
                if (mLen > count) {
                    useWidth = mWidth * count / mLen;
                    if (useWidth < charTotalLen) {
                        useWidth = charTotalLen;
                    }
                }
                LogUtil.i(TAG, "layout count:" + count + " mLen:" + mLen);
                LogUtil.i(TAG, "layout useWidth:" + useWidth + " charTotalLen:" + charTotalLen);
                float mAddedWidth = (useWidth - charTotalLen) / count;
                LogUtil.i(TAG, "layout mAddedWidth:" + mAddedWidth);
                if (start != 0) {
                    LogUtil.i(TAG, "layout drawTextRun start != mStart before:" + charLen);
                    charLen += mLastUseExtraWidth;
                    LogUtil.i(TAG, "layout drawTextRun start != mStart after:" + charLen);
                }
                for (int j = 0; j < count; j++) {
                    String char_ = chars_.substring(j, j + 1);
                    float drawX = charLen + mAddedWidth * j;
                    //绘制中文破折号——
                    if (chars_.contains("——")) {
                        if (TextUtils.equals(char_, "—") && isDashIndex(chars_, j)) {
                            drawX = charLen + mAddedWidth * j + mAddedWidth / 2f;
                            c.drawText("——", drawX, y, wp);
                        } else if (TextUtils.equals(char_, "—") && isDashIndex(chars_, j - 1)) {

                        } else {
                            c.drawText(char_, drawX, y, wp);
                        }
                    } else {
                        c.drawText(char_, drawX, y, wp);
                    }
                    charLen += widths[j];
                    mLastUseExtraWidth += mAddedWidth;
                }
            } else {
                c.drawText(mText, delta + start, delta + end, x, y, wp);
            }
        }
    }

    //是否是破折号
    private boolean isDashIndex(String text, int position) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        List<Integer> list = new ArrayList<>();
        int index = text.indexOf("——");
        while (index >= 0) {
            list.add(index);
            index = text.indexOf("——", index + 2);
        }
        return list.contains(position);
    }

    private void expandMetricsFromPaint(Paint.FontMetricsInt fmi, TextPaint wp) {
        final int previousTop = fmi.top;
        final int previousAscent = fmi.ascent;
        final int previousDescent = fmi.descent;
        final int previousBottom = fmi.bottom;
        final int previousLeading = fmi.leading;
        wp.getFontMetricsInt(fmi);
        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
    }

    private void updateMetrics(Paint.FontMetricsInt fmi, int previousTop, int previousAscent,
                               int previousDescent, int previousBottom, int previousLeading) {
        fmi.top = Math.min(fmi.top, previousTop);
        fmi.ascent = Math.min(fmi.ascent, previousAscent);
        fmi.descent = Math.max(fmi.descent, previousDescent);
        fmi.bottom = Math.max(fmi.bottom, previousBottom);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    /**
     * Returns a new TextLine from the shared pool.
     *
     * @return an uninitialized TextLine
     */
    public static TextLine obtain() {
        TextLine tl;
        synchronized (mCache) {
            for (int i = mCache.length; --i >= 0; ) {
                if (mCache[i] != null) {
                    tl = mCache[i];
                    mCache[i] = null;
                    return tl;
                }
            }
        }
        tl = new TextLine();
        if (DEBUG) {
            LogUtil.i(TAG, "new: " + tl);
        }
        return tl;
    }

    /**
     * Puts a TextLine back into the shared pool. Do not use this TextLine once it has been returned.
     *
     * @param tl the textLine
     * @return null, as a convenience from clearing references to the provided TextLine
     */
    public static TextLine recycle(TextLine tl) {
        if (tl == null) {
            return null;
        }
        tl.mText = null;
        tl.mPaint = null;
        tl.mMetricAffectingSpanSpanSet.recycle();
        tl.mCharacterStyleSpanSet.recycle();
        tl.mReplacementSpanSpanSet.recycle();
        synchronized (mCache) {
            for (int i = 0; i < mCache.length; ++i) {
                if (mCache[i] == null) {
                    mCache[i] = tl;
                    break;
                }
            }
        }
        return null;
    }

}
