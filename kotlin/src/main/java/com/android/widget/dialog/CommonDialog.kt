package com.android.widget.dialog

import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.android.widget.ViewHolder

/**
 * Created by xuzhb on 2019/10/22
 * Desc:通用的Dialog，可以实现不同布局的Dialog
 */
class CommonDialog : BaseDialog() {

    companion object {
        fun newInstance(): CommonDialog = CommonDialog()
    }

    private var mListener: ((holder: ViewHolder, dialog: DialogFragment?) -> Unit)? = null

    //设置dialog的布局
    fun setLayoutId(@LayoutRes layoutId: Int): CommonDialog {
        this.mLayoutId = layoutId
        return this
    }

    //设置事件监听
    fun setOnViewListener(listener: (holder: ViewHolder, dialog: DialogFragment?) -> Unit): CommonDialog {
        this.mListener = listener
        return this
    }

    override fun getLayoutId(): Int = mLayoutId

    override fun convertView(holder: ViewHolder, dialog: DialogFragment?) {
        mListener?.invoke(holder, dialog)
    }

}