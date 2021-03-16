package com.android.widget.FloatWindow.NeedPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2021/3/9
 * Desc:申请悬浮窗权限
 */
public class FloatActivity extends Activity {

    private static final int REQUEST_CODE = 756232212;
    private static List<OnPermissionListener> mOnPermissionListenerList;
    private static OnPermissionListener mOnPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAlertWindowPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (FloatPermissionUtil.hasPermissionOnActivityResult(this)) {
                mOnPermissionListener.onSuccess();
            } else {
                mOnPermissionListener.onFailure();
            }
        }
        finish();
    }

    static synchronized void request(Context context, OnPermissionListener permissionListener) {
        if (FloatPermissionUtil.hasPermission(context)) {
            permissionListener.onSuccess();
            return;
        }
        if (mOnPermissionListenerList == null || mOnPermissionListenerList.isEmpty()) {
            mOnPermissionListenerList = new ArrayList<>();
            mOnPermissionListener = new OnPermissionListener() {
                @Override
                public void onSuccess() {
                    for (OnPermissionListener listener : mOnPermissionListenerList) {
                        listener.onSuccess();
                    }
                    mOnPermissionListenerList.clear();
                }

                @Override
                public void onFailure() {
                    for (OnPermissionListener listener : mOnPermissionListenerList) {
                        listener.onFailure();
                    }
                    mOnPermissionListenerList.clear();
                }
            };
            Intent intent = new Intent(context, FloatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        mOnPermissionListenerList.add(permissionListener);
    }

}
