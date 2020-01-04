package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_test_adapter.*

/**
 * Created by xuzhb on 2019/10/31
 * Desc:
 */
class TestSingleAdapterActivity : BaseActivity() {

    private var mSelectedItem = ""
    private var mList: MutableList<String> = mutableListOf()
    private val mAdapter by lazy { TestSingleAdapter(this, mList, mSelectedItem) }

    override fun handleView(savedInstanceState: Bundle?) {
        mList = createData()
        srl.isEnabled = false
        rv.adapter = mAdapter
    }

    private fun createData(): MutableList<String> = mutableListOf(
        "工商银行", "招商银行", "农业银行", "建设银行", "中国银行", "广发银行", "交通银行",
        "平安银行", "兴业银行", "民生银行"
    )

    override fun initListener() {
        title_bar.setOnClickListener {
            finish()
        }
        mAdapter.setOnItemClickListener { obj, position ->
            mSelectedItem = obj as String
            mAdapter.setData(mList, mSelectedItem)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_adapter
}