package com.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;

/**
 * Created by xuzhb on 2020/7/9
 * Desc:AlertDialog工具类
 */
public class AlertDialogUtil {

    public static void showDialog(Context context, CharSequence message) {
        showDialog(context, null, message, true, null, null,
                null, null);
    }

    public static void showDialog(Context context, CharSequence title, CharSequence message, boolean cancelable,
                                  DialogInterface.OnClickListener positiveClickListener,
                                  DialogInterface.OnClickListener negativeClickListener) {
        showDialog(context, title, message, cancelable, "确定", "取消",
                positiveClickListener, negativeClickListener);
    }

    public static void showDialog(Context context, CharSequence title, CharSequence message, boolean cancelable,
                                  CharSequence positiveText, CharSequence negativeText,
                                  DialogInterface.OnClickListener positiveClickListener,
                                  DialogInterface.OnClickListener negativeClickListener) {
        showDialog(context, title, message, cancelable, positiveText, negativeText, Color.BLACK,
                Color.BLACK, positiveClickListener, negativeClickListener);
    }

    public static void showDialog(Context context, CharSequence title, CharSequence message, boolean cancelable,
                                  CharSequence positiveText, CharSequence negativeText,
                                  int positiveTextColor, int negativeTextColor,
                                  DialogInterface.OnClickListener positiveClickListener,
                                  DialogInterface.OnClickListener negativeClickListener) {
        createDialog(context, title, message, cancelable,
                positiveText, negativeText,
                positiveTextColor, negativeTextColor,
                positiveClickListener, negativeClickListener).show();
    }

    /**
     * 创建AlertDialog
     *
     * @param title                 标题文本
     * @param message               内容文本
     * @param positiveText          确定按钮文本
     * @param negativeText          取消按钮文本
     * @param positiveTextColor     确定按钮文本颜色
     * @param negativeTextColor     取消按钮文本颜色
     * @param positiveClickListener 确定按钮点击事件回调
     * @param negativeClickListener 取消按钮点击事件回调
     */
    public static AlertDialog createDialog(Context context, CharSequence title, CharSequence message, boolean cancelable,
                                           CharSequence positiveText, CharSequence negativeText,
                                           int positiveTextColor, int negativeTextColor,
                                           DialogInterface.OnClickListener positiveClickListener,
                                           DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message).setCancelable(cancelable);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(positiveText)) {
            builder.setPositiveButton(positiveText, positiveClickListener);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder.setNegativeButton(negativeText, negativeClickListener);
        }
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            Button positiveButton = ((AlertDialog) dialog1)
                    .getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setBackground(null);
            positiveButton.setTextColor(positiveTextColor);
            Button negativeButton = ((AlertDialog) dialog1)
                    .getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setBackground(null);
            negativeButton.setTextColor(negativeTextColor);
        });
        return dialog;
    }

    public static void showItemsDialog(Context context, DialogInterface.OnClickListener listener, CharSequence... items) {
        new AlertDialog.Builder(context)
                .setItems(items, listener)
                .create().show();
    }

}
