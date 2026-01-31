package com.android.util.code

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import com.android.frame.mvc.BaseActivity
import com.android.kotlin.databinding.ActivityTestCodeUtilBinding
import com.android.util.SizeUtil
import com.android.util.bitmap.BitmapUtil

/**
 * Created by xuzhb on 2019/11/18
 * Desc:
 */
class TestCodeUtilActivity : BaseActivity<ActivityTestCodeUtilBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        binding.il.inputText = "1234567890abcdefg"
    }

    override fun initListener() {
        val width = SizeUtil.dp2px(250f).toInt()
        val height = SizeUtil.dp2px(250f).toInt()
        binding.btn1.setOnClickListener {
            val content = binding.il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val bitmap = QRCodeUtil.createQRCode(content, width, height)
            binding.iv.setImageBitmap(bitmap)
        }
        binding.btn2.setOnClickListener {
            val content = binding.il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val logo = BitmapFactory.decodeResource(resources, com.android.universal.R.mipmap.ic_launcher)
            val bitmap = QRCodeUtil.createQRCode(content, width, height, logo)
            binding.iv.setImageBitmap(bitmap)
        }
        binding.btn3.setOnClickListener {
            val content = binding.il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val bitmap = BarCodeUtil.creatBarcode(content, width, height / 3)
            binding.iv.setImageBitmap(bitmap)
        }
        binding.btn4.setOnClickListener {
            if (binding.iv.drawable == null) {
                showToast("请先生成二维码")
                return@setOnClickListener
            }
            BitmapUtil.saveBitmapToGallery(this, (binding.iv.drawable as BitmapDrawable).bitmap, System.currentTimeMillis().toString())
        }
    }

}