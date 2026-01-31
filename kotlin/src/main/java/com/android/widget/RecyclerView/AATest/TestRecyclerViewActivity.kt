package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import com.android.frame.mvc.BaseActivity
import com.android.kotlin.databinding.ActivityCommonLayoutBinding
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2019/10/30
 * Desc:
 */
class TestRecyclerViewActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "RecyclerView",
            "单一布局", "多布局", "头部Header", "尾部Footer", "下拉刷新和上拉加载更多",
            "单一布局上拉加载更多", "多布局上拉加载更多"
        )
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            startActivity(TestSingleAdapterActivity::class.java)
        }
        binding.btn2.setOnClickListener {
            startActivity(TestMultiAdapterActivity::class.java)
        }
        binding.btn3.setOnClickListener {
            startActivity(TestHeaderAdapterActivity::class.java)
        }
        binding.btn4.setOnClickListener {
            startActivity(TestFooterAdapterActivity::class.java)
        }
        binding.btn5.setOnClickListener {
            startActivity(TestLoadMoreWrapperActivity::class.java)
        }
        binding.btn6.setOnClickListener {
            startActivity(TestSingleLoadMoreAdapterActivity::class.java)
        }
        binding.btn7.setOnClickListener {
            startActivity(TestMultiLoadMoreAdapterActivity::class.java)
        }
    }

}