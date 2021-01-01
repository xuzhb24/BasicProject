package com.android.frame.mvc.AATest.adapter;

import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvc.extra.RecyclerView.LoadMoreAdapter;
import com.android.java.R;
import com.android.util.SizeUtil;
import com.android.util.glide.GlideUtil;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class NewsListAdapter extends LoadMoreAdapter<NewsListBean> {

    public NewsListAdapter() {
        super(R.layout.item_test_load_more);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, NewsListBean item) {
        GlideUtil.showImageFromUrl(holder.getView(R.id.image_iv), item.getImage(),
                new RoundedCorners(SizeUtil.dp2pxInt(6f)),
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        holder.setText(R.id.title_tv, item.getTitle()).setText(R.id.time_tv, item.getPasstime());
    }
}
