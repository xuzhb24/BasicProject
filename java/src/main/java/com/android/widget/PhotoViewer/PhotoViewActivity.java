package com.android.widget.PhotoViewer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.java.R;
import com.android.java.databinding.ActivityPhotoViewBinding;
import com.android.util.StatusBar.StatusBarUtil;
import com.android.util.ToastUtil;
import com.android.util.bitmap.BitmapUtil;
import com.android.util.permission.PermissionUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuzhb on 2021/1/6
 * Desc:图片查看器Activity
 */
public class PhotoViewActivity extends AppCompatActivity {

    private static final String[] REQUEST_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST";
    private static final String EXTRA_IMAGE_POSITION = "EXTRA_IMAGE_POSITION";

    public static void start(Activity activity, String[] imageUrlArray, int position) {
        if (imageUrlArray == null || imageUrlArray.length == 0) {
            return;
        }
        start(activity, new ArrayList<>(Arrays.asList(imageUrlArray)), position);
    }

    public static void start(Activity activity, ArrayList<String> imageUrlList, int position) {
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            return;
        }
        Intent intent = new Intent(activity, PhotoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_IMAGE_URL_LIST, imageUrlList);
        bundle.putInt(EXTRA_IMAGE_POSITION, position);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.scale_center_in, R.anim.alpha_out_300);  //放大进入Activity
    }

    private ActivityPhotoViewBinding binding;
    private List<String> mImageUrlList;
    private int mCurrentPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.darkModeAndPadding(this, binding.rootFl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
        mImageUrlList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URL_LIST);
        mCurrentPosition = getIntent().getIntExtra(EXTRA_IMAGE_POSITION, -1);
        initPhoto(mImageUrlList);
        //关闭
        binding.closeIv.setOnClickListener(v -> finish());
        //下载
        binding.downloadIv.setOnClickListener(v -> {
            if (mCurrentPosition != -1) {
                downloadPicture(mImageUrlList.get(mCurrentPosition));
            }
        });
    }

    private void initPhoto(List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            binding.downloadIv.setVisibility(View.GONE);
            return;
        }
        if (mCurrentPosition < 0 || mCurrentPosition >= imageUrlList.size()) {
            mCurrentPosition = 0;
        }
        binding.indicatorTv.setText((mCurrentPosition + 1) + "/" + imageUrlList.size());
        binding.photoVp.setAdapter(new PhotoViewAdapter(this, imageUrlList));
        binding.photoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                binding.indicatorTv.setText((position + 1) + "/" + imageUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.photoVp.setOffscreenPageLimit(imageUrlList.size());
        binding.photoVp.setCurrentItem(mCurrentPosition, false);
    }

    //下载并保存图片
    private void downloadPicture(String url) {
        Glide.with(this).load(url).into(new CustomTarget<Drawable>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                ToastUtil.showToast("下载失败");
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                BitmapUtil.saveBitmapToGallery(PhotoViewActivity.this, bitmap, System.currentTimeMillis() + "");
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtil.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (mCurrentPosition != -1) {
                    downloadPicture(mImageUrlList.get(mCurrentPosition));
                }
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                ToastUtil.showToast("请先允许权限");
            }

            @Override
            public void onPermissionDeniedForever(String[] deniedForeverPermissions) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.alpha_in_300, R.anim.scale_center_out);  //缩小退出Activity
    }

}
