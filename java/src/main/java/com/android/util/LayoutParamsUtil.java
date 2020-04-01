package com.android.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xuzhb on 2020/3/29
 * Desc:布局参数工具类
 */
public class LayoutParamsUtil {

    private static final int NOT_SET = -Integer.MAX_VALUE;

    //设置View的宽度
    public static void setWidth(View view, int width) {
        setLayoutParams(view, width, NOT_SET);
    }

    //设置View的高度
    public static void setHeight(View view, int height) {
        setLayoutParams(view, NOT_SET, height);
    }

    //设置View的宽高
    public static void setLayoutParams(View view, int width, int height) {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        if (width != NOT_SET) {
            params.width = width;
        }
        if (height != NOT_SET) {
            params.height = height;
        }
        view.requestLayout();
    }

    //设置View的leftMargin
    public static void setMarginLeft(View view, int leftMargin) {
        setMargin(view, leftMargin, NOT_SET, NOT_SET, NOT_SET);
    }

    //设置View的topMargin
    public static void setMarginTop(View view, int topMargin) {
        setMargin(view, NOT_SET, topMargin, NOT_SET, NOT_SET);
    }

    //设置View的rightMargin
    public static void setMarginRight(View view, int rightMargin) {
        setMargin(view, NOT_SET, NOT_SET, rightMargin, NOT_SET);
    }

    //设置View的bottomMargin
    public static void setMarginBottom(View view, int bottomMargin) {
        setMargin(view, NOT_SET, NOT_SET, NOT_SET, bottomMargin);
    }

    //设置View的Margin
    public static void setMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        if (leftMargin != NOT_SET) {
            params.leftMargin = leftMargin;
        }
        if (topMargin != NOT_SET) {
            params.topMargin = topMargin;
        }
        if (rightMargin != NOT_SET) {
            params.rightMargin = rightMargin;
        }
        if (bottomMargin != NOT_SET) {
            params.bottomMargin = bottomMargin;
        }
        view.requestLayout();
    }

    //增加View的leftMargin
    public static void addMarginLeft(View view, int leftMargin) {
        addMargin(view, leftMargin, 0, 0, 0);
    }

    //增加View的topMargin
    public static void addMarginTop(View view, int topMargin) {
        addMargin(view, 0, topMargin, 0, 0);
    }

    //增加View的rightMargin
    public static void addMarginRight(View view, int rightMargin) {
        addMargin(view, 0, 0, rightMargin, 0);
    }

    //增加View的bottomMargin
    public static void addMarginBottom(View view, int bottomMargin) {
        addMargin(view, 0, 0, 0, bottomMargin);
    }

    //增加View的Margin，传负数表示减少View的Margin
    public static void addMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.leftMargin += leftMargin;
        params.topMargin += topMargin;
        params.rightMargin += rightMargin;
        params.bottomMargin += bottomMargin;
        view.requestLayout();
    }

    public static ViewGroup.MarginLayoutParams getMarginLayoutParams(View view) {
        return (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    }

    //设置View的paddingLeft
    public static void setPaddingLeft(View view, int left) {
        setPadding(view, left, NOT_SET, NOT_SET, NOT_SET);
    }

    //设置View的paddingTop
    public static void setPaddingTop(View view, int top) {
        setPadding(view, NOT_SET, top, NOT_SET, NOT_SET);
    }

    //设置View的paddingRight
    public static void setPaddingRight(View view, int right) {
        setPadding(view, NOT_SET, NOT_SET, right, NOT_SET);
    }

    //设置View的paddingBottom
    public static void setPaddingBottom(View view, int bottom) {
        setPadding(view, NOT_SET, NOT_SET, NOT_SET, bottom);
    }

    //设置View的Padding
    public static void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(left != NOT_SET ? left : view.getPaddingLeft(), top != NOT_SET ? top : view.getPaddingTop(),
                right != NOT_SET ? right : view.getPaddingRight(), bottom != NOT_SET ? bottom : view.getPaddingBottom());
    }

    //增加View的PaddingLeft
    public static void addPaddingLeft(View view, int left) {
        addPadding(view, left, 0, 0, 0);
    }

    //增加View的PaddingTop
    public static void addPaddingTop(View view, int top) {
        addPadding(view, 0, top, 0, 0);
    }

    //增加View的PaddingRight
    public static void addPaddingRight(View view, int right) {
        addPadding(view, 0, 0, right, 0);
    }

    //增加View的PaddingBottom
    public static void addPaddingBottom(View view, int bottom) {
        addPadding(view, 0, 0, 0, bottom);
    }

    //增加View的Padding，传负数表示减少View的Padding
    public static void addPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(view.getPaddingLeft() + left, view.getPaddingTop() + top,
                view.getPaddingRight() + right, view.getPaddingBottom() + bottom);
    }

}
