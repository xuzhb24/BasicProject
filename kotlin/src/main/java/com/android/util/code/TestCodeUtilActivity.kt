package com.android.util.code

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.BitmapUtil
import com.android.util.SizeUtil
import kotlinx.android.synthetic.main.activity_test_code_util.*

/**
 * Created by xuzhb on 2019/11/18
 * Desc:
 */
class TestCodeUtilActivity : BaseActivity() {

    override fun handleView(savedInstanceState: Bundle?) {
        et.setText("1234567890abcdefg")
    }

    override fun initListener() {
        val width = SizeUtil.dp2px(250f).toInt()
        val height = SizeUtil.dp2px(250f).toInt()
        title_bar.setOnLeftClickListener {
            finish()
        }
        btn1.setOnClickListener {
            val content = et.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val bitmap = QRCodeUtil.createQRCode(content, width, height)
            iv.setImageBitmap(bitmap)
        }
        btn2.setOnClickListener {
            val content = et.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_logo)
            val bitmap = QRCodeUtil.createQRCode(content, width, height, logo)
            iv.setImageBitmap(bitmap)
        }
        btn3.setOnClickListener {
            val content = et.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！")
                return@setOnClickListener
            }
            val bitmap = BarCodeUtil.creatBarcode(content, width, height / 3)
            iv.setImageBitmap(bitmap)
        }
        btn4.setOnClickListener {
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
        btn5.setOnClickListener {
            et.setText("")
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_code_util

}