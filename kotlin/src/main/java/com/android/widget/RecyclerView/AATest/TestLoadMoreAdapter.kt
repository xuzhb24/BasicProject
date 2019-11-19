package com.android.widget.RecyclerView.AATest

import android.content.Context
import android.widget.ImageView
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.widget.RecyclerView.BaseAdapter
import com.android.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

/**
 * Created by xuzhb on 2019/10/29
 * Desc:上拉加载更多
 */
class TestLoadMoreAdapter(context: Context, list: MutableList<NewsListBean>) :
    BaseAdapter<NewsListBean>(context, list, R.layout.item_test_load_more) {
    override fun bindData(holder: ViewHolder, data: NewsListBean, position: Int) {
        val imageIv: ImageView = holder.getView(R.id.image_iv)!!
        Glide.with(BaseApplication.instance).load(data.image).into(imageIv)
        holder.setText(R.id.title_tv, data.title).setText(R.id.time_tv, data.passtime)
    }
}