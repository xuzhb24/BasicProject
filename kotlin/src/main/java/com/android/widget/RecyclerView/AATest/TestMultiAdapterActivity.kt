package com.android.widget.RecyclerView.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestAdapterBinding
import com.android.frame.mvc.BaseActivity
import com.android.widget.RecyclerView.AATest.entity.DetailBean
import com.android.widget.RecyclerView.AATest.entity.MonthBean
import com.google.gson.Gson

/**
 * Created by xuzhb on 2019/11/1
 * Desc:多布局
 */
class TestMultiAdapterActivity : BaseActivity<ActivityTestAdapterBinding>() {

    private var mList: MutableList<String> = mutableListOf()
    private val mAdapter by lazy { TestMultiAdapter(this, mList) }

    override fun handleView(savedInstanceState: Bundle?) {
        mList = createData()
        binding.srl.isEnabled = false
        binding.rv.adapter = mAdapter
    }

    private fun createData(): MutableList<String> {
        val gson = Gson()
        val list: MutableList<String> = mutableListOf()
        with(list) {
            add(gson.toJson(MonthBean("2019年11月", "3")))
            add(gson.toJson(DetailBean("十一月文本一", "2019-11-15")))
            add(gson.toJson(DetailBean("十一月文本二", "2019-11-5")))
            add(gson.toJson(DetailBean("十一月文本三", "2019-11-1")))
            add(gson.toJson(MonthBean("2019年10月", "2")))
            add(gson.toJson(DetailBean("十月文本一", "2019-10-25")))
            add(gson.toJson(DetailBean("十月文本二", "2019-10-8")))
            add(gson.toJson(MonthBean("2019年8月", "5")))
            add(gson.toJson(DetailBean("八月文本一", "2019-8-25")))
            add(gson.toJson(DetailBean("八月文本二", "2019-8-21")))
            add(gson.toJson(DetailBean("八月文本三", "2019-8-14")))
            add(gson.toJson(DetailBean("八月文本四", "2019-8-12")))
            add(gson.toJson(DetailBean("八月文本五", "2019-8-1")))
        }
        return list
    }

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityTestAdapterBinding.inflate(layoutInflater)

}