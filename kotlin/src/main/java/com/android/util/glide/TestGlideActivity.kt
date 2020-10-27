package com.android.util.glide

import android.os.Bundle
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestGlideBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**
 * Created by xuzhb on 2020/10/21
 * Desc:
 */
class TestGlideActivity : BaseActivity<ActivityTestGlideBinding>() {

    private val imageUrl = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3225163326,3627210682&fm=26&gp=0.jpg"

    override fun handleView(savedInstanceState: Bundle?) {
        //centerCrop（不会拉伸）、圆角20dp、正常显示
        GlideUtil.showImageFromUrl(
            binding.iv1, imageUrl,
            RoundedCorners(SizeUtil.dp2pxInt(20f)),
            R.drawable.ic_bitmap, R.drawable.ic_bitmap
        )
        //centerCrop（不会拉伸）、圆角20dp、显示缺省页
        GlideUtil.showImageFromUrl(
            binding.iv2, "",
            RoundedCorners(SizeUtil.dp2pxInt(20f)),
            R.drawable.ic_bitmap, R.drawable.ic_bitmap
        )
        //centerCrop（不会拉伸）、左上角和右下角圆角20dp、正常显示
        GlideUtil.showImageFromUrl(
            binding.iv3, imageUrl,
            SectionRoundedCorners(
                SizeUtil.dp2px(20f), 0f,
                0f, SizeUtil.dp2px(20f)
            ),
            R.drawable.ic_bitmap, R.drawable.ic_bitmap
        )
        //centerCrop（不会拉伸）、左上角和右下角圆角20dp、显示缺省页
        GlideUtil.showImageFromUrl(
            binding.iv4, "",
            SectionRoundedCorners(
                SizeUtil.dp2px(20f), 0f,
                0f, SizeUtil.dp2px(20f)
            ),
            R.drawable.ic_bitmap, R.drawable.ic_bitmap
        )
        //fitXY（会拉伸）、正常显示
        GlideUtil.loadUrl(binding.iv5, imageUrl).into(binding.iv5)
        //fitXY（会拉伸）、显示缺省页
        GlideUtil.loadUrl(binding.iv6, "", placeResId = R.drawable.ic_bg, errorResId = R.drawable.ic_bitmap).into(binding.iv6)
        //centerCrop（不会拉伸）、圆形图片、正常显示
        GlideUtil.loadUrl(
            binding.iv7, imageUrl, CircleCrop(),
            R.drawable.ic_bitmap, R.drawable.ic_bitmap
        ).into(binding.iv7)
        //centerCrop（不会拉伸）、圆形图片、显示本地图片
        GlideUtil.showImageFromResource(binding.iv8, R.drawable.ic_bitmap, CircleCrop())
        //获取Bitmap
        GlideUtil.loadUrlAsBitmap(this, imageUrl) {
            binding.iv9.setImageBitmap(it)
        }
    }

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityTestGlideBinding.inflate(layoutInflater)

}