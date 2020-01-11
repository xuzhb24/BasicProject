package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import com.android.basicproject.R
import com.android.frame.http.AATest.ApiService
import com.android.frame.http.AATest.UrlConstant
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.mvc.BaseActivity
import com.android.widget.RecyclerView.LoadMoreWrapper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_test_adapter.*

/**
 * Created by xuzhb on 2019/10/30
 * Desc:
 */
class TestLoadMoreAdapterActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val ONCE_LOAD_SIEE = 10
    }

    private var mList: MutableList<NewsListBean.ResultBean> = mutableListOf()
    private val mAdapter by lazy { TestLoadMoreAdapter(this, mList) }
    private val mMoreAdapter by lazy { LoadMoreWrapper(mAdapter) }  //实现上拉加载更多
    private var mCurrentPage: Int = 1  //记录当前页面

    override fun handleView(savedInstanceState: Bundle?) {
//        srl.isEnabled = false  //禁用下拉刷新功能
        srl.setOnRefreshListener(this)  //下拉监听
        srl.setColorSchemeColors(resources.getColor(R.color.colorPrimaryDark))  //设置颜色
//        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = mMoreAdapter
        mMoreAdapter.isEmptyViewLoadMoreEnable = true
        showToast("下拉或上拉加载数据")
//        queryData(mCurrentPage)
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        //底部上拉加载更多
        mMoreAdapter.setOnLoadMoreListener(rv) {
            queryData(mCurrentPage)
        }
        //设置加载异常监听，如网络异常导致无法加载数据
        mMoreAdapter.setOnLoadFailListener {
            mMoreAdapter.startLoading()
            queryData(mCurrentPage)
        }
        //设置点击事件
        (mMoreAdapter.itemAdapter as TestLoadMoreAdapter).setOnItemClickListener { obj, position ->
            WangYiNewsWebviewActivity.start(this, "", (obj as NewsListBean.ResultBean).path)
        }
    }

    //加载数据
    private fun queryData(page: Int) {
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByBody(page, ONCE_LOAD_SIEE)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(object : Observer<NewsListBean> {
                override fun onComplete() {
                    endRefresh()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(bean: NewsListBean) {
                    if (bean.isSuccess()) {
                        showData(bean.result ?: mutableListOf())
                    } else {
                        showToast(bean.message)
                    }
                }

                override fun onError(e: Throwable) {
                    loadFail()
                    endRefresh()
                    showToast(ExceptionUtil.convertExceptopn(e))
                    e.printStackTrace()
                }
            })
    }

    //展示数据
    private fun showData(list: MutableList<NewsListBean.ResultBean>) {
        mCurrentPage++
        mList.addAll(list)
        if (list.size < ONCE_LOAD_SIEE) {
            mMoreAdapter.loadMoreEnd()
        } else {
            mMoreAdapter.loadMoreComplete()
        }
    }

    //加载异常
    private fun loadFail() {
        mMoreAdapter.loadMoreFail()
    }

    override fun getLayoutId(): Int = R.layout.activity_test_adapter

    //下拉刷新
    override fun onRefresh() {
        mCurrentPage = 1
        mList.clear()
        mMoreAdapter.notifyDataSetChanged()
        queryData(mCurrentPage)
    }

    //停止刷新，即收起刷新头部
    private fun endRefresh() {
        if (srl.isRefreshing) {
            srl.isRefreshing = false
        }
    }

}