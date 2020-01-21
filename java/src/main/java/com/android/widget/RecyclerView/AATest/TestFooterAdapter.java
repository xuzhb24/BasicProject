package com.android.widget.RecyclerView.AATest;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import com.android.java.R;
import com.android.widget.RecyclerView.BaseAdapter;
import com.android.widget.RecyclerView.MultiViewType;
import com.android.widget.RecyclerView.ViewHolder;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:尾部Footer
 */
public class TestFooterAdapter extends BaseAdapter<String> {

    private List<String> mSelectedList;

    public TestFooterAdapter(Context context, List<String> dataList, List<String> selectedList) {
        super(context, dataList, new MultiViewType<String>() {
            @Override
            public int getLayoutId(String data, int position, int totalCount) {
                if (position == totalCount - 1) {
                    return R.layout.item_header;
                }
                return R.layout.item_text_with_checkbox;
            }
        });
        mSelectedList = selectedList;
    }

    @Override
    protected void bindData(ViewHolder holder, String data, int position) {
        if (position == getItemCount() - 1) {
            holder.setText(R.id.count_tv, data);
        } else {
            holder.setText(R.id.value_tv, data);
            CheckBox cb = holder.getView(R.id.check_cb);
            cb.setChecked(false);
            if (mSelectedList != null && mSelectedList.size() != 0) {  //初始化选择项
                for (String s : mSelectedList) {
                    if (TextUtils.equals(s, data)) {
                        cb.setChecked(true);
                    }
                }
            }
            holder.setOnItemClickListener(v -> {
                if (mListener != null) {
                    mListener.onCheckedChanged(cb, cb.isChecked(), data, position);
                }
            });
            if (position == getItemCount() - 1) {
                holder.setViewGone(R.id.divider_line);  //隐藏最后一条分界线
            } else {
                holder.setViewVisible(R.id.divider_line);
            }
        }
    }

    //刷新数据，带默认选项
    public void setData(List<String> list, List<String> selectedList) {
        mSelectedList = selectedList;
        setData(list);
    }

    private TestHeaderAdapter.OnCheckedChangeListener mListener;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CheckBox cb, boolean isChecked, String data, int position);
    }

    public void setOnCheckedChangeListener(TestHeaderAdapter.OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

}
