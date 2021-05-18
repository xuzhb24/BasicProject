package com.android.util.CapturePicture

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestCaptureRecyclerviewBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.bitmap.BitmapUtil
import com.android.util.permission.PermissionUtil
import com.android.widget.RecyclerView.ViewHolder
import kotlin.random.Random

/**
 * Created by xuzhb on 2021/5/13
 * Desc:
 */
class TestCaptureRecyclerViewActivity : BaseActivity<ActivityTestCaptureRecyclerviewBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//
//        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
//        binding.recyclerView.layoutManager = GridLayoutManager(this, 3, RecyclerView.HORIZONTAL, false)
//
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)

        val list: MutableList<String> = mutableListOf()
        for (i in 1..20) {
//            list.add("文本$i")
            list.add(createExpandText(Random.nextInt(50)))
        }
        binding.recyclerView.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test_capture, null)
                return ViewHolder(view)
            }

            override fun getItemCount(): Int = list.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.setText(R.id.tv, list[position])
            }

            override fun getItemViewType(position: Int): Int = position

        }
    }

    override fun initListener() {
        mTitleBar?.setOnRightTextClickListener {
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showToast("请先允许权限")
                return@setOnRightTextClickListener
            }
            val bitmap = CapturePictureUtil.captureByRecyclerView(binding.recyclerView)
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "RecyclerView截图")) {
                showToast("保存成功，请在相册查看")
            } else {
                showToast("保存失败")
            }
        }
    }

    override fun getViewBinding() = ActivityTestCaptureRecyclerviewBinding.inflate(layoutInflater)

    private fun createExpandText(count: Int): String {
        var array = arrayOf(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "a", "b", "c", "d", "e", "f", "t", "g", "q", "w", "e",
            "p", "l", "k", "i", "n", "m", "G", "H", "J", "I", "L", "C", "V", "B"
            , "你", "我", "他", "天", "地", "动", "进", "啊", "去", "改", "酒",
            "一", "会", "年", "收", "好", "嗯", "这", "有",
            /*"\r", "\n", "\r\n", "\t",*/ "，", "！", "%", "@"
        )
        val sb = StringBuilder()
        for (i in 0 until count) {
            sb.append(array[Random.nextInt(array.size)])
        }
        return sb.toString()
    }

}