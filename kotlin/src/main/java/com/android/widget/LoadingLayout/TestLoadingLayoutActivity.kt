package com.android.widget.LoadingLayout

import android.os.Bundle
import android.os.Handler
import com.android.basicproject.databinding.ActivityTestLoadingLayoutBinding
import com.android.frame.mvc.BaseActivity

/**
 * Created by xuzhb on 2020/7/20
 * Desc:
 */
class TestLoadingLayoutActivity : BaseActivity<ActivityTestLoadingLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        binding.loadingLayout.loadStart()
        Handler().postDelayed({ binding.loadingLayout.loadComplete() }, 2000)
    }

    override fun initListener() {
        //加载成功
        binding.btn1.setOnClickListener {
            binding.loadingLayout.loadStart()
            Handler().postDelayed({ binding.loadingLayout.loadComplete() }, 2000)
        }
        //加载失败
        binding.btn2.setOnClickListener {
            binding.loadingLayout.loadStart()
            Handler().postDelayed({ binding.loadingLayout.loadFail() }, 2000)
        }
        //加载后为空
        binding.btn3.setOnClickListener {
            binding.loadingLayout.loadStart()
            Handler().postDelayed({ binding.loadingLayout.loadEmpty() }, 2000)
        }
        //点击重试
        binding.loadingLayout.setOnFailListener {
            binding.loadingLayout.loadStart()
            Handler().postDelayed({ binding.loadingLayout.loadComplete() }, 2000)
        }
        //空数据时点击按钮回调
        binding.loadingLayout.setOnEmptyListener {
            showToast("页面无数据")
        }
    }

}