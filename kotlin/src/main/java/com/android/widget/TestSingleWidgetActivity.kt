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
        with(title_bar) {
            setOnLeftClickListener {
                finish()
            }
            setOnRightClickListener {
                AlertDialog.Builder(this@TestSingleWidgetActivity)
                    .setMessage("本页面主要是查看编写的一些单一控件的样式")
                    .show()
            }
        }
        //密码输入框
        with(password_edittext) {
            setOnTextChangeListener {
                pet_tv.text = it
            }
            setOnTextCompleteListener {
                showToast(it)
            }
        }
        pet_btn.setOnClickListener {
            password_edittext.clearText()
        }
        //搜索框
        with(search_layout) {
            setOnTextChangedListener { s, start, before, count ->
                searchlayout_tv.text = s
            }
        }
        //带删除按钮的输入框
        with(input_layout) {
            setOnTextChangedListener { s, start, before, count ->
                inputlayout_tv.text = s
            }
            setOnTextClearListener {
                showToast("文本被清空了")
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_single_widget
}