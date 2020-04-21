package com.android.util.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.util.LogUtil;
import com.android.util.ToastUtil;

/**
 * Created by xuzhb on 2020/4/19
 * Desc:
 */
public class TestService extends Service {

    private static final String TAG = "TestService";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.w(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.w(TAG, "onStartCommand");
        ToastUtil.showToast("TestService已开启");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.w(TAG, "onDestroy");
        ToastUtil.showToast("TestService已销毁");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    public String getBindState() {
        return "TestService已绑定";
    }

}
