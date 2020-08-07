package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestAdapterBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.alert

/**
 * Created by xuzhb on 2019/11/17
 * Desc:尾部Footer
 */
class TestFooterAdapterActivity : BaseActivity<ActivityTestAdapterBinding>() {

    private var mSelectedList: MutableList<String> = mutableListOf()
    private var mList: MutableList<String> = mutableListOf()
    private val mAdapter by lazy { TestFooterAdapter(this, mList, mSelectedList) }

    override fun handleView(savedInstanceState: Bundle?) {
        mList = createData()
        mList.add("没有更多数据了")  //添加尾部的Footer
        mTitleBar?.rightText = "已添加列表"
        binding.srl.isEnabled = false
        binding.rv.adapter = mAdapter
    }

    private fun createData(): MutableList<String> = mutableListOf(
        "1111111111", "2222222222", "3333333333", "4444444444", "5555555555",
        "6666666666", "7777777777", "8888888888", "9999999999", "0000000000"
    )

    override fun initListener() {
        mTitleBar?.setOnRightClickListener {
            val sb = StringBuilder()
            sb.append("\n")
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

    override fun getViewBinding() = ActivityTestAdapterBinding.inflate(layoutInflater)
}