package com.android.util.app.AATest

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.ViewGroup
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestAdapterBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.IntentUtil
import com.android.util.LogUtil
import com.android.util.SizeUtil
import com.android.util.alert
import com.android.util.app.AppInfo
import com.android.util.app.AppUtil
import com.android.widget.dialog.CommonDialog
import kotlin.concurrent.thread

/**
 * Created by xuzhb on 2020/12/3
 * Desc:
 */
class TestAppListActivity : BaseActivity<ActivityTestAdapterBinding>() {

    private val mAdapter by lazy { AppAdapter(this, mutableListOf()) }

    override fun handleView(savedInstanceState: Bundle?) {
        mTitleBar?.titleText = "已安装应用列表"
        binding.srl.isEnabled = false
        showLoadingDialog("正在加载...", true)
        binding.rv.adapter = mAdapter
        thread(start = true) {
            val list = AppUtil.getAppInfoList(this)
            runOnUiThread {
                loadFinish(false)
                mAdapter.setData(list)
            }
        }
    }

    override fun initListener() {
        mAdapter.setOnItemClickListener { obj, position ->
            val appInfo = obj as AppInfo
            CommonDialog.newInstance()
                .setLayoutId(R.layout.dialog_app_detail)
                .setOnViewListener { holder, dialog ->
                    //应用详情
                    holder.setOnClickListener(R.id.detail_btn) {
                        dialog?.dismiss()
                        alert(this, getAppInfo(appInfo.packageName!!))
                    }
                    //打开应用
                    if (IntentUtil.getLaunchAppIntent(this, appInfo.packageName!!) != null) {
                        holder.setViewVisible(R.id.open_btn)
                    } else {
                        holder.setViewGone(R.id.open_btn)
                    }
                    holder.setOnClickListener(R.id.open_btn) {
                        dialog?.dismiss()
                        AppUtil.openApp(this, appInfo.packageName)
                    }
                    //打开应用设置页面
                    holder.setOnClickListener(R.id.settings_btn) {
                        dialog?.dismiss()
                        AppUtil.openAppSettings(this, appInfo.packageName)
                    }
                }
                .setViewParams(SizeUtil.dp2pxInt(315f), ViewGroup.LayoutParams.WRAP_CONTENT)
                .show(supportFragmentManager)
        }
    }

    override fun getViewBinding() = ActivityTestAdapterBinding.inflate(layoutInflater)

    private fun getAppInfo(packageName: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append("应用图标： ")
        val icon = AppUtil.getAppIcon(this, packageName)
        if (icon != null) {
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            val span = ImageSpan(icon)
            builder.setSpan(span, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        builder.append("\n应用名称：")
            .append(AppUtil.getAppLabel(this, packageName))
            .append("\n安装路径：\n")
            .append(AppUtil.getAppPath(this, packageName))
            .append("\nversionName：")
            .append(AppUtil.getAppVersionName(this, packageName))
            .append("\nversionCode：")
            .append(AppUtil.getAppVersionCode(this, packageName).toString())
            .append("\n是否系统应用：")
            .append(AppUtil.isSystemApp(this, packageName).toString())
            .append("\n是否是Debug版本：")
            .append(AppUtil.isAppDebug(this, packageName).toString())
            .append("\nMD5值：\n")
            .append(AppUtil.getAppSignatureMD5(this, packageName))
            .append("\nSHA1值：\n")
            .append(AppUtil.getAppSignatureSHA1(this, packageName))
        LogUtil.e("AppInfo", " \n${AppUtil.getAppInfo(this, packageName)}")
        return builder
    }

}