package com.android.widget

import android.app.AlertDialog
import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_test_single_widget.*

/**
 * Created by xuzhb on 2019/10/20
 * Desc:单一控件
 */
class TestSingleWidgetActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        //标题栏
        title_bar.setOnLeftClickListener {
            finish()
        }
        title_bar.setOnRightClickListener {
            AlertDialog.Builder(this)
                .setMessage("本页面主要是查看编写的一些单一控件的样式")
                .show()
        }
        //搜索框
        search_layout.setOnTextChangedListener { s, start, before, count ->
            searchlayout_tv.text = s
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_single_widget
}