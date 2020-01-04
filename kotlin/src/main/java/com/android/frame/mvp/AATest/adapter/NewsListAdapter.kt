package com.android.frame.mvp.AATest.adapter

import android.content.Context
import android.widget.ImageView
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class NewsListAdapter(context: Context, list: MutableList<NewsListBeanMvp>) :
    BaseAdapter<NewsListBeanMvp>(context, list, R.layout.item_test_load_more) {
    override fun bindData(holder: ViewHolder, data: NewsListBeanMvp, position: Int) {
        val imageIv: ImageView = holder.getView(R.id.image_iv)!!
        Glide.with(BaseApplication.instance).load(data.image).into(imageIv)
        holder.setText(R.id.title_tv, data.title).setText(R.id.time_tv, data.passtime)
    }
}