package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestAdapterBinding;
import com.android.util.ExtraUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:头部Header
 */
public class TestHeaderAdapterActivity extends BaseActivity<ActivityTestAdapterBinding> {

    private List<String> mSelectedList = new ArrayList<>();
    private List<String> mList;
    private TestHeaderAdapter mAdapter;

    @Override
    public void handleView(Bundle savedInstanceState) {
        mList = createData();
        mList.add(0, String.valueOf(mList.size()));
        mAdapter = new TestHeaderAdapter(this, mList, mSelectedList);
        mTitleBar.setRightText("已添加列表");
        binding.srl.setEnabled(false);
        binding.rv.setAdapter(mAdapter);
    }

    private List<String> createData() {
        List<String> list = new ArrayList<>();
        list.add("1111111111");
        list.add("2222222222");
        list.add("3333333333");
        list.add("4444444444");
        list.add("5555555555");
        list.add("6666666666");
        list.add("7777777777");
        list.add("8888888888");
        list.add("9999999999");
        list.add("0000000000");
        list.add("1111166666");
        list.add("2222277777");
        list.add("3333388888");
        list.add("4444499999");
        list.add("5555500000");
        list.add("6666611111");
        list.add("7777722222");
        list.add("8888833333");
        list.add("9999944444");
        list.add("0000055555");
        return list;
    }

    @Override
    public void initListener() {
        mTitleBar.setOnRightClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            if (mSelectedList.size() == 0) {
                sb.append("列表为空");
            } else {
                for (String s : mSelectedList) {
                    sb.append(s).append("\n");
                }
            }
            ExtraUtil.alert(this, sb.toString());
        });
        mAdapter.setOnCheckedChangeListener((cb, isChecked, data, position) -> {
            if (isChecked) {
                mSelectedList.remove(data);
            } else {
                mSelectedList.add(data);
            }
            //通过setData刷新数据，不要直接通过更新子View刷新数据，如cb.isChecked = false，
            //这样会导致其他不可见区域的子View也发生变化
            mAdapter.setData(mList, mSelectedList);
        });
    }

    @Override
    public ActivityTestAdapterBinding getViewBinding() {
        return ActivityTestAdapterBinding.inflate(getLayoutInflater());
    }

}
