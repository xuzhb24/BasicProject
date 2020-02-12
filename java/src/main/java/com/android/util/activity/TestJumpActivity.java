package com.android.util.activity;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2020/2/3
 * Desc:
 */
public class TestJumpActivity extends BaseActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";

    @BindView(R.id.tv)
    TextView tv;

    @Override
    public void handleView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            CommonLayoutUtil.initCommonLayout(this, "TestJumpActivity", false, true);
            tv.setText(bundle.getString(EXTRA_DATA));
        } else {
            CommonLayoutUtil.initCommonLayout(this, "TestJumpActivity", "返回");
        }

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    @OnClick(R.id.btn1)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
