package com.android.frame.mvp.AATest.activity.newslist

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.mvp.AATest.adapter.NewsListAdapter
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.BaseListActivity
import com.android.widget.RecyclerView.LoadMoreAdapter

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class NewsListActivity : BaseListActivity<NewsListBeanMvp, NewsListView, NewsListPresenter>(),
    NewsListView {
    override fun getAdapter(): LoadMoreAdapter<NewsListBeanMvp> =
        NewsListAdapter(this, mutableListOf())

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        mAdapter.setOnItemClickListener { obj, _ ->
            WangYiNewsWebviewActivity.start(this, "", (obj as NewsListBeanMvp).path)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_list_layout

    override fun getPresenter(): NewsListPresenter = NewsListPresenter()

}