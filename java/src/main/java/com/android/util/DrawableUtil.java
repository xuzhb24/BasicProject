package com.android.util;

import android.graphics.drawable.GradientDrawable;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:代码创建Drawable
 */
public class DrawableUtil {

    public static GradientDrawable createSolidShape(float corner, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        return drawable;
    }

    public static GradientDrawable createStrokeShape(float corner, int strokeWidth, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setStroke(strokeWidth, strokeColor);
        return drawable;
    }

    public static GradientDrawable createStrokeShape(
            float corner, int strokeWidth, int strokeColor, float dashWidth, float dashGap
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
        return drawable;
    }

    public static GradientDrawable createSolidStrokeShape(
            float corner, int color, int strokeWidth, int strokeColor
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        drawable.setStroke(strokeWidth, strokeColor);
        return drawable;
    }

    public static GradientDrawable createSolidStrokeShape(
            float corner, int color, int strokeWidth, int strokeColor, float dashWidth, float dashGap
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
        return drawable;
    }

}
