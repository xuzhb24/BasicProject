package com.android.frame.mvp.AATest.activity.newslist

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.android.basicproject.R
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.mvp.AATest.UrlConstantMvp
import com.android.frame.mvp.AATest.adapter.NewsListAdapter
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.BaseCompatActivity
import com.android.widget.RecyclerView.LoadMoreListener
import com.android.widget.RecyclerView.LoadMoreWrapper
import kotlinx.android.synthetic.main.activity_list_layout.*

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class NewsListActivity : BaseCompatActivity<NewsListView, NewsListPresenter>(), NewsListView {

    private var mList: MutableList<NewsListBeanMvp> = mutableListOf()
    //实现上拉加载更多
    private val mLoadMoreAdapter by lazy { LoadMoreWrapper(NewsListAdapter(this, mList)) }
    private lateinit var mRecyclerView: RecyclerView
    private var mCurrentPage: Int = 1  //记录当前页面

    override fun handleView(savedInstanceState: Bundle?) {
        title_bar.titleText = "新闻列表"
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.adapter = mLoadMoreAdapter
        mPresenter?.getNews(mCurrentPage, showLoading = true)
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        //上拉加载更多
        mRecyclerView.addOnScrollListener(object : LoadMoreListener() {
            override fun onLoadMore() {
                if (mLoadMoreAdapter.loadState != LoadMoreWrapper.STATE_LOADING) {
                    mLoadMoreAdapter.loadState = LoadMoreWrapper.STATE_LOADING
                    mPresenter?.getNews(mCurrentPage)
                }
            }
        })
        //设置加载异常监听，如网络异常导致无法加载数据
        mLoadMoreAdapter.setOnLoadFailListener {
            mLoadMoreAdapter.loadState = LoadMoreWrapper.STATE_LOADING
            mPresenter?.getNews(mCurrentPage)
        }
        (mLoadMoreAdapter.itemAdapter as NewsListAdapter).setOnItemClickListener { obj, position ->
            WangYiNewsWebviewActivity.start(this, "", (obj as NewsListBeanMvp).path)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_list_layout

    override fun getPresenter(): NewsListPresenter = NewsListPresenter(this)

    override fun refreshData() {
        mCurrentPage = 1
        mList.clear()
        mLoadMoreAdapter.loadState = LoadMoreWrapper.STATE_LOADING
        mPresenter?.getNews(mCurrentPage)
    }

    override fun showData(list: MutableList<NewsListBeanMvp>) {
        mCurrentPage++
        mList.addAll(list)
        if (list.size < UrlConstantMvp.ONCE_LOAD_SIZE.toInt()) {
            mLoadMoreAdapter.loadState = LoadMoreWrapper.STATE_LOAD_END
        } else {
            mLoadMoreAdapter.loadState = LoadMoreWrapper.STATE_DEFAULT
        }
    }

    override fun loadFail() {
        mLoadMoreAdapter.loadState = LoadMoreWrapper.STATE_LOAD_FAIL
    }
}