package com.android.frame.mvp.AATest.listType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.mvc.AATest.adapter.NewsListAdapter;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvp.BaseListFragment;
import com.android.java.R;
import com.android.java.databinding.FragmentTestMvcListBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpListFragment extends BaseListFragment<NewsListBean, FragmentTestMvcListBinding, TestMvpListView, TestMvpListPresenter> implements TestMvpListView {

    public static TestMvpListFragment newInstance() {
        return new TestMvpListFragment();
    }

    @Override
    public BaseQuickAdapter<NewsListBean, BaseViewHolder> getAdapter() {
        return new NewsListAdapter();
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

    @Override
    public FragmentTestMvcListBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTestMvcListBinding.inflate(inflater, container, false);
    }

    @Override
    public TestMvpListPresenter getPresenter() {
        return new TestMvpListPresenter();
    }
}
