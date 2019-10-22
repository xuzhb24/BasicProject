package com.android.widget.dialog

import android.support.annotation.LayoutRes
import com.android.widget.ViewHolder

/**
 * Created by xuzhb on 2019/10/22
 * Desc:通用的Dialog，可以实现不同布局的Dialog
 */
class CommonDialog : BaseDialog() {

    companion object {
        fun newInstance(): CommonDialog = CommonDialog()
    }

    private var mListener: ((holder: ViewHolder, dialog: BaseDialog) -> Unit)? = null

    //设置dialog的布局
    fun setLayoutId(@LayoutRes layoutId: Int): CommonDialog {
        this.mLayoutId = layoutId
        return this
    }

    //设置事件监听
    fun setOnViewListener(listener: (holder: ViewHolder, dialog: BaseDialog) -> Unit): CommonDialog {
        this.mListener = listener
        return this
    }

    override fun getLayoutId(): Int = mLayoutId

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        mListener?.invoke(holder, dialog)
    }

}