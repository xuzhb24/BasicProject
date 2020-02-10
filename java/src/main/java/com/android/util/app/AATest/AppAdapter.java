package com.android.util.app.AATest;

import android.content.Context;
import com.android.java.R;
import com.android.util.app.AppInfo;
import com.android.widget.RecyclerView.BaseAdapter;
import com.android.widget.RecyclerView.ViewHolder;

import java.util.List;

/**
 * Created by xuzhb on 2020/2/5
 * Desc:
 */
public class AppAdapter extends BaseAdapter<AppInfo> {

    public AppAdapter(Context context, List<AppInfo> dataList) {
        super(context, dataList, R.layout.item_image_with_text);
    }

    @Override
    protected void bindData(ViewHolder holder, AppInfo data, int position) {
        holder.setImageDrawable(R.id.item_iv, data.getIcon())
                .setText(R.id.item_tv, data.getLabel());
    }
}
