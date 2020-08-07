package com.android.util.code

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestCodeUtilBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.BitmapUtil
import com.android.util.SizeUtil
import kotlinx.android.synthetic.main.activity_test_code_util.*

/**
 * Created by xuzhb on 2019/11/18
 * Desc:
 */
class TestCodeUtilActivity : BaseActivity<ActivityTestCodeUtilBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        il.inputText = "1234567890abcdefg"
    }

    override fun initListener() {
        val width = SizeUtil.dp2px(250f).toInt()
        val height = SizeUtil.dp2px(250f).toInt()
        btn1.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val bitmap = QRCodeUtil.createQRCode(content, width, height)
            iv.setImageBitmap(bitmap)
        }
        btn2.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val bitmap = QRCodeUtil.createQRCode(content, width, height, logo)
            iv.setImageBitmap(bitmap)
        }
        btn3.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val bitmap = BarCodeUtil.creatBarcode(content, width, height / 3)
            iv.setImageBitmap(bitmap)
        }
        btn4.setOnClickListener {
            if (iv.drawable == null) {
                showToast("请先生成二维码")
                return@setOnClickListener
            }
            val flag = BitmapUtil.saveImageToGallery(
                this,
                (iv.drawable as BitmapDrawable).bitmap,
                System.currentTimeMillis().toString()
            )
            if (flag) {
                showToast("保存成功！")
            } else {
                showToast("保存失败！")
            }
        }
    }

    override fun getViewBinding() = ActivityTestCodeUtilBinding.inflate(layoutInflater)

}