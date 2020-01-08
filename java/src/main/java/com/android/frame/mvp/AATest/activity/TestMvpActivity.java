package com.android.frame.mvp.AATest.activity;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import com.android.frame.mvp.AATest.activity.newslist.NewsListActivity;
import com.android.frame.mvp.AATest.activity.weather.WeatherActivity;
import com.android.frame.mvp.CommonBaseActivity;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class TestMvpActivity extends CommonBaseActivity {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "MVP框架", "天气信息", "网易新闻");
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(WeatherActivity.class);
                break;
            case R.id.btn2:
                startActivity(NewsListActivity.class);
                break;
        }
    }
}
