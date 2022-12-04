package com.android.frame.mvc.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.frame.mvc.WebviewActivity
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2022/12/4
 * Desc:
 */
class TestWebviewActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "Webview", "图片上传", "图片查看", "视频全屏播放")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            val url = "https://www.gaitubao.com/"
            WebviewActivity.start(this, "图片上传", url, true)
        }
        binding.btn2.setOnClickListener {
            val url = "https://baijiahao.baidu.com/s?id=1729854270473235328&wfr=spider&for=pc"
            WebviewActivity.start(this, "图片查看", url, true)
        }
        binding.btn3.setOnClickListener {
            val url = "https://www.iqiyi.com/"
            WebviewActivity.start(this, "视频全屏播放", url, true)
        }
    }

}