package com.android.widget.RecyclerView.AATest;

import android.content.Context;
import com.android.java.R;
import com.android.util.ToastUtil;
import com.android.widget.RecyclerView.AATest.entity.DetailBean;
import com.android.widget.RecyclerView.AATest.entity.MonthBean;
import com.android.widget.RecyclerView.BaseAdapter;
import com.android.widget.RecyclerView.MultiViewType;
import com.android.widget.RecyclerView.ViewHolder;
import com.google.gson.Gson;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:多布局
 */
public class TestMultiAdapter extends BaseAdapter<String> {

    public TestMultiAdapter(Context context, List<String> dataList) {
        super(context, dataList, new MultiViewType<String>() {
            @Override
            public int getLayoutId(String data, int position, int totalCount) {
                if (dataList.get(position).contains("month")) {
                    return R.layout.item_month;
                }
                return R.layout.item_detail;
            }
        });
    }

    @Override
    protected void bindData(ViewHolder holder, String data, int position) {
        Gson gson = new Gson();
        if (data.contains("month")) {
            MonthBean bean = gson.fromJson(data, MonthBean.class);
            holder.setText(R.id.month_tv, bean.getMonth()).
                    setText(R.id.count_tv, "总计" + bean.getCount() + "个");
        } else {
            DetailBean bean = gson.fromJson(data, DetailBean.class);
            holder.setText(R.id.detail_tv, bean.getDetail()).setText(R.id.time_tv, bean.getTime())
                    .setOnItemClickListener(v -> {
                        ToastUtil.showToast(bean.getDetail());
                    });
        }
    }
}
