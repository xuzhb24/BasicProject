package com.android.widget.PopupWindow.AATest

import android.content.Context
import com.android.basicproject.R
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.ViewHolder

/**
 * Created by xuzhb on 2019/9/1
 * Desc:
 */
class PopupWindowAdapter(context: Context, list: MutableList<String>) :
    BaseAdapter<String>(context, list, R.layout.item_popup_window) {

    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        holder.setText(R.id.item_tv, data)
        if (position == itemCount - 1) {
            holder.setViewGone(R.id.divider_line)
        } else {
            holder.setViewVisible(R.id.divider_line)
        }
    }

}