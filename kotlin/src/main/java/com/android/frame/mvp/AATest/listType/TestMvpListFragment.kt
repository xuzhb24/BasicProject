package com.android.frame.mvp.AATest.listType

import android.os.Bundle
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.mvc.AATest.adapter.NewsListAdapter
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvp.BaseListFragment
import com.android.kotlin.databinding.FragmentTestMvcListBinding

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpListFragment : BaseListFragment<NewsListBean, FragmentTestMvcListBinding, TestMvpListView, TestMvpListPresenter>(),
    TestMvpListView {

    companion object {
        fun newInstance() = TestMvpListFragment()
    }

    override fun getAdapter() = NewsListAdapter()

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mAdapter.getItem(position)
            WangYiNewsWebviewActivity.start(mContext, "", bean.path)
        }
        //Item内子View的点击事件
        mAdapter.addChildClickViewIds(com.android.universal.R.id.image_iv)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == com.android.universal.R.id.image_iv) {
                showToast("点击了第${position}项的图片")
            }
        }
    }

    override fun getPresenter() = TestMvpListPresenter()

}