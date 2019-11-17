package com.android.widget.RecyclerView.AATest

import android.content.Context
import android.graphics.Color
import com.android.basicproject.R
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.ViewHolder

/**
 * Created by xuzhb on 2019/10/2
 * Desc:单一布局
 */
class TestSingleAdapter(context: Context, list: MutableList<String>, private var mSelectedItem: String = "") :
    BaseAdapter<String>(context, list, R.layout.item_text_with_image) {
    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        holder.setText(R.id.value_tv, data)
        if (data.equals(mSelectedItem)) {
            holder.setViewVisible(R.id.selected_iv)
                .setTextColor(R.id.value_tv, Color.parseColor("#0071FF"))
        } else {
            holder.setViewGone(R.id.selected_iv)
                .setTextColor(R.id.value_tv, Color.BLACK)
        }
        if (position == (itemCount - 1)) {
            holder.setViewGone(R.id.divider_line)
        } else {
            holder.setViewVisible(R.id.divider_line)
        }
    }

    //刷新数据，带默认选项
    fun setData(list: MutableList<String>, selectedItem: String) {
        mSelectedItem = selectedItem
        setData(list)
    }

}