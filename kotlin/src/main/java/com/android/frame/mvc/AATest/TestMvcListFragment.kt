package com.android.frame.mvc.AATest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.R
import com.android.basicproject.databinding.FragmentTestMvcListBinding
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.AATest.adapter.NewsListAdapter
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvc.AATest.server.ApiHelper
import com.android.frame.mvc.BaseListFragment
import com.android.frame.mvc.extra.http.CustomObserver

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
class TestMvcListFragment : BaseListFragment<NewsListBean, FragmentTestMvcListBinding>() {

    companion object {
        fun newInstance() = TestMvcListFragment()
    }

    override fun getAdapter() = NewsListAdapter()

    //第一种写法
//    override fun loadDataFromServer(page: Int): Observable<BaseListResponse<NewsListBean>>? {
//        return ApiHelper.getWangYiNewsByField("$page", "${getLoadSize()}")
//    }

    //第二种写法
    //如果只是从服务器获取并展示列表数据，那么采用第一种写法重写loadDataFromServer返回相应的接口即可
    //但如果有需要处理其他逻辑，那么就需要重写loadData，并且在获取到数据后调用showData展示列表数据
    override fun loadData(page: Int, showLoadLayout: Boolean, showLoadingDialog: Boolean) {
        ApiHelper.getWangYiNewsByField("$page", "${getLoadSize()}")
            .subscribe(object : CustomObserver<BaseListResponse<NewsListBean>>(
                this, false, page == 1  //第一次加载时才显示加载框
            ) {
                override fun onSuccess(response: BaseListResponse<NewsListBean>) {
                    if (page < 3) {
                        showData(page, response.data)
                    } else {  //模拟数据加载到底
                        showData(page, mutableListOf())
                    }
                }
            })
    }

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            showToast("点击了第${position}项")
        }
        //Item内子View的点击事件
        mAdapter.addChildClickViewIds(R.id.image_iv)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.image_iv) {
                showToast("点击了第${position}项的图片")
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTestMvcListBinding.inflate(inflater, container, false)

}