package com.android.widget.RecyclerView.AATest

import android.content.Context
import android.widget.CheckBox
import com.android.basicproject.R
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.MultiViewType
import com.android.widget.RecyclerView.ViewHolder

/**
 * Created by xuzhb on 2019/10/2
 * Desc:
 */
class TestMultiAdapter(context: Context, list: MutableList<String>, private var mSelectedList: MutableList<String>) :
    BaseAdapter<String>(context, list, object : MultiViewType<String> {
        override fun getLayoutId(item: String, position: Int, totalCount: Int): Int {
            return when (position) {
                0 -> R.layout.item_header
                else -> R.layout.item_text_with_checkbox
            }
        }
    }) {
    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        when (position) {
            0 -> holder.setText(R.id.count_tv, "已搜索到${data}条数据")
            else -> {
                holder.setText(R.id.value_tv, data)
                val cb = holder.getView<CheckBox>(R.id.check_cb)!!
                cb.isChecked = false
                if (mSelectedList.size != 0) {  //初始化选择项
                    mSelectedList.forEach {
                        if (data.equals(it)) {
                            cb.isChecked = true
                        }
                    }
                }
                holder.setOnItemClickListener {
                    mListener?.invoke(cb, cb.isChecked, data, position)
                }
                if (position == (itemCount - 1)) {
                    holder.setViewGone(R.id.divider_line)  //隐藏最后一条分界线
                } else {
                    holder.setViewVisible(R.id.divider_line)
                }
            }
        }
    }

    //刷新数据，带默认选项
    fun setData(list: MutableList<String>, selectedList: MutableList<String>) {
        mSelectedList = selectedList
        setData(list)
    }

    private var mListener: ((cb: CheckBox, isChecked: Boolean, data: String, position: Int) -> Unit)? = null
    fun setOnCheckedChangeListener(listener: (cb: CheckBox, isChecked: Boolean, data: String, position: Int) -> Unit) {
        this.mListener = listener
    }

}