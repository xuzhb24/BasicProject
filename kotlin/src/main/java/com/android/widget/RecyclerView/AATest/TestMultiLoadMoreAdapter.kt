package com.android.widget.RecyclerView.AATest

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.widget.RecyclerView.AATest.entity.DateBean
import com.android.widget.RecyclerView.LoadMoreAdapter
import com.android.widget.RecyclerView.MultiViewType
import com.android.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson

/**
 * Created by xuzhb on 2020/1/13
 * Desc:多布局上拉加载更多
 */
class TestMultiLoadMoreAdapter(context: Context, list: MutableList<String>) :
    LoadMoreAdapter<String>(context, list, object : MultiViewType<String> {
        override fun getLayoutId(item: String, position: Int, totalCount: Int): Int {
            return when {
                list[position].contains("date") -> R.layout.item_month
                else -> R.layout.item_test_load_more
            }
        }
    }) {
    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        when {
            data.contains("date") -> {
                val bean: DateBean = Gson().fromJson(data, DateBean::class.java)
                holder.setText(R.id.month_tv, bean.date)
                    .setTextColor(R.id.month_tv, Color.parseColor("#db4b3c"))
            }
            else -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
                val bean: NewsListBean.ResultBean = Gson().fromJson(data, NewsListBean.ResultBean::class.java)
                val imageIv: ImageView = holder.getView(R.id.image_iv)!!
                Glide.with(BaseApplication.instance).load(bean.image).into(imageIv)
                holder.setText(R.id.title_tv, bean.title)
                    .setText(R.id.time_tv, bean.passtime)
                    .setOnItemClickListener {
                        mOnGetNewsListener?.invoke(bean, position)
                    }
            }
        }
    }

    //item点击事件
    private var mOnGetNewsListener: ((obj: Any?, position: Int) -> Unit)? = null

    fun setOnGetNewsListener(listener: (obj: Any?, position: Int) -> Unit) {
        this.mOnGetNewsListener = listener
    }

}