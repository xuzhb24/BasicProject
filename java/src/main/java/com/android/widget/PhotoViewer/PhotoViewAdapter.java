package com.android.widget.PhotoViewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.android.java.R;
import com.android.widget.PhotoViewer.widget.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

/**
 * Created by xuzhb on 2021/1/13
 * Desc:查看多张图片对应的Adapter
 */
public class PhotoViewAdapter extends PagerAdapter {

    private Activity mActivity;
    private List<String> mImageUrlList;

    public PhotoViewAdapter(Activity activity, @NonNull List<String> imageUrlList) {
        this.mActivity = activity;
        this.mImageUrlList = imageUrlList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.item_photo_view_pager, container, false);
        ProgressBar progressBar = layout.findViewById(R.id.progress_bar);
        PhotoView photoView = layout.findViewById(R.id.photo_view);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(mActivity).asBitmap().load(mImageUrlList.get(position))
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        photoView.setImageBitmap(resource);
                        photoView.setZoomable(true);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        photoView.setImageResource(R.drawable.ic_photo_load_fail);
                        photoView.setZoomable(false);
                        progressBar.setVisibility(View.GONE);
                    }

                });
        photoView.setOnClickListener(v -> mActivity.finish());
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public static boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

}
