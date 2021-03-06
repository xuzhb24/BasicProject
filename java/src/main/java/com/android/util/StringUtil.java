package com.android.util;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

/**
 * Created by xuzhb on 2020/2/8
 * Desc:字符串工具类
 */
public class StringUtil {

    //纯Java判断是否是空字符，相当于Android中的TextUtils.isEmpty
    public static boolean isEmpty(String s) {
        if (s != null) {
            for (int i = 0, len = s.length(); i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    //设置同一个字符串不同字体大小和颜色
    public static SpannableString createTextSpan(
            String content,
            int startIndex,
            int endIndex,
            int textColor,
            int textSize) {
        return createTextSpan(content, startIndex, endIndex, textColor, textSize, Typeface.NORMAL, false, null);
    }

    //设置同一个字符串不同字体大小和颜色
    public static SpannableString createTextSpan(
            String content,
            int startIndex,
            int endIndex,
            int textColor,
            int textSize,
            int textStyle,
            boolean showUnderLine,         //是否设置下划线
            View.OnClickListener listener  //设置点击事件
    ) {
        if (endIndex > content.length()) {
            endIndex = content.length();
        }
        SpannableString spannableString = new SpannableString(content);
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE：从起始下标到终止下标，包括起始下标，不包括终止下标
        //Spanned.SPAN_INCLUSIVE_INCLUSIVE：从起始下标到终止下标，同时包括起始下标和终止下标
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE：从起始下标到终止下标，但都不包括起始下标和终止下标
        //Spanned.SPAN_EXCLUSIVE_INCLUSIVE：从起始下标到终止下标，不包括起始下标，包括终止下标

        //设置不同字体颜色
        spannableString.setSpan(new ForegroundColorSpan(textColor), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置不同字体大小
        spannableString.setSpan(new AbsoluteSizeSpan(textSize), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置不同的textStyle
        spannableString.setSpan(new StyleSpan(textStyle), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //是否设置下划线
        if (showUnderLine) {
            spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        //设置点击事件
        if (listener != null) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    listener.onClick(widget);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(textColor);  //设置字体颜色
                    ds.setUnderlineText(showUnderLine);  //是否显示下划线
                }
            }, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    //反转字符串
    public static String reverse(String s) {
        if (isEmpty(s)) {
            return s;
        }
        int len = s.length();
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    //转化为半角字符
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    //转化为全角字符
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

}
