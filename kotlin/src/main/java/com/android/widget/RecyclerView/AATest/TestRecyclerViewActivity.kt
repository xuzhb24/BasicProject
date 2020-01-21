package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2019/10/30
 * Desc:
 */
class TestRecyclerViewActivity : BaseActivity() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "RecyclerView",
            "单一布局", "多布局", "头部Header", "尾部Footer", "下拉刷新和上拉加载更多",
            "单一布局上拉加载更多", "多布局上拉加载更多"
        )
    }

    override fun initListener() {
        btn1.setOnClickListener {
            startActivity(TestSingleAdapterActivity::class.java)
        }
        btn2.setOnClickListener {
            startActivity(TestMultiAdapterActivity::class.java)
        }
        btn3.setOnClickListener {
            startActivity(TestHeaderAdapterActivity::class.java)
        }
        btn4.setOnClickListener {
            startActivity(TestFooterAdapterActivity::class.java)
        }
        btn5.setOnClickListener {
            startActivity(TestLoadMoreWrapperActivity::class.java)
        }
        btn6.setOnClickListener {
            startActivity(TestSingleLoadMoreAdapterActivity::class.java)
        }
        btn7.setOnClickListener {
            startActivity(TestMultiLoadMoreAdapterActivity::class.java)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout
}