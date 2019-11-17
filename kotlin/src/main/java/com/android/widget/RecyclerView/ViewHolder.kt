package com.android.widget.RecyclerView

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by xuzhb on 2019/7/30
 * Desc:通用的ViewHolder(RecyelerView)
 */
@Suppress("UNCHECKED_CAST")
open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //缓存View
    private var mViewList: SparseArray<View>

    init {
        mViewList = SparseArray()
    }

    //查找View中的控件
    fun <T : View> getView(viewId: Int): T? {
        //对已有的view做缓存
        var view: View? = mViewList.get(viewId)
        //使用缓存的方式减少findViewById的次数
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViewList.put(viewId, view)
        }
        return view as? T
    }

    //设置文本
    fun setText(viewId: Int, text: CharSequence): ViewHolder {
        val view = getView<TextView>(viewId)
        view?.text = text
        return this //链式调用
    }

    //设置文本字体颜色
    fun setTextColor(viewId: Int, color: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view?.setTextColor(color)
        return this
    }

    //设置文本字体大小，单位默认为SP，故设置时只需要传递数值就可以，如setTextSize(R.id.xxx,15f)
    fun setTextSize(viewId: Int, textSize: Float): ViewHolder {
        val view = getView<TextView>(viewId)
        view?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        return this
    }

    //设置图片
    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        val iv = getView<ImageView>(viewId)
        iv?.setImageResource(resId)
        return this
    }

    //显示View
    fun setViewVisible(viewId: Int): ViewHolder {
        getView<View>(viewId)?.visibility = View.VISIBLE
        return this
    }

    //隐藏View
    fun setViewGone(viewId: Int): ViewHolder {
        getView<View>(viewId)?.visibility = View.GONE
        return this
    }

    //设置View宽度
    fun setViewWidth(viewId: Int, width: Int): ViewHolder {
        return setViewParams(viewId, width, -1)
    }

    //设置View高度
    fun setViewHeight(viewId: Int, height: Int): ViewHolder {
        return setViewParams(viewId, -1, height)
    }

    //设置View的宽度和高度
    fun setViewParams(viewId: Int, width: Int, height: Int): ViewHolder {
        getView<View>(viewId)?.let {
            val params = it.layoutParams as ViewGroup.MarginLayoutParams
            if (width >= 0) {
                params.width = width
            }
            if (height >= 0) {
                params.height = height
            }
            it.layoutParams = params
        }
        return this
    }

    //设置控件的点击事件，通过ViewHolder调用
    fun setOnItemChildClickListener(viewId: Int, listener: (v: View) -> Unit) {
        getView<View>(viewId)?.setOnClickListener { v -> listener.invoke(v) }
    }

    //设置控件的长按事件，通过ViewHolder调用
    fun setOnItemChildLongClickListener(viewId: Int, listener: (v: View) -> Boolean) {
        getView<View>(viewId)?.setOnLongClickListener { v -> listener.invoke(v) }
    }

    //设置条目点击事件，通过ViewHolder调用
    fun setOnItemClickListener(listener: (v: View) -> Unit) {
        itemView.setOnClickListener { v -> listener.invoke(v) }
    }

    //设置条目长按事件，通过ViewHolder调用
    fun setOnItemLongClickListener(listener: (v: View) -> Boolean) {
        itemView.setOnLongClickListener { v -> listener.invoke(v) }
    }

}