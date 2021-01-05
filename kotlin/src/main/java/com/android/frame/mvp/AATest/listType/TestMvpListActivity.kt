package com.android.frame.mvp.AATest.listType

import android.os.Bundle
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestMvcListBinding
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.mvc.AATest.adapter.NewsListAdapter
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvp.BaseListActivity

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpListActivity : BaseListActivity<NewsListBean, ActivityTestMvcListBinding, TestMvpListView, TestMvpListPresenter>(),
    TestMvpListView {

    override fun getAdapter() = NewsListAdapter()

    override fun handleView(savedInstanceState: Bundle?) {
        mTitleBar?.titleText = "列表Activity(MVP)"
    }

    override fun initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mAdapter.getItem(position)
            WangYiNewsWebviewActivity.start(this, "", bean.path)
        }
        //Item内子View的点击事件
        mAdapter.addChildClickViewIds(R.id.image_iv)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.image_iv) {
                showToast("点击了第${position}项的图片")
            }
        }
    }

    override fun getViewBinding() = ActivityTestMvcListBinding.inflate(layoutInflater)

    override fun getPresenter() = TestMvpListPresenter()

}