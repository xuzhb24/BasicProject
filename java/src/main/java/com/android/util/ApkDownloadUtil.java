package com.android.util;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuzhb on 2020/7/11
 * Desc:APK下载工具类
 */
public class ApkDownloadUtil {

    private Context mContext;
    private long mDownloadID;
    private DownloadManager mDownManager;
    private DownLoadCompleteReceiver mReceiver;
    private boolean showProgress = true;
    private ProgressDialog mProgressDialog;
    private ScheduledExecutorService mExecutorService;

    public ApkDownloadUtil(Context context) {
        this.mContext = context;
    }

    //下载Apk
    public void downLoadApk(final String url) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
            mReceiver = new DownLoadCompleteReceiver();
            mContext.registerReceiver(mReceiver, filter);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //设置通知栏标题
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle("下载");
            request.setDescription("正在下载");
            request.setAllowedOverRoaming(false);
            //设置下载路径，如"/storage/emulated/0/Android/data/com.android.java/files/Download/xxx.apk"
            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "basicproject.apk");
            mDownManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            mDownloadID = mDownManager.enqueue(request);  //开始下载
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMessage("下载中，请稍后...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mExecutorService = Executors.newSingleThreadScheduledExecutor();
            mExecutorService.scheduleAtFixedRate(() -> queryProgressById(mDownloadID),
                    0, 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询下载进度
    private void queryProgressById(long downloadID) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        Cursor cursor = mDownManager.query(query);
        String progress = "0";
        String totalSize = "0";
        if (cursor.moveToNext()) {
            progress = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            totalSize = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        }
        cursor.close();
        if (showProgress && mProgressDialog != null) {
            mProgressDialog.setMax(Integer.parseInt(totalSize));
            mProgressDialog.setProgress(Integer.parseInt(progress));
        }
    }

    //监听下载进度
    private class DownLoadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {  //下载完成
                try {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (mDownloadID == id) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(id);
                        Cursor cursor = mDownManager.query(query);
                        String fileName;
                        if (cursor.moveToNext()) {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                fileName = Uri.parse(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))).getPath();
                            } else {
                                fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            }
                            installApk(fileName);
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }
    }

    //apk安装
    private void installApk(String path) {
        try {
            if (showProgress && mProgressDialog != null) {
                mProgressDialog.dismiss();
            } else {
                Toast.makeText(mContext.getApplicationContext(), "下载成功，正在安装", Toast.LENGTH_SHORT).show();
            }
            mExecutorService.shutdown();
            mContext.unregisterReceiver(mReceiver);
            String authority = mContext.getApplicationInfo().packageName + ".provider";  //对应android:authorities属性值
            mContext.startActivity(IntentUtil.getInstallAppIntent(mContext, path, authority));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
