package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:单一布局
 */
public class TestSingleAdapterActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private String mSelectedItem;
    private List<String> mList;
    private TestSingleAdapter mAdapter;

    @Override
    public void handleView(Bundle savedInstanceState) {
        mList = createData();
        mAdapter = new TestSingleAdapter(this, mList, mSelectedItem);
        srl.setEnabled(false);
        rv.setAdapter(mAdapter);
    }

    private List<String> createData() {
        List<String> list = new ArrayList<>();
        list.add("工商银行");
        list.add("招商银行");
        list.add("农业银行");
        list.add("建设银行");
        list.add("中国银行");
        list.add("广发银行");
        list.add("交通银行");
        list.add("平安银行");
        list.add("兴业银行");
        list.add("民生银行");
        return list;
    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(v -> {
            finish();
        });
        mAdapter.setOnItemClickListener((data, position) -> {
            mSelectedItem = (String) data;
            mAdapter.setData(mList, mSelectedItem);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_adapter;
    }

}
