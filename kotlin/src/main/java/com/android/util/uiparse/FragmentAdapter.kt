package com.android.util.uiparse

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/9/22
 */
class FragmentAdapter(
    private val list: MutableList<UIStructure.FragmentStructure>,
    private val mContext: Context = BaseApplication.instance
) : RecyclerView.Adapter<FragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext.applicationContext).inflate(R.layout.item_ui_parse, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val structure = list[position]
        holder.mNameTv.text = structure.name
        holder.mVisibleTv.text = structure.visible.toString()
        if (structure.visible) {
            holder.mVisibleTv.setTextColor(Color.parseColor("#db4b3c"))
        } else {
            holder.mVisibleTv.setTextColor(Color.BLACK)
        }
        if (structure.isChildFragment()) {
            holder.mRootLl.setBackgroundColor(Color.parseColor("#f3f3f3"))
            holder.mNameTv.setPadding(SizeUtil.dp2px(10f).toInt(), 0, 0, 0)
        } else {
            holder.mRootLl.setBackgroundColor(Color.WHITE)
            holder.mNameTv.setPadding(0, 0, 0, 0)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mRootLl: LinearLayout = itemView.findViewById(R.id.root_ll)
        var mNameTv: TextView = itemView.findViewById(R.id.name_tv)
        var mVisibleTv: TextView = itemView.findViewById(R.id.visible_tv)
    }

}