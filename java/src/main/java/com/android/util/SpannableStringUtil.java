package com.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.base.BaseApplication;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * Created by xuzhb on 2021/4/28
 * Desc:SpannableString工具类
 */
public class SpannableStringUtil {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final int DEFAULT_COLOR = 0x12000000;

    public static final int ALIGN_TOP = 0;       //顶部对齐
    public static final int ALIGN_CENTER = 1;    //居中对齐
    public static final int ALIGN_BOTTOM = 2;    //底部对齐
    public static final int ALIGN_BASELINE = 3;  //基线对齐

    @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Align {
    }

    public static class Builder {

        private CharSequence mText;
        private int mFlag;
        @ColorInt
        private int mForegroundColor;  //前景色
        @ColorInt
        private int mBackgroundColor;  //背景色
        @ColorInt
        private int mQuoteColor;          //引用线的颜色
        private int mQuoteWidth;          //引用线线宽
        private int mQuoteGapWidth;       //引用线和文字间距
        private boolean isLeading;        //是否设置缩进
        private int mFirstLeading;        //首行缩进
        private int mRestLeading;         //剩余行缩进
        private int mMargin;              //间距
        private boolean isBullet;         //是否设置列表标记
        private int mBulletColor;         //列表标记的颜色
        private int mBulletRadius;        //列表标记的半径
        private int mBulletPadding;       //列表标记和文字间距离
        private int mFontSize;            //字体大小
        private boolean isFontSizeDp;     //是否使用dp
        private float mProportion;        //字体比例
        private float mXProportion;       //字体横向比例
        private boolean isStrikethrough;  //是否设置删除线
        private boolean isUnderline;      //是否设置下划线
        private boolean isSuperscript;    //是否设置上标
        private boolean isSubscript;      //是否设置下标
        private boolean isBold;           //是否设置粗体
        private boolean isItalic;         //是否设置斜体
        private boolean isBoldItalic;     //是否设置粗斜体
        private String mFontFamily;       //字体系列：monospace、serif、sans-serif
        private Typeface mTypeface;       //字体
        private boolean isBitmap;      //是否设置图片
        private Bitmap mBitmap;        //图片
        private boolean isDrawable;    //是否设置Drawable
        private Drawable mDrawable;    //Drawable
        private boolean isUri;         //是否设置图片uri
        private Uri mUri;              //图片uri
        private boolean isResourceId;  //是否是资源id
        @DrawableRes
        private int mResourceId;       //资源id

        private ClickableSpan mClickSpan;  //点击事件
        private String mUrl;        //超链接
        private boolean isBlur;     //是否设置模糊
        private float mBlurRadius;  //模糊半径
        private BlurMaskFilter.Blur mStyle;  //模糊样式
        @Align
        private int mVerticalAlignment;  //竖直对齐方式：ALIGN_TOP(顶部对齐)、ALIGN_CENTER(居中对齐)、ALIGN_BOTTOM(底部对齐)、ALIGN_BASELINE(基线对齐)
        private Layout.Alignment mHorizontalAlignment;  //水平对齐方式：ALIGN_NORMAL(正常)、ALIGN_OPPOSITE(相反)、ALIGN_CENTER(居中)

        private SpannableStringBuilder mBuilder;

        public Builder() {
            mFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
            mForegroundColor = DEFAULT_COLOR;
            mBackgroundColor = DEFAULT_COLOR;
            mQuoteColor = DEFAULT_COLOR;
            mMargin = -1;
            mFontSize = -1;
            mProportion = -1;
            mXProportion = -1;
            mVerticalAlignment = ALIGN_BOTTOM;
            mBuilder = new SpannableStringBuilder();
        }

        /**
         * 设置标识
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE：从起始下标到终止下标，包括起始下标，不包括终止下标
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE：从起始下标到终止下标，同时包括起始下标和终止下标
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE：从起始下标到终止下标，但都不包括起始下标和终止下标
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE：从起始下标到终止下标，不包括起始下标，包括终止下标
         */
        public Builder setFlag(int flag) {
            this.mFlag = flag;
            return this;
        }

        /**
         * 设置前景色
         *
         * @param color 前景色
         */
        public Builder setForegroundColor(@ColorInt int color) {
            this.mForegroundColor = color;
            return this;
        }

        /**
         * 设置背景色
         *
         * @param color 背景色
         */
        public Builder setBackgroundColor(@ColorInt int color) {
            this.mBackgroundColor = color;
            return this;
        }

        /**
         * 设置引用线的颜色
         *
         * @param color 引用线的颜色
         */
        public Builder setQuoteColor(@ColorInt int color) {
            this.mQuoteColor = color;
            this.mQuoteWidth = 2;
            this.mQuoteGapWidth = 2;
            return this;
        }

        /**
         * 设置引用线的颜色
         *
         * @param color         引用线的颜色
         * @param stripeWidth   引用线线宽
         * @param quoteGapWidth 引用线和文字间距
         */
        public Builder setQuoteColor(@ColorInt int color, int stripeWidth, int quoteGapWidth) {
            this.mQuoteColor = color;
            this.mQuoteWidth = stripeWidth;
            this.mQuoteGapWidth = quoteGapWidth;
            return this;
        }

        /**
         * 设置缩进
         *
         * @param firstLeading 首行缩进
         * @param restLeading  剩余行缩进
         */
        public Builder setLeadingMargin(int firstLeading, int restLeading) {
            this.mFirstLeading = firstLeading;
            this.mRestLeading = restLeading;
            this.isLeading = true;
            return this;
        }

        /**
         * 设置间距
         *
         * @param margin 间距
         */
        public Builder setMargin(int margin) {
            this.mMargin = margin;
            this.mText = " " + this.mText;
            return this;
        }

        /**
         * 设置列表标记
         *
         * @param gapWidth 列表标记和文字间距离
         */
        public Builder setBullet(@ColorInt int gapWidth) {
            this.mBulletColor = 0;
            this.mBulletRadius = 3;
            this.mBulletPadding = gapWidth;
            this.isBullet = true;
            return this;
        }

        /**
         * 设置列表标记
         *
         * @param color   列表标记的颜色
         * @param radius  列表标记颜色
         * @param padding 列表标记和文字间距离
         */
        public Builder setBullet(@ColorInt int color, int radius, int padding) {
            this.mBulletColor = color;
            this.mBulletRadius = radius;
            this.mBulletPadding = padding;
            this.isBullet = true;
            return this;
        }

        /**
         * 设置字体大小
         *
         * @param size 字体大小
         */
        public Builder setFontSize(int size) {
            this.mFontSize = size;
            this.isFontSizeDp = false;
            return this;
        }

        /**
         * 设置字体大小
         *
         * @param size 字体大小
         * @param isDp 是否使用dp
         */
        public Builder setFontSize(int size, boolean isDp) {
            this.mFontSize = size;
            this.isFontSizeDp = isDp;
            return this;
        }

        /**
         * 设置字体比例
         *
         * @param proportion 比例
         */
        public Builder setFontProportion(float proportion) {
            this.mProportion = proportion;
            return this;
        }

        /**
         * 设置字体横向比例
         *
         * @param proportion 比例
         */
        public Builder setFontXProportion(float proportion) {
            this.mXProportion = proportion;
            return this;
        }

        /**
         * 设置删除线
         */
        public Builder setStrikethrough() {
            this.isStrikethrough = true;
            return this;
        }

        /**
         * 设置下划线
         */
        public Builder setUnderline() {
            this.isUnderline = true;
            return this;
        }

        /**
         * 设置上标
         */
        public Builder setSuperscript() {
            this.isSuperscript = true;
            return this;
        }

        /**
         * 设置下标
         */
        public Builder setSubscript() {
            this.isSubscript = true;
            return this;
        }

        /**
         * 设置粗体
         */
        public Builder setBold() {
            this.isBold = true;
            return this;
        }

        /**
         * 设置斜体
         */
        public Builder setItalic() {
            this.isItalic = true;
            return this;
        }

        /**
         * 设置粗斜体
         */
        public Builder setBoldItalic() {
            this.isBoldItalic = true;
            return this;
        }

        /**
         * 设置字体系列
         *
         * @param fontFamily 字体系列：monospace、serif、sans-serif
         */
        public Builder setFontFamily(@NonNull String fontFamily) {
            this.mFontFamily = fontFamily;
            return this;
        }

        /**
         * 设置字体
         *
         * @param typeface 字体
         */
        public Builder setTypeface(@NonNull Typeface typeface) {
            this.mTypeface = typeface;
            return this;
        }

        /**
         * 设置水平对齐方式
         *
         * @param alignment 水平对齐方式
         *                  ALIGN_NORMAL：正常
         *                  ALIGN_OPPOSITE：相反
         *                  ALIGN_CENTER：居中
         */
        public Builder setAlignment(@NonNull Layout.Alignment alignment) {
            this.mHorizontalAlignment = alignment;
            return this;
        }

        /**
         * 设置图片
         *
         * @param bitmap 图片
         */
        public Builder setBitmap(@NonNull Bitmap bitmap) {
            return setBitmap(bitmap, mVerticalAlignment);
        }

        /**
         * 设置图片
         *
         * @param bitmap 图片位图
         * @param align  竖直对齐方式
         *               ALIGN_TOP：顶部对齐
         *               ALIGN_CENTER：居中对齐
         *               ALIGN_BOTTOM：底部对齐
         *               ALIGN_BASELINE：基线对齐
         */
        public Builder setBitmap(@NonNull Bitmap bitmap, @Align int align) {
            this.mBitmap = bitmap;
            this.mVerticalAlignment = align;
            this.mText = " " + this.mText;
            this.isBitmap = true;
            return this;
        }

        /**
         * 设置Drawable资源
         *
         * @param drawable Drawable资源
         */
        public Builder setDrawable(@NonNull Drawable drawable) {
            return setDrawable(drawable, mVerticalAlignment);
        }

        /**
         * 设置Drawable资源
         *
         * @param drawable Drawable资源
         * @param align    竖直对齐方式
         *                 ALIGN_TOP：顶部对齐
         *                 ALIGN_CENTER：居中对齐
         *                 ALIGN_BOTTOM：底部对齐
         *                 ALIGN_BASELINE：基线对齐
         */
        public Builder setDrawable(@NonNull Drawable drawable, @Align int align) {
            this.mDrawable = drawable;
            this.mVerticalAlignment = align;
            this.mText = " " + this.mText;
            this.isDrawable = true;
            return this;
        }

        /**
         * 设置图片uri
         *
         * @param uri 图片uri
         */
        public Builder setUri(@NonNull Uri uri) {
            return setUri(uri, mVerticalAlignment);
        }

        /**
         * 设置图片uri
         *
         * @param uri   图片uri
         * @param align 竖直对齐方式
         *              ALIGN_TOP：顶部对齐
         *              ALIGN_CENTER：居中对齐
         *              ALIGN_BOTTOM：底部对齐
         *              ALIGN_BASELINE：基线对齐
         */
        public Builder setUri(@NonNull Uri uri, @Align int align) {
            this.mUri = uri;
            this.mVerticalAlignment = align;
            this.mText = " " + this.mText;
            this.isUri = true;
            return this;
        }

        /**
         * 设置资源id
         *
         * @param resourceId 资源id
         */
        public Builder setResourceId(@DrawableRes int resourceId) {
            return setResourceId(resourceId, mVerticalAlignment);
        }

        /**
         * 设置资源id
         *
         * @param resourceId 资源id
         * @param align      竖直对齐方式
         *                   ALIGN_TOP：顶部对齐
         *                   ALIGN_CENTER：居中对齐
         *                   ALIGN_BOTTOM：底部对齐
         *                   ALIGN_BASELINE：基线对齐
         */
        public Builder setResourceId(@DrawableRes int resourceId, @Align int align) {
            this.mResourceId = resourceId;
            this.mVerticalAlignment = align;
            this.mText = " " + this.mText;
            this.isResourceId = true;
            return this;
        }

        /**
         * 设置点击事件
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param clickSpan 点击事件
         */
        public Builder setClickSpan(@NonNull ClickableSpan clickSpan) {
            this.mClickSpan = clickSpan;
            return this;
        }

        /**
         * 设置超链接
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param url 超链接
         */
        public Builder setUrl(@NonNull String url) {
            this.mUrl = url;
            return this;
        }

        /**
         * 设置模糊
         * 尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊
         * 推荐还是把所有字体都模糊这样使用
         *
         * @param radius 模糊半径（需大于0）
         * @param style  模糊样式：NORMAL、SOLID、OUTER、INNER
         */
        public Builder setBlur(float radius, BlurMaskFilter.Blur style) {
            this.mBlurRadius = radius;
            this.mStyle = style;
            this.isBlur = true;
            return this;
        }

        /**
         * 追加一行字符串
         *
         * @param text 字符串
         */
        public Builder appendLine(@NonNull CharSequence text) {
            return append(text + LINE_SEPARATOR);
        }

        /**
         * 追加字符串，不换行
         *
         * @param text 字符串
         */
        public Builder append(@NonNull CharSequence text) {
            createSpan();
            this.mText = text;
            return this;
        }

        /**
         * 创建SpannableStringBuilder
         */
        public SpannableStringBuilder create() {
            createSpan();
            return mBuilder;
        }

        private void createSpan() {
            if (mText == null || mText.length() == 0) {
                return;
            }
            int start = mBuilder.length();
            mBuilder.append(this.mText);
            int end = mBuilder.length();
            if (mBackgroundColor != DEFAULT_COLOR) {
                mBuilder.setSpan(new BackgroundColorSpan(mBackgroundColor), start, end, mFlag);
                mBackgroundColor = DEFAULT_COLOR;
            }
            if (mForegroundColor != DEFAULT_COLOR) {
                mBuilder.setSpan(new ForegroundColorSpan(mForegroundColor), start, end, mFlag);
                mForegroundColor = DEFAULT_COLOR;
            }
            if (isLeading) {
                mBuilder.setSpan(new LeadingMarginSpan.Standard(mFirstLeading, mRestLeading), start, end, mFlag);
                isLeading = false;
            }
            if (mMargin != -1) {
                mBuilder.setSpan(new MarginSpan(mMargin), start, end, mFlag);
                mMargin = -1;
            }
            if (mQuoteColor != DEFAULT_COLOR) {
                mBuilder.setSpan(new CustomQuoteSpan(mQuoteColor, mQuoteWidth, mQuoteGapWidth), start, end, mFlag);
                mQuoteColor = DEFAULT_COLOR;
            }
            if (isBullet) {
                mBuilder.setSpan(new CustomBulletSpan(mBulletColor, mBulletRadius, mBulletPadding), start, end, mFlag);
                isBullet = false;
            }
            if (mFontSize != -1) {
                mBuilder.setSpan(new AbsoluteSizeSpan(mFontSize, isFontSizeDp), start, end, mFlag);
                mFontSize = -1;
                isFontSizeDp = false;
            }
            if (mProportion != -1) {
                mBuilder.setSpan(new RelativeSizeSpan(mProportion), start, end, mFlag);
                mProportion = -1;
            }
            if (mXProportion != -1) {
                mBuilder.setSpan(new ScaleXSpan(mXProportion), start, end, mFlag);
                mXProportion = -1;
            }
            if (isStrikethrough) {
                mBuilder.setSpan(new StrikethroughSpan(), start, end, mFlag);
                isStrikethrough = false;
            }
            if (isUnderline) {
                mBuilder.setSpan(new UnderlineSpan(), start, end, mFlag);
                isUnderline = false;
            }
            if (isSuperscript) {
                mBuilder.setSpan(new SuperscriptSpan(), start, end, mFlag);
                isSuperscript = false;
            }
            if (isSubscript) {
                mBuilder.setSpan(new SubscriptSpan(), start, end, mFlag);
                isSubscript = false;
            }
            if (isBold) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, mFlag);
                isBold = false;
            }
            if (isItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, mFlag);
                isItalic = false;
            }
            if (isBoldItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, mFlag);
                isBoldItalic = false;
            }
            if (mFontFamily != null) {
                mBuilder.setSpan(new TypefaceSpan(mFontFamily), start, end, mFlag);
                mFontFamily = null;
            }
            if (mTypeface != null) {
                mBuilder.setSpan(new CustomTypefaceSpan(mTypeface), start, end, mFlag);
                mTypeface = null;
            }
            if (mHorizontalAlignment != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(mHorizontalAlignment), start, end, mFlag);
                mHorizontalAlignment = null;
            }
            if (isBitmap || isDrawable || isUri || isResourceId) {
                if (isBitmap) {
                    mBuilder.setSpan(new CustomImageSpan(BaseApplication.getInstance(), mBitmap, mVerticalAlignment), start, end, mFlag);
                    mBitmap = null;
                    isBitmap = false;
                } else if (isDrawable) {
                    mBuilder.setSpan(new CustomImageSpan(mDrawable, mVerticalAlignment), start, end, mFlag);
                    mDrawable = null;
                    isDrawable = false;
                } else if (isUri) {
                    mBuilder.setSpan(new CustomImageSpan(BaseApplication.getInstance(), mUri, mVerticalAlignment), start, end, mFlag);
                    mUri = null;
                    isUri = false;
                } else {
                    mBuilder.setSpan(new CustomImageSpan(BaseApplication.getInstance(), mResourceId, mVerticalAlignment), start, end, mFlag);
                    mResourceId = 0;
                    isResourceId = false;
                }
            }
            if (mClickSpan != null) {
                mBuilder.setSpan(mClickSpan, start, end, mFlag);
                mClickSpan = null;
            }
            if (mUrl != null) {
                mBuilder.setSpan(new URLSpan(mUrl), start, end, mFlag);
                mUrl = null;
            }
            if (isBlur) {
                mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(mBlurRadius, mStyle)), start, end, mFlag);
                isBlur = false;
            }
            mFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        }

    }

    private static class MarginSpan extends ReplacementSpan {

        private final int mMargin;

        private MarginSpan(int margin) {
            super();
            this.mMargin = margin;
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
            text = " ";
            return mMargin;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {

        }
    }

    private static class CustomQuoteSpan implements LeadingMarginSpan {

        private final int mColor;
        private final int mStripeWidth;
        private final int mGapWidth;

        private CustomQuoteSpan(@ColorInt int color, int stripeWidth, int gapWidth) {
            super();
            this.mColor = color;
            this.mStripeWidth = stripeWidth;
            this.mGapWidth = gapWidth;
        }

        public int getLeadingMargin(boolean first) {
            return mStripeWidth + mGapWidth;
        }

        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
            Paint.Style style = p.getStyle();
            int color = p.getColor();
            p.setStyle(Paint.Style.FILL);
            p.setColor(this.mColor);
            c.drawRect(x, top, x + dir * mStripeWidth, bottom, p);
            p.setStyle(style);
            p.setColor(color);
        }
    }

    private static class CustomBulletSpan implements LeadingMarginSpan {

        private final int mColor;
        private final int mRadius;
        private final int mGapWidth;

        private static Path mBulletPath = null;

        private CustomBulletSpan(int color, int radius, int gapWidth) {
            this.mColor = color;
            this.mRadius = radius;
            this.mGapWidth = gapWidth;
        }

        public int getLeadingMargin(boolean first) {
            return 2 * mRadius + mGapWidth;
        }

        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
            if (((Spanned) text).getSpanStart(this) == start) {
                Paint.Style style = p.getStyle();
                int oldColor = 0;
                oldColor = p.getColor();
                p.setColor(mColor);
                p.setStyle(Paint.Style.FILL);
                if (c.isHardwareAccelerated()) {
                    if (mBulletPath == null) {
                        mBulletPath = new Path();
                        // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                        mBulletPath.addCircle(0.0f, 0.0f, mRadius, Path.Direction.CW);
                    }
                    c.save();
                    c.translate(x + dir * mRadius, (top + bottom) / 2.0f);
                    c.drawPath(mBulletPath, p);
                    c.restore();
                } else {
                    c.drawCircle(x + dir * mRadius, (top + bottom) / 2.0f, mRadius, p);
                }
                p.setColor(oldColor);
                p.setStyle(style);
            }
        }
    }

    @SuppressLint("ParcelCreator")
    private static class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface mNewType;

        private CustomTypefaceSpan(Typeface type) {
            super("");
            mNewType = type;
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            apply(textPaint, mNewType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            apply(paint, mNewType);
        }

        private static void apply(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }
            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }
            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.setTypeface(tf);
        }
    }

    private static class CustomImageSpan extends CustomDynamicDrawableSpan {

        private Drawable mDrawable;
        private Uri mContentUri;
        private int mResourceId;
        private Context mContext;

        CustomImageSpan(Context context, Bitmap b, int verticalAlignment) {
            super(verticalAlignment);
            mContext = context;
            mDrawable = context != null ? new BitmapDrawable(context.getResources(), b) : new BitmapDrawable(b);
            int width = mDrawable.getIntrinsicWidth();
            int height = mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
        }

        CustomImageSpan(Drawable d, int verticalAlignment) {
            super(verticalAlignment);
            mDrawable = d;
            mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        }

        CustomImageSpan(Context context, Uri uri, int verticalAlignment) {
            super(verticalAlignment);
            mContext = context;
            mContentUri = uri;
        }

        CustomImageSpan(Context context, @DrawableRes int resourceId, int verticalAlignment) {
            super(verticalAlignment);
            mContext = context;
            mResourceId = resourceId;
        }

        @Override
        public Drawable getDrawable() {
            Drawable drawable = null;
            if (mDrawable != null) {
                drawable = mDrawable;
            } else if (mContentUri != null) {
                Bitmap bitmap = null;
                try {
                    InputStream is = mContext.getContentResolver().openInputStream(
                            mContentUri);
                    bitmap = BitmapFactory.decodeStream(is);
                    drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                    Log.e("sms", "Failed to loaded content " + mContentUri, e);
                }
            } else {
                try {
                    drawable = ContextCompat.getDrawable(mContext, mResourceId);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                } catch (Exception e) {
                    Log.e("sms", "Unable to find resource: " + mResourceId);
                }
            }
            return drawable;
        }
    }

    private static abstract class CustomDynamicDrawableSpan extends ReplacementSpan {

        private final int mVerticalAlignment;
        private WeakReference<Drawable> mDrawableRef;

        CustomDynamicDrawableSpan() {
            mVerticalAlignment = ALIGN_BOTTOM;
        }

        CustomDynamicDrawableSpan(int verticalAlignment) {
            mVerticalAlignment = verticalAlignment;
        }

        public abstract Drawable getDrawable();

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = getCachedDrawable();
            Rect rect = d.getBounds();
            final int fontHeight = (int) (paint.getFontMetrics().descent - paint.getFontMetrics().ascent);
            if (fm != null) {
                if (rect.height() > fontHeight) {
                    if (mVerticalAlignment == ALIGN_TOP) {
                        fm.descent += rect.height() - fontHeight;
                    } else if (mVerticalAlignment == ALIGN_CENTER) {
                        fm.ascent -= (rect.height() - fontHeight) / 2;
                        fm.descent += (rect.height() - fontHeight) / 2;
                    } else if (mVerticalAlignment == ALIGN_BASELINE) {
                        fm.ascent -= rect.height() - fontHeight + fm.descent;
                    } else {
                        fm.ascent -= rect.height() - fontHeight;
                    }
                }
            }
            return rect.right;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            Drawable d = getCachedDrawable();
            Rect rect = d.getBounds();
            canvas.save();
            final float fontHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
            int transY = bottom - rect.bottom;
            if (rect.height() < fontHeight) {
                if (mVerticalAlignment == ALIGN_BASELINE) {
                    transY -= paint.getFontMetricsInt().descent;
                } else if (mVerticalAlignment == ALIGN_CENTER) {
                    transY -= (fontHeight - rect.height()) / 2;
                } else if (mVerticalAlignment == ALIGN_TOP) {
                    transY -= fontHeight - rect.height();
                }
            } else {
                if (mVerticalAlignment == ALIGN_BASELINE) {
                    transY -= paint.getFontMetricsInt().descent;
                }
            }
            canvas.translate(x, transY);
            d.draw(canvas);
            canvas.restore();
        }

        private Drawable getCachedDrawable() {
            WeakReference<Drawable> wr = mDrawableRef;
            Drawable d = null;
            if (wr != null)
                d = wr.get();
            if (d == null) {
                d = getDrawable();
                mDrawableRef = new WeakReference<>(d);
            }
            return getDrawable();
        }

    }

}
