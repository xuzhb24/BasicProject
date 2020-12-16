package com.android.util

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by xuzhb on 2020/12/16
 * Desc:APK下载工具类
 */
class ApkDownloadUtil constructor(private val mContext: Context) {

    private var mDownloadID: Long = 0
    private var mDownManager: DownloadManager? = null
    private var mReceiver: DownLoadCompleteReceiver? = null
    private val showProgress = true
    private var mProgressDialog: ProgressDialog? = null
    private var mExecutorService: ScheduledExecutorService? = null

    //下载Apk
    fun downLoadApk(url: String?) {
        if (url.isNullOrBlank()) {
            return
        }
        try {
            val filter = IntentFilter()
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            mReceiver = DownLoadCompleteReceiver()
            mContext.registerReceiver(mReceiver, filter)
            val request = DownloadManager.Request(Uri.parse(url))
            //设置通知栏标题
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setTitle("下载")
            request.setDescription("正在下载")
            request.setAllowedOverRoaming(false)
            //设置下载路径，如"/storage/emulated/0/Android/data/com.android.java/files/Download/xxx.apk"
            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "basicproject.apk")
            mDownManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            mDownloadID = mDownManager!!.enqueue(request) //开始下载
            mProgressDialog = ProgressDialog(mContext)
            mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            mProgressDialog!!.setMessage("下载中，请稍后...")
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
            mExecutorService = Executors.newSingleThreadScheduledExecutor()
            mExecutorService!!.scheduleAtFixedRate(
                Runnable { queryProgressById(mDownloadID) },
                0, 1, TimeUnit.SECONDS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //查询下载进度
    private fun queryProgressById(downloadID: Long) {
        val query = DownloadManager.Query()
        query.setFilterById(downloadID)
        var progress = "0"
        var totalSize = "0"
        mDownManager?.query(query)?.let {
            if (it.moveToNext()) {
                progress = it.getString(it.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                totalSize = it.getString(it.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            }
            it.close()
        }
        if (showProgress && mProgressDialog != null) {
            mProgressDialog!!.max = totalSize.toInt()
            mProgressDialog!!.progress = progress.toInt()
        }
    }

    //监听下载进度
    inner class DownLoadCompleteReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {  //下载完成
                try {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (mDownloadID == id) {
                        val query = DownloadManager.Query()
                        query.setFilterById(id)
                        mDownManager?.query(query)?.let {
                            if (it.moveToNext()) {
                                val fileName = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                    Uri.parse(it.getString(it.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))).path
                                } else {
                                    it.getString(it.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
                                }
                                installApk(fileName)
                            }
                            it.close()
                        }
                    }
                } catch (e: Exception) {
                    mProgressDialog?.dismiss()
                    e.printStackTrace()
                }
            }
        }
    }

    //apk安装
    private fun installApk(path: String?) {
        if (path.isNullOrBlank()) {
            return
        }
        try {
            if (showProgress && mProgressDialog != null) {
                mProgressDialog!!.dismiss()
            } else {
                Toast.makeText(mContext.applicationContext, "下载成功，正在安装", Toast.LENGTH_SHORT).show()
            }
            mExecutorService?.shutdown()
            mContext.unregisterReceiver(mReceiver)
            val authority = mContext.applicationInfo.packageName + ".provider" //对应android:authorities属性值
            mContext.startActivity(IntentUtil.getInstallAppIntent(mContext, path, authority))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}