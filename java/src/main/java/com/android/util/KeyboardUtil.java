package com.android.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by xuzhb on 2019/11/24
 * Desc:软键盘管理工具类
 */
public class KeyboardUtil {

    //弹出软键盘
    public static void showSoftInput(Context context, View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager manager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.showSoftInput(view, 0);
        }
    }

    //延迟弹出软键盘，默认延迟200ms
    public static void showSoftInputDelay(Context context, View view) {
        showSoftInputDelay(context, view, 200);
    }

    //延迟弹出软键盘
    public static void showSoftInputDelay(Context context, View view, long delay) {
        new Handler().postDelayed(() -> {
            showSoftInput(context, view);
        }, delay);
    }

    //收起软键盘
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //复制内容到剪切板
    public static void copyToClipboard(Context context, CharSequence text) {
        ClipboardManager cm = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        cm.setPrimaryClip(clipData);
    }

    //获取剪切板的文本
    public static CharSequence getClipboardText(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cm.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            return clipData.getItemAt(0).coerceToText(context);
        }
        return null;
    }

}
