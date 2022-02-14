package com.android.frame.mvvm.AATest.listType;

import android.os.Bundle;

import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.mvc.AATest.adapter.NewsListAdapter;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvvm.BaseListFragment;
import com.android.java.R;
import com.android.java.databinding.FragmentTestMvcListBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmListFragment extends BaseListFragment<NewsListBean, FragmentTestMvcListBinding, TestMvvmListFragmentViewModel> {

    public static TestMvvmListFragment newInstance() {
        return new TestMvvmListFragment();
    }

    @Override
    public BaseQuickAdapter<NewsListBean, BaseViewHolder> getAdapter() {
        return new NewsListAdapter();
    }

    @Override
    protected void observerListDataChange() {
        //获取到列表数据成功
        viewModel.successData.observe(this, response -> {
            if (mCurrentPage < 3) {
                showData(mCurrentPage, response.getData());  //展示列表数据
            } else {  //模拟数据加载到底
                showData(mCurrentPage, new ArrayList<>());
            }
        });
        //获取列表数据失败
        viewModel.errorData.observe(this, it -> {
            showToast("请求异常，" + it.getMessage());  //提示错误信息
        });
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            NewsListBean bean = mAdapter.getItem(position);
            WangYiNewsWebviewActivity.start(mContext, "", bean.getPath());
        });
        //Item内子View的点击事件
        mAdapter.addChildClickViewIds(R.id.image_iv);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.image_iv) {
                showToast("点击了第" + position + "项的图片");
            }
        });
    }

}
