package com.android.widget.PhotoViewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuzhb on 2021/1/6
 * Desc:图片查看器Activity
 */
public class PhotoViewActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PhotoViewActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.scale_center_in, R.anim.alpha_out_300);  //放大进入Activity
    }

    private ActivityPhotoViewBinding binding;
    private final List<String> mImageUrlList = Arrays.asList(
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1730713693,2130926401&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2202780618,895893289&fm=26&gp=0.jpg",
            "http:sslancvan",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3621987426,796514073&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1833567670,2009341108&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3225163326,3627210682&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3200450391,4154446234&fm=26&gp=0.jpg"
    );
    private int mCurrentPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.darkMode(this, Color.BLACK, 0, false);
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
        mCurrentPosition = 0;
        binding.indicatorTv.setText("1/" + imageUrlList.size());
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
    }

    //下载并保存图片
    private void downloadPicture(String url) {
        Glide.with(this).downloadOnly().load(url).into(new CustomTarget<File>() {
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
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                boolean isSuccess = BitmapUtil.saveImageFileToGallery(getApplicationContext(), resource, System.currentTimeMillis() + "");
                if (isSuccess) {
                    ToastUtil.showToast("保存成功");
                } else {
                    ToastUtil.showToast("保存失败");
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.alpha_in_300, R.anim.scale_center_out);  //缩小退出Activity
    }

}
