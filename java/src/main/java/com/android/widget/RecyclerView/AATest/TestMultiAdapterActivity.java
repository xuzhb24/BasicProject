package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestAdapterBinding;
import com.android.widget.RecyclerView.AATest.entity.DetailBean;
import com.android.widget.RecyclerView.AATest.entity.MonthBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:多布局
 */
public class TestMultiAdapterActivity extends BaseActivity<ActivityTestAdapterBinding> {

    private TestMultiAdapter mAdapter;

    @Override
    public void handleView(Bundle savedInstanceState) {
        mAdapter = new TestMultiAdapter(this, createData());
        binding.srl.setEnabled(false);
        binding.rv.setAdapter(mAdapter);
    }

    private List<String> createData() {
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();
        list.add(gson.toJson(new MonthBean("2019年11月", "3")));
        list.add(gson.toJson(new DetailBean("十一月文本一", "2019-11-15")));
        list.add(gson.toJson(new DetailBean("十一月文本二", "2019-11-5")));
        list.add(gson.toJson(new DetailBean("十一月文本三", "2019-11-1")));
        list.add(gson.toJson(new MonthBean("2019年10月", "2")));
        list.add(gson.toJson(new DetailBean("十月文本一", "2019-10-25")));
        list.add(gson.toJson(new DetailBean("十月文本二", "2019-10-8")));
        list.add(gson.toJson(new MonthBean("2019年8月", "5")));
        list.add(gson.toJson(new DetailBean("八月文本一", "2019-8-25")));
        list.add(gson.toJson(new DetailBean("八月文本二", "2019-8-21")));
        list.add(gson.toJson(new DetailBean("八月文本三", "2019-8-14")));
        list.add(gson.toJson(new DetailBean("八月文本四", "2019-8-12")));
        list.add(gson.toJson(new DetailBean("八月文本五", "2019-8-1")));
        return list;
    }

    @Override
    public void initListener() {

    }

}
