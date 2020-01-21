package com.android.widget.PopupWindow.AATest;

import android.content.Context;
import com.android.java.R;
import com.android.widget.RecyclerView.BaseAdapter;
import com.android.widget.RecyclerView.ViewHolder;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class PopupWindowAdapter extends BaseAdapter<String> {

    public PopupWindowAdapter(Context context, List<String> dataList) {
        super(context, dataList, R.layout.item_popup_window);
    }

    @Override
    protected void bindData(ViewHolder holder, String data, int position) {
        holder.setText(R.id.item_tv, data);
        if (position == getItemCount() - 1) {
            holder.setViewGone(R.id.divider_line);
        } else {
            holder.setViewVisible(R.id.divider_line);
        }
    }
}
