package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.alert
import kotlinx.android.synthetic.main.layout_recycler_view.*

/**
 * Created by xuzhb on 2019/11/1
 * Desc:
 */
class TestMultiAdapterActivity : BaseActivity() {

    private var mSelectedList: MutableList<String> = mutableListOf()
    private var mList: MutableList<String> = mutableListOf()
    private val mAdapter by lazy { TestMultiAdapter(this, mList, mSelectedList) }

    override fun handleView(savedInstanceState: Bundle?) {
        mList = createData()
        mList.add(0, mList.size.toString())
        title_bar.rightText = "已添加列表"
        srl.isEnabled = false
        rv.adapter = mAdapter
    }

    private fun createData(): MutableList<String> = mutableListOf(
        "1111111111", "2222222222", "3333333333", "4444444444", "5555555555",
        "6666666666", "7777777777", "8888888888", "9999999999", "0000000000",
        "1111166666", "2222277777", "3333388888", "4444499999", "5555500000",
        "6666611111", "7777722222", "8888833333", "9999944444", "0000055555"
    )

    override fun initListener() {
        title_bar.setOnClickListener {
            finish()
        }
        title_bar.setOnRightClickListener {
            val sb = StringBuilder()
            if (mSelectedList.size == 0) {
                sb.append("列表为空")
            } else {
                mSelectedList.forEach {
                    sb.append("$it\n")
                }
            }
            alert(this, sb.toString())
        }
        mAdapter.setOnCheckedChangeListener { cb, isChecked, data, position ->
            if (isChecked) {
                mSelectedList.remove(data)
            } else {
                mSelectedList.add(data)
            }
            //通过setData刷新数据，不要直接通过更新子View刷新数据，如cb.isChecked = false，
            //这样会导致其他不可见区域的子View也发生变化
            mAdapter.setData(mList, mSelectedList)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_recycler_view
}