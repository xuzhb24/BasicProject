package com.android.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.util.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by xuzhb on 2020/11/17
 * Desc:Glide工具类
 */
public class GlideUtil {

    //显示网络图片
    public static void showImageFromUrl(ImageView iv, String url, BitmapTransformation transformation, @DrawableRes int placeResId, @DrawableRes int errorResId) {
        loadUrl(iv, url, transformation, placeResId, errorResId).into(iv);
    }

    //显示本地图片
    public static void showImageFromResource(ImageView iv, @DrawableRes int resId, BitmapTransformation transformation) {
        loadResource(iv, resId, transformation).into(iv);
    }

    //加载网络图片
    public static RequestBuilder<Drawable> loadUrl(ImageView iv, String url) {
        return loadUrl(iv, url, null, -1, -1);
    }

    /**
     * 加载网络图片
     *
     * @param url            图片链接
     * @param transformation 图片转换，CircleCrop：圆形图片，RoundedCorners：圆角图片，SectionRoundedCorners：指定部分圆角图片
     * @param placeResId     占位图
     * @param errorResId     错误图
     */
    public static RequestBuilder<Drawable> loadUrl(ImageView iv, String url, BitmapTransformation transformation, @DrawableRes int placeResId, @DrawableRes int errorResId) {
        RequestBuilder<Drawable> result = Glide.with(iv).load(url);
        if (iv.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            if (transformation != null) {
                result.transform(new CenterCrop(), transformation);
            } else {
                result.transform(new CenterCrop());
            }
        } else {
            if (transformation != null) {
                result.transform(transformation);
            }
        }
        if (placeResId != -1) {
            if (transformation != null) {
                result.thumbnail(loadResource(iv, placeResId, transformation));  //占位图图片转换
            } else {
                result.placeholder(placeResId);
            }
        }
        if (errorResId != -1) {
            if (transformation != null) {
                result.thumbnail(loadResource(iv, errorResId, transformation)); //错误图图片转换
            } else {
                result.error(errorResId);
            }
        }
        return result;
    }

    /**
     * 加载本地图片
     *
     * @param resId          本地图片
     * @param transformation 图片转换，CircleCrop：圆形图片，RoundedCorners：圆角图片，SectionRoundedCorners：指定部分圆角图片
     */
    public static RequestBuilder<Drawable> loadResource(ImageView iv, @DrawableRes int resId, BitmapTransformation transformation) {
        if (iv.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            if (transformation != null) {
                return Glide.with(iv).load(resId).transform(new CenterCrop(), transformation);
            } else {
                return Glide.with(iv).load(resId).transform(new CenterCrop());
            }
        } else {
            if (transformation != null) {
                return Glide.with(iv).load(resId).transform(transformation);
            } else {
                return Glide.with(iv).load(resId);
            }
        }
    }

    //加载网络图片并转换成Bitmap
    public static void loadUrlAsBitmap(Context context, String url, BitmapCallback callback) {
        Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                LogUtil.i("Bitmap Size", resource.getWidth() + " x " + resource.getHeight());
                callback.onResourceReady(resource);
            }
        });
    }

    public interface BitmapCallback {
        void onResourceReady(Bitmap resource);
    }

}
