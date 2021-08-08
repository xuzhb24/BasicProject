package com.android.frame.mvvm.AATest.listType

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.basicproject.R
import com.android.basicproject.databinding.FragmentTestMvcListBinding
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.mvc.AATest.adapter.NewsListAdapter
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvvm.BaseListFragment

/**
 * Created by xuzhb on 2021/8/8
 * Desc:
 */
class TestMvvmListFragment : BaseListFragment<NewsListBean, FragmentTestMvcListBinding, TestMvvmListFragmentViewModel>() {

    companion object {
        fun newInstance() = TestMvvmListFragment()
    }

    override fun getAdapter() = NewsListAdapter()

    override fun observerListDataChange() {
        //获取到列表数据成功
        viewModel.successData.observe(this, Observer {
            if (mCurrentPage < 3) {
                showData(mCurrentPage, it.data)  //展示列表数据
            } else {  //模拟数据加载到底
                showData(mCurrentPage, mutableListOf())
            }
        })
        //获取列表数据失败
        viewModel.errorData.observe(this, Observer {
            showToast(it.message)  //提示错误信息
        })
    }

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mAdapter.getItem(position)
            WangYiNewsWebviewActivity.start(mContext, "", bean.path)
        }
        //Item内子View的点击事件
        mAdapter.addChildClickViewIds(R.id.image_iv)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.image_iv) {
                showToast("点击了第${position}项的图片")
            }
        }
    }
}