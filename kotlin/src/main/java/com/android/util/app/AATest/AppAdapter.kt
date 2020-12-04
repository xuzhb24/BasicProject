package com.android.util.app.AATest

import android.content.Context
import com.android.basicproject.R
import com.android.util.app.AppInfo
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.ViewHolder

/**
 * Created by xuzhb on 2020/12/3
 * Desc:
 */
class AppAdapter(context: Context, list: MutableList<AppInfo>) : BaseAdapter<AppInfo>(context, list, R.layout.item_image_with_text) {
    override fun bindData(holder: ViewHolder, data: AppInfo, position: Int) {
        holder.setImageDrawable(R.id.item_iv, data.icon!!)
            .setText(R.id.item_tv, data.label)
    }
}