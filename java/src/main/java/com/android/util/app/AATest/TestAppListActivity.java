package com.android.util.app.AATest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.frame.mvp.CommonBaseActivity;
import com.android.java.R;
import com.android.util.ExtraUtil;
import com.android.util.IntentUtil;
import com.android.util.LogUtil;
import com.android.util.SizeUtil;
import com.android.util.app.AppInfo;
import com.android.util.app.AppUtil;
import com.android.widget.Dialog.CommonDialog;
import com.android.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xuzhb on 2020/2/5
 * Desc:
 */
public class TestAppListActivity extends CommonBaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private AppAdapter mAdapter;

    @Override
    public void handleView(Bundle savedInstanceState) {
        titleBar.setTitleText("已安装应用列表");
        srl.setEnabled(false);
        showLoading("正在加载...", true);
        mAdapter = new AppAdapter(this, new ArrayList<>());
        rv.setAdapter(mAdapter);
        new Thread(() -> {
            List<AppInfo> list = AppUtil.getAppInfoList(this);
            runOnUiThread(() -> {
                dismissLoading();
                mAdapter.setData(list);
            });
        }).start();
    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(v -> {
            finish();
        });
        mAdapter.setOnItemClickListener(((data, position) -> {
            AppInfo appInfo = (AppInfo) data;
            CommonDialog.newInstance()
                    .setLayoutId(R.layout.dialog_app_detail)
                    .setOnViewListener((holder, dialog) -> {
                        //应用详情
                        holder.setOnClickListener(R.id.detail_btn, v -> {
                            dialog.dismiss();
                            ExtraUtil.alert(this, getAppInfo(appInfo.getPackageName()));
                        });
                        //打开应用
                        if (IntentUtil.getLaunchAppIntent(this, appInfo.getPackageName()) != null) {
                            holder.setViewVisible(R.id.open_btn);
                        } else {
                            holder.setViewGone(R.id.open_btn);
                        }
                        holder.setOnClickListener(R.id.open_btn, v -> {
                            dialog.dismiss();
                            AppUtil.openApp(this, appInfo.getPackageName());
                        });
                        //打开应用设置页面
                        holder.setOnClickListener(R.id.settings_btn, v -> {
                            dialog.dismiss();
                            AppUtil.openAppSettings(this, appInfo.getPackageName());
                        });
                    })
                    .setViewParams((int) SizeUtil.dp2px(315), ViewGroup.LayoutParams.WRAP_CONTENT)
                    .show(getSupportFragmentManager());
        }));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_adapter;
    }

    private SpannableStringBuilder getAppInfo(String packageName) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("应用图标： ");
        Drawable icon = AppUtil.getAppIcon(this, packageName);
        if (icon != null) {
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(icon);
            builder.setSpan(span, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        builder.append("\n应用名称：")
                .append(AppUtil.getAppLabel(this, packageName))
                .append("\n安装路径：\n")
                .append(AppUtil.getAppPath(this, packageName))
                .append("\nversionName：")
                .append(AppUtil.getAppVersionName(this, packageName))
                .append("\nversionCode：")
                .append(String.valueOf(AppUtil.getAppVersionCode(this, packageName)))
                .append("\n是否系统应用：")
                .append(String.valueOf(AppUtil.isSystemApp(this, packageName)))
                .append("\n是否是Debug版本：")
                .append(String.valueOf(AppUtil.isAppDebug(this, packageName)))
                .append("\nMD5值：\n")
                .append(AppUtil.getAppSignatureMD5(this, packageName))
                .append("\nSHA1值：\n")
                .append(AppUtil.getAppSignatureSHA1(this, packageName));
//                    .append("\n是否处于前台：")
//                    .append(String.valueOf(AppUtil.isAppForeground(this,packageName)))
        LogUtil.e("AppInfo", " \n" + AppUtil.getAppInfo(this, packageName));
        return builder;
    }
}
