package com.android.util.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.android.util.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Created by xuzhb on 2020/10/21
 * Desc:Glide工具类
 */
object GlideUtil {

    //显示网络图片
    fun showImageFromUrl(
        iv: ImageView,
        url: String,
        transformation: BitmapTransformation? = null,
        @DrawableRes placeResId: Int = -1,
        @DrawableRes errorResId: Int = -1
    ) {
        loadUrl(iv, url, transformation, placeResId, errorResId).into(iv)
    }

    /**
     * 加载网络图片
     * @param url 图片链接
     * @param transformation 图片转换，CircleCrop：圆形图片，RoundedCorners：圆角图片，SectionRoundedCorners：指定部分圆角图片
     * @param placeResId 占位图
     * @param errorResId 错误图
     */
    fun loadUrl(
        iv: ImageView,
        url: String,
        transformation: BitmapTransformation? = null,
        @DrawableRes placeResId: Int = -1,
        @DrawableRes errorResId: Int = -1
    ): RequestBuilder<Drawable> {
        return Glide.with(iv).load(url).apply {
            if (iv.scaleType == ImageView.ScaleType.CENTER_CROP) {
                if (transformation != null) {
                    transform(CenterCrop(), transformation)
                } else {
                    transform(CenterCrop())
                }
            } else {
                if (transformation != null) {
                    transform(transformation)
                }
            }
            if (placeResId != -1) {
                if (transformation != null) {
                    thumbnail(loadResource(iv, placeResId, transformation))  //占位图图片转换
                } else {
                    placeholder(placeResId)
                }
            }
            if (errorResId != -1) {
                if (transformation != null) {
                    thumbnail(loadResource(iv, errorResId, transformation))  //错误图图片转换
                } else {
                    error(errorResId)
                }
            }
        }
    }

    //显示本地图片
    fun showImageFromResource(
        iv: ImageView,
        @DrawableRes resId: Int,
        transformation: BitmapTransformation?
    ) {
        loadResource(iv, resId, transformation).into(iv)
    }

    /**
     * 加载本地图片
     * @param resId 本地图片
     * @param transformation 图片转换，CircleCrop：圆形图片，RoundedCorners：圆角图片，SectionRoundedCorners：指定部分圆角图片
     */
    fun loadResource(
        iv: ImageView,
        @DrawableRes resId: Int,
        transformation: BitmapTransformation?
    ): RequestBuilder<Drawable> {
        return if (iv.scaleType == ImageView.ScaleType.CENTER_CROP) {  //解决Glide和ImageView自身的CENTER_CROP冲突
            if (transformation != null) {
                Glide.with(iv).load(resId).transform(CenterCrop(), transformation)
            } else {
                Glide.with(iv).load(resId).transform(CenterCrop())
            }
        } else {
            if (transformation != null) {
                Glide.with(iv).load(resId).transform(transformation)
            } else {
                Glide.with(iv).load(resId)
            }
        }
    }

    //加载网络图片并转换成Bitmap
    fun loadUrlAsBitmap(
        context: Context,
        url: String,
        method: (resource: Bitmap) -> Unit
    ) {
        Glide.with(context).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                LogUtil.i("Bitmap Size", "${resource.width} x ${resource.height}")
                method(resource)
            }
        })
    }

    //加载网络图片并转换成drawable
    fun loadUrlAsDrawable(
        context: Context,
        url: String,
        method: (resource: Drawable) -> Unit
    ) {
        Glide.with(context).load(url).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                method(resource)
            }
        })
    }

}