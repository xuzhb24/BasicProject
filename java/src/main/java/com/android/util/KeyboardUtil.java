package com.android.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

    //收起软键盘
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //复制内容到剪切板
    public static void copyToClipboard(Context context, String data) {
        ClipboardManager cm = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, data);
        cm.setPrimaryClip(clipData);
    }

}
