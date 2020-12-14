package com.android.util.bitmap

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestBitmapBinding
import com.android.frame.mvc.viewBinding.BaseActivity_VB
import com.android.util.LayoutParamsUtil
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/12/14
 * Desc:
 */
class TestBitmapActivity : BaseActivity_VB<ActivityTestBitmapBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        setImage(BitmapUtil.addTextWatermark(getSrc(), "原图", SizeUtil.sp2px(15f), Color.RED, 10f, 10f, true))
        //缩放
        setImage(BitmapUtil.scale(getSrc(), 0.5f, 0.5f, true))
        setImage(BitmapUtil.scale(getSrc(), SizeUtil.dp2pxInt(150f), SizeUtil.dp2pxInt(150f), true))
        //裁剪
        setImage(BitmapUtil.clip(getSrc(), 150, 150, 250, 250, true))
        //倾斜
        setImage(BitmapUtil.skew(getSrc(), 0.2f, 0.2f, 0f, 0f, true))
        //旋转
        setImage(BitmapUtil.rotate(getSrc(), 90, 100f, 100f, true))
        //圆形图片
        setImage(BitmapUtil.toCircle(getSrc(), true))
        //圆角图片
        setImage(BitmapUtil.toRoundCorner(getSrc(), SizeUtil.dp2px(20f), true))
        //模糊
        setImage(BitmapUtil.fastBlur(getSrc(), 0.8f, 10f, true))
        //添加颜色边框
        setImage(BitmapUtil.addFrame(getSrc(), SizeUtil.dp2px(10f).toInt(), Color.GREEN, true))
        //倒影
        setImage(BitmapUtil.addReflection(getSrc(), 250, true))
        //图片水印
        setImage(BitmapUtil.addImageWatermark(getSrc(), BitmapUtil.getBitmapFromResource(resources, R.mipmap.ic_logo), 10, 10, 150, true))
        //灰度图片
        setImage(BitmapUtil.toGray(getSrc()!!, true))
    }

    private fun setImage(bm: Bitmap?) {
        val iv = ImageView(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        iv.layoutParams = params
        LayoutParamsUtil.setMarginTop(iv, SizeUtil.dp2pxInt(15f))
        iv.setImageBitmap(bm)
        binding.rootLl.addView(iv)
    }

    private fun getSrc() = BitmapUtil.getBitmapFromResource(resources, R.drawable.ic_bitmap)

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityTestBitmapBinding.inflate(layoutInflater)

}