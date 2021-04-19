package com.android.widget.AlignTextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AlignmentSpan;
import android.text.style.ParagraphStyle;
import android.text.style.TabStopSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.java.R;

import java.util.Arrays;

import static android.text.Layout.DIR_LEFT_TO_RIGHT;

/**
 * Created by xuzhb on 2021/4/13
 * Desc:两端对齐的TextView
 */
public class AlignTextView extends AppCompatTextView {

    private static final int TAB_INCREMENT = 20;
    private static final ParagraphStyle[] NO_PARA_SPANS = ArrayUtil.emptyArray(ParagraphStyle.class);
    private static final Rect mTempRect = new Rect();
    private boolean isAlign;

    public boolean isAlign() {
        return isAlign;
    }

    public void setAlign(boolean align) {
        isAlign = align;
    }

    public AlignTextView(@NonNull Context context) {
        super(context);
    }

    public AlignTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView);
        isAlign = ta.getBoolean(R.styleable.AlignTextView_align, true);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Layout layout = getLayout();
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int extendedPaddingTop = getExtendedPaddingTop();
        int voffsetText = 0;
        canvas.save();
        canvas.translate(compoundPaddingLeft, extendedPaddingTop + voffsetText);
        long lineRange = getLineRangeForDraw(canvas, layout);
        int firstLine = unpackRangeStartFromLong(lineRange);
        int lastLine = unpackRangeEndFromLong(lineRange);
        if (lastLine < 0) {
            return;
        }
        drawText(canvas, firstLine, lastLine, layout);
        canvas.restore();
    }

    private void drawText(Canvas canvas, int firstLine, int lastLine, Layout layout) {
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
                    spanEnd = sp.nextSpanTransition(start, textLength, ParagraphStyle.class);
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
            final int indentWidth;
            if (align == Layout.Alignment.ALIGN_NORMAL) {
                if (dir == DIR_LEFT_TO_RIGHT) {
                    indentWidth = 0;
                    x = left;
                } else {
                    indentWidth = 0;
                    x = right;
                }
            } else {
                indentWidth = 0;
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
            if (!(getText() instanceof Spanned) && !hasTabOrEmoji && !isAlign) {
                //directions == DIRS_ALL_LEFT_TO_RIGHT && to be true
                // XXX: assumes there's nothing additional to be done
                canvas.drawText(buf, start, end, x, lbaseline, paint);
            } else {
                boolean needJustify = isJustificationRequired(layout, buf, i) && isAlign;
                tl.set(paint, buf, start, end, dir, hasTabOrEmoji, needJustify);
                if (needJustify) {
                    tl.align(right - left - indentWidth);
                }
                tl.draw(canvas, x, ltop, lbaseline, lbottom);
            }
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
    private final int getLineEnd(Layout layout, int line) {
        return layout.getLineStart(line + 1);
    }

    private <T> T[] getParagraphSpans(Spanned text, int start, int end, Class<T> type) {
        if (start == end && start > 0) {
            return ArrayUtil.emptyArray(type);
        }
        if (text instanceof SpannableStringBuilder) {
            return ((SpannableStringBuilder) text).getSpans(start, end, type);
        } else {
            return text.getSpans(start, end, type);
        }
    }

    /**
     * @param canvas
     * @return The range of lines that need to be drawn, possibly empty.
     * @hide
     */
    private long getLineRangeForDraw(Canvas canvas, Layout layout) {
        int dtop, dbottom;
        synchronized (mTempRect) {
            if (!canvas.getClipBounds(mTempRect)) {
                // Negative range end used as a special flag
                return packRangeInLong(0, -1);
            }
            dtop = mTempRect.top;
            dbottom = mTempRect.bottom;
        }
        final int top = Math.max(dtop, 0);
        final int bottom = Math.min(layout.getLineTop(getLineCount()), dbottom);
        if (top >= bottom) {
            return packRangeInLong(0, -1);
        }
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
        int dir = layout.getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        tl.set(getPaint(), getText(), start, end, dir, hasTabsOrEmoji, false);
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    private long packRangeInLong(int start, int end) {
        return (((long) start) << 32) | end;
    }

    private int unpackRangeStartFromLong(long range) {
        return (int) (range >>> 32);
    }

    private int unpackRangeEndFromLong(long range) {
        return (int) (range & 0x00000000FFFFFFFFL);
    }

    public static class TabStops {

        private int[] mStops;

        TabStops(int increment, Object[] spans) {
            reset(increment, spans);
        }

        private void reset(int increment, Object[] spans) {
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
        }

    }

}
