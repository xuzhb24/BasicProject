package com.android.widget.justtext;

import static android.text.Layout.DIR_LEFT_TO_RIGHT;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.ParagraphStyle;
import android.text.style.TabStopSpan;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.java.R;
import com.android.universal.BuildConfig;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xuzhb on 2024/12/4
 * Desc:两端对齐文本，不适用SpannableString
 */
public class JustTextView extends AppCompatTextView {
    private static final ParagraphStyle[] NO_PARA_SPANS = ArrayUtils.emptyArray(ParagraphStyle.class);

    private boolean isTextBold;      //自定义加粗效果

    public JustTextView(Context context) {
        super(context);
        init(context, null);
    }

    public JustTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JustTextView);
            isTextBold = a.getBoolean(R.styleable.JustTextView_text_bold, false);
            a.recycle();
        }
        if (isTextBold) {
            getPaint().setFakeBoldText(true);
        }
        setEllipsize(null);
    }

    private String mFirstIndentText;  //第一行最左边的缩进文本，不会绘制在开头，为什么第一行缩进要用文本，因为要参与整个字符串的换行和相关计算
    private float mLastIndentLength;  //最后一行最右边的缩进长度
    private final ArrayList<SpanConfig> mSpanConfigList = new ArrayList<>();  //配置的字体样式列表
    private boolean showLastIndentWhenSetMaxLinesIfDrawAllText = false;  //设置maxLines后如果到最大行数就绘制完全部文本时是否依然显示末行缩进，默认不显示

    //设置文本，带首行缩进，如果不带首行缩进直接调用setText
    public void setTextWithFirstIndent(String text, float firstIndentLength) {
        setTextWithIndent(text, firstIndentLength, 0);
    }

    public void setLastIndentLength(float lastIndentLength) {
        this.mLastIndentLength = lastIndentLength > 0 ? lastIndentLength : 0;
        this.invalidate();
    }

    public void setTextWithIndent(String text, float firstIndentLength, float lastIndentLength) {
        String firstIndentText = firstIndentLength > 0 ? JustUtils.createFirstIndent(firstIndentLength, getTextSize()) : "";
        setTextWithIndent(text, firstIndentText, lastIndentLength);
    }

    //设置缩进
    public void setTextWithIndent(String text, String firstIndentText, float lastIndentLength) {
        this.mFirstIndentText = TextUtils.isEmpty(firstIndentText) ? "" : firstIndentText;
        String finalText = TextUtils.isEmpty(text) ? "" : firstIndentText + text;
        this.mLastIndentLength = lastIndentLength > 0 ? lastIndentLength : 0;
        this.setText(finalText);
    }

    public void setSpanConfig(@NonNull SpanConfig config) {
        ArrayList<SpanConfig> configs = new ArrayList<>();
        configs.add(config);
        setSpanConfigList(configs);
    }

    //配置字体样式
    public void setSpanConfigList(@NonNull ArrayList<SpanConfig> configs) {
        this.mSpanConfigList.clear();
        this.mSpanConfigList.addAll(JustUtils.getActiveSpanConfigList(configs, isTextBold));
        this.invalidate();
    }

    public void setShowLastIndentWhenSetMaxLinesIfDrawAllText(boolean showLastIndentWhenSetMaxLinesIfDrawAllText) {
        this.showLastIndentWhenSetMaxLinesIfDrawAllText = showLastIndentWhenSetMaxLinesIfDrawAllText;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long drawStartTime = System.currentTimeMillis();
        Layout layout = getLayout();
        draw(canvas, layout);
        if (BuildConfig.DEBUG) {
            Log.i("绘制耗时", (System.currentTimeMillis() - drawStartTime) + "ms");
        }
    }

    public void draw(Canvas canvas, Layout layout) {
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        int extendedPaddingTop = getExtendedPaddingTop();
        int voffsetText = 0;
        int voffsetCursor = 0;

        canvas.save();
        canvas.translate(compoundPaddingLeft, extendedPaddingTop + voffsetText);
        final long lineRange = getLineRangeForDraw(canvas, layout);
        int firstLine = unpackRangeStartFromLong(lineRange);
        int lastLine = unpackRangeEndFromLong(lineRange);
        if (lastLine < 0) return;

        drawText(canvas, firstLine, lastLine, layout);
        canvas.restore();
    }

    public void drawText(Canvas canvas, int firstLine, int lastLine, Layout layout) {
        int previousLineBottom = layout.getLineTop(firstLine);
        int previousLineEnd = layout.getLineStart(firstLine);
        ParagraphStyle[] spans = NO_PARA_SPANS;
        int spanEnd = 0;
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        CharSequence buf = layout.getText();

        Layout.Alignment paraAlign = layout.getAlignment();
        TabStops tabStops = null;
        boolean tabStopsIsInitialized = false;

        TextLine tl = TextLine.obtain();

        // Draw the lines, one at a time.
        // The baseline is the top of the following line minus the current line's descent.
        for (int i = firstLine; i <= lastLine; i++) {
            int start = previousLineEnd;
            previousLineEnd = layout.getLineStart(i + 1);
            int end = layout.getLineVisibleEnd(i);

            int ltop = previousLineBottom;
            int lbottom = layout.getLineTop(i + 1);
            previousLineBottom = lbottom;
            int lbaseline = lbottom - layout.getLineDescent(i);

            int dir = layout.getParagraphDirection(i);
            int left = 0;
            int right = layout.getWidth();

            if (getText() instanceof Spanned) {
                Spanned sp = (Spanned) buf;
                int textLength = buf.length();
                boolean isFirstParaLine = (start == 0 || buf.charAt(start - 1) == '\n');

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
                    spanEnd = sp.nextSpanTransition(start, textLength,
                            ParagraphStyle.class);
                    spans = getParagraphSpans(sp, start, spanEnd, ParagraphStyle.class);

                    paraAlign = layout.getAlignment();
                    for (int n = spans.length - 1; n >= 0; n--) {
                        if (spans[n] instanceof AlignmentSpan) {
                            paraAlign = ((AlignmentSpan) spans[n]).getAlignment();
                            break;
                        }
                    }

                    tabStopsIsInitialized = false;
                }

                // Draw all leading margin spans.  Adjust left or right according
                // to the paragraph direction of the line.
                final int length = spans.length;
                for (int n = 0; n < length; n++) {
                    if (spans[n] instanceof LeadingMarginSpan) {
                        //TODO
                    }
                }
            }

            boolean hasTabOrEmoji = layout.getLineContainsTab(i);
            // Can't tell if we have tabs for sure, currently
            if (hasTabOrEmoji && !tabStopsIsInitialized) {
                if (tabStops == null) {
                    tabStops = new TabStops(TAB_INCREMENT, spans);
                } else {
                    tabStops.reset(TAB_INCREMENT, spans);
                }
                tabStopsIsInitialized = true;
            }
            // Determine whether the line aligns to normal, opposite, or center.
            Layout.Alignment align = paraAlign;

            int x = 0;
            if (align == Layout.Alignment.ALIGN_NORMAL) {
                if (dir == DIR_LEFT_TO_RIGHT) {
                    x = left;
                } else {
                    x = right;
                }
            } else {
                int max = (int) getLineExtent(i, tabStops, false, layout);
                if (align == Layout.Alignment.ALIGN_OPPOSITE) {
                    if (dir == DIR_LEFT_TO_RIGHT) {
                        x = right - max;
                    } else {
                        x = left - max;
                    }
                } else { // Alignment.ALIGN_CENTER
                    max = max & ~1;
                    x = (right + left - max) >> 1;
                }
            }
            Layout.Directions directions = layout.getLineDirections(i);
            boolean needJustify = isJustificationRequired(layout, buf, i);
            tl.set(paint, buf, start, end, dir, directions, hasTabOrEmoji, needJustify, tabStops);
            tl.justify(right - left);  //设置文本行总宽度
            tl.setFirstIndentText(i == firstLine && JustUtils.isFirstIndent(mFirstIndentText) ? mFirstIndentText : "");  //设置首行缩进
            float lastIndentLength = 0;
            if (i == lastLine) {  //最后一行
                int actualLastLine = lastLine;  //实际的最后一行，兼容设置了maxLines(默认Integer.MAX_VALUE)和minLines(默认0)的情况
                if (getMaxLines() != Integer.MAX_VALUE) {
                    if (showLastIndentWhenSetMaxLinesIfDrawAllText) {
                        actualLastLine = getMaxLines() - 1;
                    } else if (JustUtils.isDrawAllText(end, buf)) {
                        actualLastLine = getMaxLines();
                    }
                } else if (getMinLines() > 0) {
                    actualLastLine = Math.max(lastLine, getMinLines() - 1);
                }
                if (i == actualLastLine) {
                    if (JustUtils.isLastIndent(mLastIndentLength)) {  //手动设置了末行缩进，则以手动设置为准
                        lastIndentLength = mLastIndentLength;
                    } else if (!JustUtils.isDrawAllText(end, buf)) {  //后面还有文本未绘制，标记一下，好在最后一行后面绘制...表示还有内容
                        lastIndentLength = 0.1f;
                    }
                }
                if (JustUtils.DEBUG) {
                    String msg = "当前行:" + i + "，首行:" + firstLine + "，末行:" + lastLine + "，实际末行:" + actualLastLine +
                            "，最小行数:" + getMinLines() + "，最大行数:" + getMaxLines() +
                            "，末行缩进:" + mLastIndentLength + " " + lastIndentLength +
                            "，开始位置:" + start + "，结束位置:" + end
                            + "，总字数:" + buf.length() + "，" + buf;
                    Log(msg);
                    StringBuilder spanSb = new StringBuilder();
                    spanSb.append(" 字体样式\n==================================================================================\n");
                    for (SpanConfig config : mSpanConfigList) {
                        spanSb.append(new Gson().toJson(config)).append("\n");
                    }
                    spanSb.append("==================================================================================");
                    Log(spanSb.toString());
                }
            }
            tl.setLastIndentLength(lastIndentLength);  //设置末行缩进
            tl.setSpanConfigList(mSpanConfigList);     //配置字体样式
            tl.draw(canvas, x, ltop, lbaseline, lbottom);
        }
        TextLine.recycle(tl);
    }


    private boolean isJustificationRequired(Layout layout, CharSequence mText, int lineNum) {
        final int lineEnd = getLineEnd(layout, lineNum);
        return lineEnd < mText.length() && mText.charAt(lineEnd - 1) != '\n';
    }

    /**
     * Return the text offset after the last character on the specified line.
     */
    public final int getLineEnd(Layout layout, int line) {
        return layout.getLineStart(line + 1);
    }

    static <T> T[] getParagraphSpans(Spanned text, int start, int end, Class<T> type) {
        if (start == end && start > 0) {
            return ArrayUtils.emptyArray(type);
        }

        if (text instanceof SpannableStringBuilder) {
            return ((SpannableStringBuilder) text).getSpans(start, end, type);
        } else {
            return text.getSpans(start, end, type);
        }
    }

    private static final Rect sTempRect = new Rect();

    /**
     * @param canvas
     * @return The range of lines that need to be drawn, possibly empty.
     * @hide
     */
    public long getLineRangeForDraw(Canvas canvas, Layout layout) {
        int dtop, dbottom;

        synchronized (sTempRect) {
            if (!canvas.getClipBounds(sTempRect)) {
                // Negative range end used as a special flag
                return packRangeInLong(0, -1);
            }

            dtop = sTempRect.top;
            dbottom = sTempRect.bottom;
        }

        final int top = Math.max(dtop, 0);
        final int bottom = Math.min(layout.getLineTop(getLineCount()), dbottom);

        if (top >= bottom) return packRangeInLong(0, -1);
        return packRangeInLong(layout.getLineForVertical(top), layout.getLineForVertical(bottom));
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
    private float getLineExtent(int line, TabStops tabStops, boolean full, Layout layout) {
        int start = layout.getLineStart(line);
        int end = full ? layout.getLineEnd(line) : layout.getLineVisibleEnd(line);
        boolean hasTabsOrEmoji = layout.getLineContainsTab(line);
        Layout.Directions directions = layout.getLineDirections(line);
        int dir = layout.getParagraphDirection(line);

        TextLine tl = TextLine.obtain();
        tl.set(getPaint(), getText(), start, end, dir, directions, hasTabsOrEmoji, false, tabStops);
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    private void Log(String msg) {
        if (JustUtils.DEBUG) {
            Log.i("JustTextView", msg);  //绘制长文本时打印日志会耗时，所以非调试不要打开
        }
    }

    private static final int TAB_INCREMENT = 20;

    /* package */ static class TabStops {
        private int[] mStops;
        private int mNumStops;
        private int mIncrement;

        TabStops(int increment, Object[] spans) {
            reset(increment, spans);
        }

        void reset(int increment, Object[] spans) {
            this.mIncrement = increment;

            int ns = 0;
            if (spans != null) {
                int[] stops = this.mStops;
                for (Object o : spans) {
                    if (o instanceof TabStopSpan) {
                        if (stops == null) {
                            stops = new int[10];
                        } else if (ns == stops.length) {
                            int[] nstops = new int[ns * 2];
                            for (int i = 0; i < ns; ++i) {
                                nstops[i] = stops[i];
                            }
                            stops = nstops;
                        }
                        stops[ns++] = ((TabStopSpan) o).getTabStop();
                    }
                }
                if (ns > 1) {
                    Arrays.sort(stops, 0, ns);
                }
                if (stops != this.mStops) {
                    this.mStops = stops;
                }
            }
            this.mNumStops = ns;
        }

        float nextTab(float h) {
            int ns = this.mNumStops;
            if (ns > 0) {
                int[] stops = this.mStops;
                for (int i = 0; i < ns; ++i) {
                    int stop = stops[i];
                    if (stop > h) {
                        return stop;
                    }
                }
            }
            return nextDefaultStop(h, mIncrement);
        }

        public static float nextDefaultStop(float h, int inc) {
            return ((int) ((h + inc) / inc)) * inc;
        }
    }

    public static long packRangeInLong(int start, int end) {
        return (((long) start) << 32) | end;
    }

    public static int unpackRangeStartFromLong(long range) {
        return (int) (range >>> 32);
    }

    public static int unpackRangeEndFromLong(long range) {
        return (int) (range & 0x00000000FFFFFFFFL);
    }
}