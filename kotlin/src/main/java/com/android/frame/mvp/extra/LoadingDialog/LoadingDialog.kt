package com.android.frame.mvp.extra.LoadingDialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import com.android.basicproject.R

/**
 * Created by xuzhb on 2019/12/29
 * Desc:自定义加载框
 */
class LoadingDialog @JvmOverloads constructor(
    context: Context, themeStyle: Int = 0
) : Dialog(context, themeStyle) {

    private var mLoadingView: LoadingView
    private var mMessageTv: TextView

    init {
        setContentView(R.layout.dialog_loading_layout)
        mLoadingView = findViewById(R.id.loading_view)
        mMessageTv = findViewById(R.id.message_tv)
    }

    //显示加载框
    fun show(message: CharSequence, cancelable: Boolean) {
        val animation = RotateAnimation(
            0f, 359f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        mLoadingView.startAnimation(animation.apply {
            duration = 800
            repeatCount = Animation.INFINITE
            repeatMode = Animation.RESTART
            setInterpolator(LinearInterpolator())
        })
        with(mMessageTv) {
            if (TextUtils.isEmpty(message)) {
                visibility = View.GONE
            } else {
                text = message
            }
        }
        //点击对话框外的部分不消失
        setCanceledOnTouchOutside(false)
        //点击或按返回键时是否消失
        setCancelable(cancelable)
        //设置对话框居中
        with(window.attributes) {
            gravity = Gravity.CENTER
            dimAmount = 0.2f
        }
        show()
    }

}