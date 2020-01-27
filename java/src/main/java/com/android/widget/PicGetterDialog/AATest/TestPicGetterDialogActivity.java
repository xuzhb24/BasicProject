package com.android.widget.PicGetterDialog.AATest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.SizeUtil;
import com.android.widget.PicGetterDialog.OnPicGetterListener;
import com.android.widget.PicGetterDialog.PicGetterDialog;
import com.android.widget.TitleBar;
import com.yalantis.ucrop.UCrop;

/**
 * Created by xuzhb on 2020/1/26
 * Desc:
 */
public class TestPicGetterDialogActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.pic_tv)
    TextView picTv;
    @BindView(R.id.pic_iv)
    ImageView picIv;

    private PicGetterDialog mPicGetterDialog;

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(v -> {
            finish();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_pic_getter_dialog;
    }

    @OnClick({R.id.dialog_btn1, R.id.dialog_btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn1:
                showPicGetDialog();
                break;
            case R.id.dialog_btn2:
                showCustomPicGetDialog();
                break;
        }
    }

    private void showPicGetDialog() {
        if (mPicGetterDialog != null && mPicGetterDialog.getDialog() != null &&
                mPicGetterDialog.getDialog().isShowing()) {
            return;
        }
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        mPicGetterDialog = PicGetterDialog.newInstance();
        mPicGetterDialog.setAnimationStyle(R.style.AnimUp)
                .setCropOptions(options)
                .setMaxCropSize(800, 2400)
                .setOnPicGetterListener(new OnPicGetterListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap, String picPath) {
                        picIv.setImageBitmap(bitmap);
                        picTv.setText(picPath);
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
                        picIv.setImageBitmap(bitmap);
                        picTv.setText(picPath);
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
