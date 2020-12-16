package com.android.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.TextUtils

/**
 * Created by xuzhb on 2020/7/28
 * Desc:AlertDialog工具类
 */
object AlertDialogUtil {

    fun showDialog(
        context: Context,
        title: CharSequence,
        message: CharSequence,
        cancelable: Boolean = true,
        positiveText: CharSequence = "确定",
        negativeText: CharSequence = "取消",
        positiveTextColor: Int = Color.BLACK,
        negativeTextColor: Int = Color.BLACK,
        positiveClickListener: DialogInterface.OnClickListener? = null,
        negativeClickListener: DialogInterface.OnClickListener? = null
    ) {
        createDialog(
            context, title, message, cancelable,
            positiveText, negativeText,
            positiveTextColor, negativeTextColor,
            positiveClickListener, negativeClickListener
        ).show()
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
    fun createDialog(
        context: Context,
        title: CharSequence,
        message: CharSequence,
        cancelable: Boolean,
        positiveText: CharSequence,
        negativeText: CharSequence,
        positiveTextColor: Int,
        negativeTextColor: Int,
        positiveClickListener: DialogInterface.OnClickListener?,
        negativeClickListener: DialogInterface.OnClickListener?
    ): AlertDialog {
        val builder = AlertDialog.Builder(context).setMessage(message).setCancelable(cancelable)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        if (!TextUtils.isEmpty(positiveText)) {
            builder.setPositiveButton(positiveText, positiveClickListener)
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder.setNegativeButton(negativeText, negativeClickListener)
        }
        val dialog = builder.create()
        dialog.setOnShowListener { dialog1: DialogInterface ->
            val positiveButton = (dialog1 as AlertDialog)
                .getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.background = null
            positiveButton.setTextColor(positiveTextColor)
            val negativeButton = dialog1
                .getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.background = null
            negativeButton.setTextColor(negativeTextColor)
        }
        return dialog
    }

}