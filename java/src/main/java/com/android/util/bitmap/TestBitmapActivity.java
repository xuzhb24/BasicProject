package com.android.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.frame.mvc.viewBinding.BaseActivity_VB;
import com.android.java.R;
import com.android.java.databinding.ActivityTestBitmapBinding;
import com.android.util.LayoutParamsUtil;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2020/5/30
 * Desc:
 */
public class TestBitmapActivity extends BaseActivity_VB<ActivityTestBitmapBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        setImage(BitmapUtil.addTextWatermark(getSrc(), "原图", SizeUtil.sp2px(15f), Color.RED, 10, 10, true));
        //缩放
        setImage(BitmapUtil.scale(getSrc(), 0.5f, 0.5f, true));
        setImage(BitmapUtil.scale(getSrc(), (int) SizeUtil.dp2px(150), (int) SizeUtil.dp2px(150), true));
        //裁剪
        setImage(BitmapUtil.clip(getSrc(), 150, 150, 250, 250, true));
        //倾斜
        setImage(BitmapUtil.skew(getSrc(), 0.2f, 0.2f, 0, 0, true));
        //旋转
        setImage(BitmapUtil.rotate(getSrc(), 90, 100, 100, true));
        //圆形图片
        setImage(BitmapUtil.toCircle(getSrc(), true));
        //圆角图片
        setImage(BitmapUtil.toRoundCorner(getSrc(), SizeUtil.dp2px(20), true));
        //模糊
        setImage(BitmapUtil.fastBlur(getSrc(), 0.8f, 10, true));
        //添加颜色边框
        setImage(BitmapUtil.addFrame(getSrc(), (int) SizeUtil.dp2px(10), Color.GREEN, true));
        //倒影
        setImage(BitmapUtil.addReflection(getSrc(), 250, true));
        //图片水印
        setImage(BitmapUtil.addImageWatermark(getSrc(), BitmapUtil.getBitmapFromResource(getResources(), R.mipmap.ic_logo), 10, 10, 150, true));
        //灰度图片
        setImage(BitmapUtil.toGray(getSrc(), true));
    }

    private void setImage(Bitmap bm) {
        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(params);
        LayoutParamsUtil.setMarginTop(iv, (int) SizeUtil.dp2px(15));
        iv.setImageBitmap(bm);
        binding.rootLl.addView(iv);
    }

    private Bitmap getSrc() {
        return BitmapUtil.getBitmapFromResource(getResources(), R.drawable.ic_bitmap);
    }

    @Override
    public void initListener() {

    }

    @Override
    public ActivityTestBitmapBinding getViewBinding() {
        return ActivityTestBitmapBinding.inflate(getLayoutInflater());
    }
}
