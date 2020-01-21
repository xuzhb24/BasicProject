package com.android.widget.RecyclerView.AATest;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import com.android.base.BaseApplication;
import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.java.R;
import com.android.widget.RecyclerView.AATest.entity.DateBean;
import com.android.widget.RecyclerView.LoadMoreAdapter;
import com.android.widget.RecyclerView.MultiViewType;
import com.android.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:多布局上拉加载更多
 */
public class TestMultiLoadMoreAdapter extends LoadMoreAdapter<String> {

    public TestMultiLoadMoreAdapter(Context context, List<String> dataList) {
        super(context, dataList, new MultiViewType<String>() {
            @Override
            public int getLayoutId(String data, int position, int totalCount) {
                if (dataList.get(position).contains("date")) {
                    return R.layout.item_month;
                }
                return R.layout.item_test_load_more;
            }
        });
    }

    @Override
    protected void bindData(ViewHolder holder, String data, int position) {
        Gson gson = new Gson();
        if (data.contains("date")) {
            DateBean bean = gson.fromJson(data, DateBean.class);
            holder.setText(R.id.month_tv, bean.getDate())
                    .setTextColor(R.id.month_tv, Color.parseColor("#db4b3c"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            NewsListBean.ResultBean bean = gson.fromJson(data, NewsListBean.ResultBean.class);
            ImageView imageIv = holder.getView(R.id.image_iv);
            Glide.with(BaseApplication.getInstance()).load(bean.getImage()).into(imageIv);
            holder.setText(R.id.title_tv, bean.getTitle())
                    .setText(R.id.time_tv, bean.getPasstime())
                    .setOnItemClickListener(v -> {
                        if (mOnGetNewsListener != null) {
                            mOnGetNewsListener.onGetNews(bean, position);
                        }
                    });
        }
    }

    private OnGetNewsListener mOnGetNewsListener;

    public interface OnGetNewsListener {
        void onGetNews(Object data, int position);
    }

    public void setOnGetNewsListener(OnGetNewsListener listener) {
        this.mOnGetNewsListener = listener;
    }

}
