package com.android.widget.justtext;

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

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2024/12/6
 * Desc:
 */
class TextLine {

    private TextPaint mPaint;
    private CharSequence mText;
    private int mStart;
    private int mEnd;
    private int mLen;
    private int mDir;
    private boolean mHasTabs;
    private JustTextView.TabStops mTabs;
    private Layout.Directions mDirections;
    private char[] mChars;
    private boolean mCharsValid;
    private Spanned mSpanned;
    private float mWidth;
    private float mLastUseExtraWidth;

    private final TextPaint mWorkPaint = new TextPaint();
    private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet =
            new SpanSet<MetricAffectingSpan>(MetricAffectingSpan.class);
    private final SpanSet<CharacterStyle> mCharacterStyleSpanSet =
            new SpanSet<CharacterStyle>(CharacterStyle.class);
    private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet =
            new SpanSet<ReplacementSpan>(ReplacementSpan.class);

    private static final TextLine[] sCached = new TextLine[3];

    private boolean mNeedJustify = false;

    private String mFirstIndentText;  //第一行最左边的缩进文本，不会绘制在开头，为什么第一行缩进要用文本，因为要参与整个字符串的换行和相关计算
    private float mLastIndentLength;  //最后一行最右边的缩进长度
    private final ArrayList<SpanConfig> mSpanConfigList = new ArrayList<>();  //配置的字体样式列表

    public void justify(float justifyWidth) {
        mWidth = justifyWidth;
    }

    public void setFirstIndentText(String firstIndentText) {
        this.mFirstIndentText = firstIndentText;
    }

    public void setLastIndentLength(float lastIndentLength) {
        this.mLastIndentLength = lastIndentLength;
    }

    public void setSpanConfigList(@NonNull ArrayList<SpanConfig> configs) {
        this.mSpanConfigList.clear();
        this.mSpanConfigList.addAll(configs);
    }

    /**
     * Returns a new TextLine from the shared pool.
     *
     * @return an uninitialized TextLine
     */
    static TextLine obtain() {
        TextLine tl;
        synchronized (sCached) {
            for (int i = sCached.length; --i >= 0; ) {
                if (sCached[i] != null) {
                    tl = sCached[i];
                    sCached[i] = null;
                    return tl;
                }
            }
        }
        tl = new TextLine();
        if (JustUtils.DEBUG) {
            Log.v("TLINE", "new: " + tl);
        }
        return tl;
    }

    /**
     * Puts a TextLine back into the shared pool. Do not use this TextLine once
     * it has been returned.
     *
     * @param tl the textLine
     * @return null, as a convenience from clearing references to the provided
     * TextLine
     */
    static TextLine recycle(TextLine tl) {
        tl.mText = null;
        tl.mPaint = null;

        tl.mMetricAffectingSpanSpanSet.recycle();
        tl.mCharacterStyleSpanSet.recycle();
        tl.mReplacementSpanSpanSet.recycle();

        tl.mFirstIndentText = null;
        tl.mLastIndentLength = 0;
        tl.mSpanConfigList.clear();

        synchronized (sCached) {
            for (int i = 0; i < sCached.length; ++i) {
                if (sCached[i] == null) {
                    sCached[i] = tl;
                    break;
                }
            }
        }
        return null;
    }

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
    void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Layout.Directions directions,
             boolean hasTabs, boolean needJustify, JustTextView.TabStops tabStops) {
        mPaint = paint;
        mText = text;
        mStart = start;
        mLen = limit - start;
        mEnd = limit;
        mDir = dir;
        mDirections = directions;
        mHasTabs = hasTabs;
        mSpanned = null;
        mNeedJustify = needJustify;
        mLastUseExtraWidth = 0;

        boolean hasReplacement = false;
        if (text instanceof Spanned) {
            mSpanned = (Spanned) text;
            mReplacementSpanSpanSet.init(mSpanned, start, limit);
            hasReplacement = mReplacementSpanSpanSet.numberOfSpans > 0;
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
        mTabs = tabStops;
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
    void draw(Canvas c, float x, int top, int y, int bottom) {
        drawRun(c, 0, mLen, false, x, top, y, bottom, false);
    }

    /**
     * Returns metrics information for the entire line.
     *
     * @param fmi receives font metrics information, can be null
     * @return the signed width of the line
     */
    float metrics(Paint.FontMetricsInt fmi) {
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
     * @return the signed offset from the leading margin to the requested
     * character edge.
     */
    float measure(int offset, boolean trailing, Paint.FontMetricsInt fmi) {
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
    private float drawRun(Canvas c, int start,
                          int limit, boolean runIsRtl, float x, int top, int y, int bottom,
                          boolean needWidth) {

        if ((mDir == Layout.DIR_LEFT_TO_RIGHT) == runIsRtl) {
            float w = -measureRun(start, limit, limit, runIsRtl, null);
            handleRun(start, limit, limit, runIsRtl, c, x + w, top,
                    y, bottom, null, false);
            return w;
        }

        return handleRun(start, limit, limit, runIsRtl, c, x, top,
                y, bottom, null, needWidth);
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
    private float measureRun(int start, int offset, int limit, boolean runIsRtl,
                             Paint.FontMetricsInt fmi) {
        return handleRun(start, offset, limit, runIsRtl, null, 0, 0, 0, 0, fmi, true);
    }

    /**
     * @param wp
     */
    private static void expandMetricsFromPaint(Paint.FontMetricsInt fmi, TextPaint wp) {
        final int previousTop = fmi.top;
        final int previousAscent = fmi.ascent;
        final int previousDescent = fmi.descent;
        final int previousBottom = fmi.bottom;
        final int previousLeading = fmi.leading;

        wp.getFontMetricsInt(fmi);

        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom,
                previousLeading);
    }

    static void updateMetrics(Paint.FontMetricsInt fmi, int previousTop, int previousAscent,
                              int previousDescent, int previousBottom, int previousLeading) {
        fmi.top = Math.min(fmi.top, previousTop);
        fmi.ascent = Math.min(fmi.ascent, previousAscent);
        fmi.descent = Math.max(fmi.descent, previousDescent);
        fmi.bottom = Math.max(fmi.bottom, previousBottom);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    /**
     * Utility function for measuring and rendering text.  The text must
     * not include a tab or emoji.
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
     * @return the signed width of the run based on the run direction; only
     * valid if needWidth is true
     */
    private float handleText(TextPaint wp, int start, int end,
                             int contextStart, int contextEnd, boolean runIsRtl,
                             Canvas c, float x, int top, int y, int bottom,
                             Paint.FontMetricsInt fmi, boolean needWidth) {

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

            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl,
                    x, y + wp.baselineShift);
        }

        return runIsRtl ? -ret : ret;
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
     * @return the signed width of the run based on the run direction; only
     * valid if needWidth is true
     */
    private float handleReplacement(ReplacementSpan replacement, TextPaint wp,
                                    int start, int limit, boolean runIsRtl, Canvas c,
                                    float x, int top, int y, int bottom, Paint.FontMetricsInt fmi,
                                    boolean needWidth) {

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
                updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom,
                        previousLeading);
            }
        }

        if (c != null) {
            if (runIsRtl) {
                x -= ret;
            }
            replacement.draw(c, mText, textStart, textLimit,
                    x, top, y, bottom, wp);
        }

        return runIsRtl ? -ret : ret;
    }

    /**
     * Utility function for handling a unidirectional run.  The run must not
     * contain tabs or emoji but can contain styles.
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
    private float handleRun(int start, int measureLimit,
                            int limit, boolean runIsRtl, Canvas c, float x, int top, int y,
                            int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {

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
            return handleText(wp, start, mlimit, start, limit, runIsRtl, c, x, top,
                    y, bottom, fmi, needWidth || mlimit < measureLimit);
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

            inext = mMetricAffectingSpanSpanSet.getNextTransition(mStart + i, mStart + limit) -
                    mStart;
            int mlimit = Math.min(inext, measureLimit);

            ReplacementSpan replacement = null;

            for (int j = 0; j < mMetricAffectingSpanSpanSet.numberOfSpans; j++) {
                // Both intervals [spanStarts..spanEnds] and [mStart + i..mStart + mlimit] are NOT
                // empty by construction. This special case in getSpans() explains the >= & <= tests
                if ((mMetricAffectingSpanSpanSet.spanStarts[j] >= mStart + mlimit) ||
                        (mMetricAffectingSpanSpanSet.spanEnds[j] <= mStart + i)) continue;
                MetricAffectingSpan span = mMetricAffectingSpanSpanSet.spans[j];
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
                    for (int k = 0; k < mCharacterStyleSpanSet.numberOfSpans; k++) {
                        // Intentionally using >= and <= as explained above
                        if ((mCharacterStyleSpanSet.spanStarts[k] >= mStart + jnext) ||
                                (mCharacterStyleSpanSet.spanEnds[k] <= mStart + j)) continue;

                        CharacterStyle span = mCharacterStyleSpanSet.spans[k];
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
    private void drawTextRun(Canvas c, TextPaint wp, int start, int end,
                             int contextStart, int contextEnd, boolean runIsRtl, float x, int y) {

        Log("start:" + start + " end:" + end + " mStart:" + mStart + " layout drawTextRun mCharsValid:" + mCharsValid);
        if (mCharsValid) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawText(mChars, start, count, x, y, wp);
        } else {
            int delta = mStart;
            int count = end - start;
            float useWidth = mWidth;  //文本一行总宽度
            float charLen = x;
            int charTotalLen = 0;  //文本测量后总长度
            String chars_ = mText.subSequence(delta + start, delta + end).toString();
            //设置首行缩进
            float firstIndentLength = 0;
            if (JustUtils.isFirstIndent(mFirstIndentText)) {
                firstIndentLength = mPaint.measureText(mFirstIndentText);
                useWidth -= firstIndentLength;
                charLen += firstIndentLength;
                chars_ = chars_.replaceFirst(mFirstIndentText, "");  //去掉缩进文本
                count = chars_.length();
            }
            float[] widths = new float[chars_.length()];
            for (int i = 0; i < chars_.length(); i++) {
                String char_ = String.valueOf(chars_.charAt(i));
                float charWidth = mPaint.measureText(char_);
                SpanConfig config = JustUtils.getSpanConfig(mSpanConfigList, delta + start + i);  //delta + start + i：当前要绘制的文字位置
                if (config != null && config.isSizeConfig() && config.getTextSize() != mPaint.getTextSize()) {  //字体大小不同
                    TextPaint spanPaint = JustUtils.copyPaint(mPaint);
                    spanPaint.setTextSize(config.getTextSize());
                    charWidth = spanPaint.measureText(char_);  //重新测量文本宽度
                }
                widths[i] = charWidth;
                charTotalLen += charWidth;
            }
            if (mLen > count && !JustUtils.isFirstIndent(mFirstIndentText)) {
                useWidth = mWidth * count / mLen;
                if (useWidth < charTotalLen) {
                    useWidth = charTotalLen;
                }
            }
            //需要两端对齐时每个字额外加的偏移，无需对齐时取0
            //useWidth < charTotalLen 出现的情况：设置一行文本中部分字体大小比原先的大
            float mAddedWidth = mNeedJustify || useWidth < charTotalLen ? (useWidth - charTotalLen) / count : 0;
            if (mAddedWidth > (float) charTotalLen / chars_.length() / 2.5f) {
                mAddedWidth = 0;
            }
            Log("layout count:" + count + " mLen:" + mLen + " useWidth:" + useWidth + " charTotalLen:" + charTotalLen + " mAddedWidth:" + mAddedWidth + " " + chars_);
            if (start != 0) {
                Log("layout drawTextRun start != mStart before:" + charLen);
                charLen += mLastUseExtraWidth;
                Log("layout drawTextRun start != mStart after:" + charLen);
            }
            /**=========================== 逐字绘制 start ===========================*/
            String lastSuffix = "...";
            float lastSuffixWidth = mPaint.measureText(lastSuffix + "哈");  //多留一个字间距
            //所有文本绘制都在这里
            for (int j = 0; j < count; j++) {
                String char_ = chars_.substring(j, j + 1);
                float drawX = charLen + mAddedWidth * j;
                int curPosition = delta + start + j;  //当前要绘制的文字位置
                if (JustUtils.DEBUG) {
                    String msg = "行字数:" + count + "，" + char_ + "[" + curPosition + "]" +
                            "，x:" + new DecimalFormat("#0.0").format(drawX) +
                            "，行宽:" + useWidth + "，字宽:" + charTotalLen +
                            "，偏移:" + new DecimalFormat("#0.0").format(mAddedWidth) +
                            "，首行:" + firstIndentLength + "，末行:" + mLastIndentLength +
                            "，后缀:" + lastSuffixWidth + "，可用:" + (useWidth - mLastIndentLength + firstIndentLength);
                    Log(msg);
                }
                //设置末行缩进
                if (JustUtils.isLastIndent(mLastIndentLength)) {
                    if (JustUtils.DEBUG) {
                        String msg = "总字数:" + mText.length() + "，行字数:" + count + "，" + char_ + "[" + curPosition + "]" +
                                "，x:" + (drawX + lastSuffixWidth) + "，可用宽度:" + (useWidth - mLastIndentLength + firstIndentLength) +
                                "，开始位置:" + start + "，结束位置:" + end;
                        Log(msg);
                    }
                    if (drawX + lastSuffixWidth > useWidth - mLastIndentLength + firstIndentLength) {   //一行不够绘制
                        //如果同时设置了首行和末行缩进且文本只有一行，则后面比较要加上firstIndentLength（首行缩进宽度），
                        //因为drawX是包含首行缩进宽度的，而useWidth首行缩进时是减去首行缩进宽度的
                        c.drawText(lastSuffix, drawX, y, wp);  //绘制...
                        break;
                    } else if (j == count - 1 && !JustUtils.isDrawAllText(curPosition + 1, mText)) {  //一行足够绘制，但是后面还有内容
                        c.drawText(lastSuffix, drawX + widths[j], y, wp);  //绘制...
                    }
                }
                //设置字体样式
                SpanConfig config = JustUtils.getSpanConfig(mSpanConfigList, curPosition);
                boolean isSpanConfig = config != null;
                int baseY = y;  //文本绘制的基线y坐标
                TextPaint spanPaint = JustUtils.copyPaint(wp);
                if (isSpanConfig) {  //有配置字体样式
                    if (config.isBoldConfig()) {
                        spanPaint.setFakeBoldText(true);  //加粗
                    }
                    if (config.isColorConfig()) {
                        spanPaint.setColor(config.getTextColor());  //字体颜色
                    }
                    if (config.isSizeConfig() && config.getTextSize() != wp.getTextSize()) {
                        spanPaint.setTextSize(config.getTextSize());  //字体大小
                        if (config.isAlignLineCenter()) {  //如果设置了同一行文本居中对齐，则调整绘制的基线位置，默认基线y坐标是相同的
                            baseY = (int) (y - (JustUtils.getDistanceFromCenterToBaseLine(wp) - JustUtils.getDistanceFromCenterToBaseLine(spanPaint)));
                        }
                    }
                }
                //逐字绘制文本
                if (chars_.contains("——")) {  //绘制中文破折号——，如果有的话
                    if (TextUtils.equals(char_, "—") && isDashIndex(chars_, j)  //第一个中文破折号位置
                            && !(JustUtils.isLastIndent(mLastIndentLength) && drawX + widths[j] + lastSuffixWidth > useWidth - mLastIndentLength + firstIndentLength)) {  //绘制第二个中文破折号的时候还有地方可以绘制
                        drawX = charLen + mAddedWidth * j + mAddedWidth / 2f;
                        c.drawText("——", drawX, baseY, isSpanConfig ? spanPaint : wp);
                    } else if (TextUtils.equals(char_, "—") && isDashIndex(chars_, j - 1)) {

                    } else {
                        c.drawText(char_, drawX, baseY, isSpanConfig ? spanPaint : wp);
                    }
                } else {
                    c.drawText(char_, drawX, baseY, isSpanConfig ? spanPaint : wp);
                }
                charLen += widths[j];
                mLastUseExtraWidth += mAddedWidth;
            }
            /**=========================== 逐字绘制 end ===========================*/
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

    private void Log(String msg) {
        if (JustUtils.DEBUG) {
            Log.i("JustTextView", msg);  //绘制长文本时打印日志会耗时，所以非调试不要打开
        }
    }

}


