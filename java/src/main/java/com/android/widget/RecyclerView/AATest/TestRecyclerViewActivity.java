package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class TestRecyclerViewActivity extends BaseActivity {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(
                this, "RecyclerView",
                "单一布局", "多布局", "头部Header", "尾部Footer", "下拉刷新和上拉加载更多",
                "单一布局上拉加载更多", "多布局上拉加载更多"
        );
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:  //单一布局
                startActivity(TestSingleAdapterActivity.class);
                break;
            case R.id.btn2:  //多布局
                startActivity(TestMultiAdapterActivity.class);
                break;
            case R.id.btn3:  //头部Header
                startActivity(TestHeaderAdapterActivity.class);
                break;
            case R.id.btn4:  //尾部Footer
                startActivity(TestFooterAdapterActivity.class);
                break;
            case R.id.btn5:  //下拉刷新和上拉加载更多
                startActivity(TestLoadMoreWrapperActivity.class);
                break;
            case R.id.btn6:  //单一布局上拉加载更多
                startActivity(TestSingleLoadMoreAdapterActivity.class);
                break;
            case R.id.btn7:  //多布局上拉加载更多
                startActivity(TestMultiLoadMoreAdapterActivity.class);
                break;
        }
    }
}
