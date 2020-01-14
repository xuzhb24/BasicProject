package com.android.widget.RecyclerView.AATest

import android.content.Context
import android.widget.ImageView
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.widget.RecyclerView.LoadMoreAdapter
import com.android.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

/**
 * Created by xuzhb on 2020/1/13
 * Desc:单布局上拉加载更多
 */
class TestSingleLoadMoreAdapter(context: Context, list: MutableList<NewsListBean.ResultBean>) :
    LoadMoreAdapter<NewsListBean.ResultBean>(context, list, R.layout.item_test_load_more) {
    override fun bindData(holder: ViewHolder, data: NewsListBean.ResultBean, position: Int) {
        val imageIv: ImageView = holder.getView(R.id.image_iv)!!
        Glide.with(BaseApplication.instance).load(data.image).into(imageIv)
        holder.setText(R.id.title_tv, data.title).setText(R.id.time_tv, data.passtime)
    }
}