package com.android.widget.RecyclerView.AATest;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import com.android.java.R;
import com.android.widget.RecyclerView.BaseAdapter;
import com.android.widget.RecyclerView.ViewHolder;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:单一布局
 */
public class TestSingleAdapter extends BaseAdapter<String> {

    private String mSelectedItem;

    public TestSingleAdapter(Context context, List<String> dataList, String selectedItem) {
        super(context, dataList, R.layout.item_text_with_image);
        mSelectedItem = selectedItem;
    }

    @Override
    protected void bindData(ViewHolder holder, String data, int position) {
        holder.setText(R.id.value_tv, data);
        if (TextUtils.equals(data, mSelectedItem)) {
            holder.setViewVisible(R.id.selected_iv)
                    .setTextColor(R.id.value_tv, Color.parseColor("#0071FF"));
        } else {
            holder.setViewGone(R.id.selected_iv)
                    .setTextColor(R.id.value_tv, Color.BLACK);
        }
        if (position == getItemCount() - 1) {
            holder.setViewGone(R.id.divider_line);  //隐藏最后一条分界线
        } else {
            holder.setViewVisible(R.id.divider_line);
        }
    }

    //刷新数据，带默认选项
    public void setData(List<String> list, String selectedItem) {
        mSelectedItem = selectedItem;
        setData(list);
    }

}
