package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import android.text.TextUtils
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestAdapterBinding
import com.android.frame.http.AATest.ApiService
import com.android.frame.http.AATest.UrlConstant
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.mvc.BaseActivity
import com.android.util.DateUtil
import com.android.widget.RecyclerView.AATest.entity.DateBean
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by xuzhb on 2020/1/14
 * Desc:
 */
class TestMultiLoadMoreAdapterActivity : BaseActivity<ActivityTestAdapterBinding>(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val ONCE_LOAD_SIEE = 10
    }

    private val mAdapter by lazy { TestMultiLoadMoreAdapter(this, mutableListOf()) }

    private var mCurrentPage: Int = 1  //记录当前页面
    private var mCurrentDate = DateUtil.getDistanceDateByDay(1, DateUtil.Y_M_D)

    override fun handleView(savedInstanceState: Bundle?) {
        binding.srl.setOnRefreshListener(this)  //下拉监听
        binding.srl.setColorSchemeColors(resources.getColor(R.color.colorPrimaryDark))  //设置颜色
        binding.rv.adapter = mAdapter
        queryData(mCurrentPage)
    }

    override fun initListener() {
        //底部上拉加载更多
        mAdapter.setOnLoadMoreListener(binding.rv) {
            queryData(mCurrentPage)
        }
        //设置加载异常监听，如网络异常导致无法加载数据
        mAdapter.setOnLoadFailListener {
            queryData(mCurrentPage)
        }
        //获取新闻信息
        mAdapter.setOnGetNewsListener { obj, position ->
            WangYiNewsWebviewActivity.start(this, "", (obj as NewsListBean.ResultBean).path)
        }
    }

    override fun getViewBinding() = ActivityTestAdapterBinding.inflate(layoutInflater)

    override fun onRefresh() {
        mCurrentPage = 1
        mCurrentDate = DateUtil.getDistanceDateByDay(1, DateUtil.Y_M_D)
        mAdapter.setData(mutableListOf())
        mAdapter.notifyDataSetChanged()
        queryData(mCurrentPage)
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
                        if (bean.result != null && bean.result.size > 0) {
                            val list: MutableList<String> = mutableListOf()
                            for (i in bean.result.indices) {
                                val date = bean.result.get(i).passtime.split(" ")[0].trim()
                                if (!TextUtils.equals(mCurrentDate, date)) {
                                    list.add(i, Gson().toJson(DateBean(date)))
                                    mCurrentDate = date
                                } else {
                                    list.add(Gson().toJson(bean.result.get(i)))
                                }
                            }
                            showData(list)
                        } else {
                            showData(mutableListOf())
                        }
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
    private fun showData(list: MutableList<String>) {
        mCurrentPage++
        mAdapter.addData(list)
        if (list.size < ONCE_LOAD_SIEE) {
            mAdapter.loadMoreEnd()
        } else {
            mAdapter.loadMoreComplete()
        }
    }

    //加载异常
    private fun loadFail() {
        mAdapter.loadMoreFail()
    }

    //停止刷新，即收起刷新头部
    private fun endRefresh() {
        if (binding.srl.isRefreshing) {
            binding.srl.isRefreshing = false
        }
    }

}