package com.android.frame.mvc.AATest.adapter

import com.android.kotlin.R
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvc.extra.RecyclerView.LoadMoreAdapter
import com.android.util.SizeUtil
import com.android.util.glide.GlideUtil
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
class NewsListAdapter : LoadMoreAdapter<NewsListBean>(com.android.universal.R.layout.item_test_load_more) {
    override fun convert(holder: BaseViewHolder, item: NewsListBean) {
        GlideUtil.showImageFromUrl(holder.getView(com.android.universal.R.id.image_iv), item.image, RoundedCorners(SizeUtil.dp2pxInt(6f)))
        holder.setText(com.android.universal.R.id.title_tv, item.title).setText(R.id.time_tv, item.passtime)
    }
}