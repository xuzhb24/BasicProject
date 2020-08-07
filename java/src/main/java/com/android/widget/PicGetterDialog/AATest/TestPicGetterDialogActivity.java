package com.android.widget.PicGetterDialog.AATest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestPicGetterDialogBinding;
import com.android.util.DateUtil;
import com.android.util.IntentUtil;
import com.android.util.SizeUtil;
import com.android.widget.PicGetterDialog.OnPicGetterListener;
import com.android.widget.PicGetterDialog.PicGetterDialog;
import com.yalantis.ucrop.UCrop;

/**
 * Created by xuzhb on 2020/1/26
 * Desc:
 */
public class TestPicGetterDialogActivity extends BaseActivity<ActivityTestPicGetterDialogBinding> {

    private PicGetterDialog mPicGetterDialog;
    private String mPicPath;

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        binding.dialogBtn1.setOnClickListener(v -> showPicGetDialog());
        binding.dialogBtn2.setOnClickListener(v -> showCustomPicGetDialog());
        binding.dialogBtn3.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mPicPath)) {
                showToast("请先选择或拍一张照片");
            } else {
                String content = "分享于" + DateUtil.getCurrentDateTime();
                String authority = getApplicationInfo().packageName + ".provider";
                startActivity(IntentUtil.getShareImageIntent(this, content, mPicPath, authority));
            }
        });
    }

    @Override
    public ActivityTestPicGetterDialogBinding getViewBinding() {
        return ActivityTestPicGetterDialogBinding.inflate(getLayoutInflater());
    }

    private void showPicGetDialog() {
        if (mPicGetterDialog != null && mPicGetterDialog.getDialog() != null &&
                mPicGetterDialog.getDialog().isShowing()) {
            return;
        }
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("裁剪图片");
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        mPicGetterDialog = PicGetterDialog.newInstance();
        mPicGetterDialog.setAnimationStyle(R.style.AnimTranslateBottom)
                .setCropOptions(options)
                .setMaxCropSize(800, 2400)
                .setOnPicGetterListener(new OnPicGetterListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap, String picPath) {
                        mPicPath = picPath;
                        binding.picIv.setImageBitmap(bitmap);
                        binding.picTv.setText(picPath);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        showToast(errorMsg);
                    }

                    @Override
                    public void onCancel() {
                        showToast("用户取消了弹窗");
                    }
                });
        mPicGetterDialog.show(getSupportFragmentManager());
    }

    private void showCustomPicGetDialog() {
        CustomPicGetterDialog dialog = CustomPicGetterDialog.newInstance();
        dialog.setViewParams((int) SizeUtil.dp2px(315), ViewGroup.MarginLayoutParams.WRAP_CONTENT)
                .setOnPicGetterListener(new OnPicGetterListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap, String picPath) {
                        mPicPath = picPath;
                        binding.picIv.setImageBitmap(bitmap);
                        binding.picTv.setText(picPath);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        showToast(errorMsg);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
        dialog.showAtCenter(getSupportFragmentManager());
    }

}
