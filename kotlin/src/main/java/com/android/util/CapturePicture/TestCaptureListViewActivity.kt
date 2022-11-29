package com.android.util.CapturePicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestCaptureListviewBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.bitmap.BitmapUtil

/**
 * Created by xuzhb on 2021/5/13
 * Desc:
 */
class TestCaptureListViewActivity : BaseActivity<ActivityTestCaptureListviewBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        val list: MutableList<String> = mutableListOf()
        for (i in 1..20) {
            list.add("文本$i")
        }
        binding.listView.adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = LayoutInflater.from(applicationContext).inflate(R.layout.item_test_capture, null)
                view.findViewById<TextView>(R.id.tv).text = list[position]
                return view
            }

            override fun getItem(position: Int): String = list[position]

            override fun getItemId(position: Int): Long = position.toLong()

            override fun getCount() = list.size
        }
    }

    override fun initListener() {
        mTitleBar?.setOnRightTextClickListener {
            val bitmap = CapturePictureUtil.captureByListView(binding.listView)
            BitmapUtil.saveBitmapToGallery(this, bitmap, "ListView截图")
        }
    }

}