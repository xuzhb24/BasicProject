package com.android.frame.mvc.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.frame.mvc.WebviewActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2022/11/25
 * Desc:
 */
public class TestWebviewActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "Webview", "图片上传", "图片查看", "视频全屏播放");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            String url = "https://www.gaitubao.com/";
            WebviewActivity.start(TestWebviewActivity.this, "图片上传", url, true);
        });
        binding.btn2.setOnClickListener(v -> {
            String url = "https://baijiahao.baidu.com/s?id=1729854270473235328&wfr=spider&for=pc";
            WebviewActivity.start(TestWebviewActivity.this, "图片查看", url, true);
        });
        binding.btn3.setOnClickListener(v -> {
            String url = "https://www.iqiyi.com/";
            WebviewActivity.start(TestWebviewActivity.this, "视频全屏播放", url, true);
        });
    }

}
