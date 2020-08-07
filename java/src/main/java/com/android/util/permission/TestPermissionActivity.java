package com.android.util.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.AlertDialogUtil;
import com.android.util.CommonLayoutUtil;
import com.android.util.LogUtil;

import java.util.Arrays;

/**
 * Created by xuzhb on 2020/6/1
 * Desc:
 */
public class TestPermissionActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    private static final String TAG = "PermissionTAG";
    private static final String[] REQUEST_PERMISSION = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_SETTINGS_CODE = 1111;

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "权限工具", "申请权限", "应用设置");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            //申请权限
            if (PermissionUtil.requestPermissions(this, REQUEST_PERMISSION_CODE, REQUEST_PERMISSION)) {
                executeNextLogic();
            }
        });
        binding.btn2.setOnClickListener(v -> {
            //打开应用设置页面
            PermissionUtil.openSettings(this, getPackageName(), 2222);
        });
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //处理权限申请结果
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtil.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                executeNextLogic();
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                LogUtil.e(TAG, "onPermissionDenied：" + Arrays.toString(deniedPermissions));
                LogUtil.e(TAG, "isPermissionDenied：" + PermissionUtil.isPermissionDenied(TestPermissionActivity.this, REQUEST_PERMISSION));
                LogUtil.e(TAG, "isPermissionDeniedForever：" + PermissionUtil.isPermissionDeniedForever(TestPermissionActivity.this, REQUEST_PERMISSION));
                AlertDialogUtil.showDialog(TestPermissionActivity.this, "权限申请",
                        "请授予应用相应的权限，否则app可能无法正常工作", false, (dialog, which) -> {
                            PermissionUtil.requestPermissions(TestPermissionActivity.this,
                                    REQUEST_PERMISSION_CODE, REQUEST_PERMISSION);
                        }, (dialog, which) -> showToast("权限申请失败！"));

            }

            @Override
            public void onPermissionDeniedForever(String[] deniedForeverPermissions) {
                LogUtil.e(TAG, "onPermissionDeniedForever：" + Arrays.toString(deniedForeverPermissions));
                LogUtil.e(TAG, "isPermissionDenied：" + PermissionUtil.isPermissionDenied(TestPermissionActivity.this, REQUEST_PERMISSION));
                LogUtil.e(TAG, "isPermissionDeniedForever：" + PermissionUtil.isPermissionDeniedForever(TestPermissionActivity.this, REQUEST_PERMISSION));
                AlertDialogUtil.showDialog(TestPermissionActivity.this, "权限申请",
                        "请到应用设置页面-权限中开启相应权限，保证app的正常使用", false, "去设置", "取消",
                        (dialog, which) -> {
                            PermissionUtil.openSettings(TestPermissionActivity.this, getPackageName(), REQUEST_SETTINGS_CODE);
                        }, (dialog, which) -> showToast("权限申请失败！"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTINGS_CODE) {
            //是否已申请所有权限
            if (PermissionUtil.isPermissionGranted(this, REQUEST_PERMISSION)) {
                executeNextLogic();
            } else {
                showToast("权限开启失败！");
            }
        }
    }

    private void executeNextLogic() {
        showToast("权限申请成功，执行接下来的逻辑！");
    }

}
