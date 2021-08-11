package com.android.frame.mvvm.AATest.listType;

import android.os.Bundle;

import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.mvc.AATest.adapter.NewsListAdapter;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvvm.BaseListActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestMvcListBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmListActivity extends BaseListActivity<NewsListBean, ActivityTestMvcListBinding, TestMvvmListActivityViewModel> {

    @Override
    public BaseQuickAdapter<NewsListBean, BaseViewHolder> getAdapter() {
        return new NewsListAdapter();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("列表Activity(MVVM)");
    }

    @Override
    public void initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            NewsListBean bean = mAdapter.getItem(position);
            WangYiNewsWebviewActivity.start(this, "", bean.getPath());
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
