package com.android.widget

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.android.basicproject.R

/**
 * Created by xuzhb on 2020/7/17
 * Desc:加载框
 */
class LoadingDialog @JvmOverloads constructor(
    context: Context,
    private val mMessage: String? = null,
    private val mCancelable: Boolean = true
) : Dialog(context, R.style.LoadingDialogStyle) {

    init {
        setContentView(R.layout.dialog_loading)
        with(findViewById<TextView>(R.id.loading_tv)) {
            if (TextUtils.isEmpty(mMessage)) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                text = mMessage
            }
        }
        //点击对话框外的部分不消失
        setCanceledOnTouchOutside(mCancelable)
        //点击或按返回键时是否消失
        setCancelable(mCancelable)
    }

}