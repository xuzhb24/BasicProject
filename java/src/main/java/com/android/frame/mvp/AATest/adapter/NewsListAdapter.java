package com.android.frame.mvp.AATest.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.base.BaseApplication;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.java.R;
import com.android.widget.RecyclerView.LoadMoreAdapter;
import com.android.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListAdapter extends LoadMoreAdapter<NewsListBeanMvp> {

    public NewsListAdapter(Context context, List<NewsListBeanMvp> dataList) {
        super(context, dataList, R.layout.item_test_load_more);
    }

    @Override
    protected void bindData(ViewHolder holder, NewsListBeanMvp data, int position) {
        ImageView imageIv = holder.getView(R.id.image_iv);
        Glide.with(BaseApplication.getInstance()).load(data.getImage()).into(imageIv);
        holder.setText(R.id.title_tv, data.getTitle()).setText(R.id.time_tv, data.getPasstime());
    }
}
