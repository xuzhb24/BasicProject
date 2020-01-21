package com.android.widget.RecyclerView.AATest;

import android.content.Context;
import android.widget.ImageView;
import com.android.base.BaseApplication;
import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.java.R;
import com.android.widget.RecyclerView.BaseAdapter;
import com.android.widget.RecyclerView.LoadMoreAdapter;
import com.android.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:单布局上拉加载更多
 */
public class TestSingleLoadMoreAdapter extends LoadMoreAdapter<NewsListBean.ResultBean> {

    public TestSingleLoadMoreAdapter(Context context, List<NewsListBean.ResultBean> dataList) {
        super(context, dataList, R.layout.item_test_load_more);
    }

    @Override
    protected void bindData(ViewHolder holder, NewsListBean.ResultBean data, int position) {
        ImageView imageIv = holder.getView(R.id.image_iv);
        Glide.with(BaseApplication.getInstance()).load(data.getImage()).into(imageIv);
        holder.setText(R.id.title_tv, data.getTitle()).setText(R.id.time_tv, data.getPasstime());
    }
}
