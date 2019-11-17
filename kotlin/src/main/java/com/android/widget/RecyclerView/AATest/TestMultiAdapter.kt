package com.android.widget.RecyclerView.AATest

import android.content.Context
import com.android.basicproject.R
import com.android.util.ToastUtil
import com.android.util.isEntity
import com.android.widget.RecyclerView.AATest.entity.DetailBean
import com.android.widget.RecyclerView.AATest.entity.MonthBean
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.MultiViewType
import com.android.widget.RecyclerView.ViewHolder
import com.google.gson.Gson

/**
 * Created by xuzhb on 2019/10/2
 * Desc:多布局
 */
class TestMultiAdapter(context: Context, list: MutableList<String>) :
    BaseAdapter<String>(context, list, object : MultiViewType<String> {
        override fun getLayoutId(item: String, position: Int, totalCount: Int): Int {
            return when {
//                isEntity(list[position], MonthBean::class) -> R.layout.item_month
                list[position].contains("month") -> R.layout.item_month
                else -> R.layout.item_detail
            }
        }
    }) {
    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        when {
            data.contains("month") -> {
                val bean: MonthBean = Gson().fromJson(data, MonthBean::class.java)
                holder.setText(R.id.month_tv, bean.month).setText(R.id.count_tv, "总计${bean.count}个")
            }
            else -> {
                val bean: DetailBean = Gson().fromJson(data, DetailBean::class.java)
                holder.setText(R.id.detail_tv, bean.detail).setText(R.id.time_tv, bean.time)
                holder.setOnItemClickListener {
                    ToastUtil.showToast(bean.detail)
                }
            }
        }
    }

}