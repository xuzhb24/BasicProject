package com.android.widget.PopupWindow;

import android.app.Activity;
import android.support.annotation.FloatRange;
import android.view.Window;
import android.view.WindowManager;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:Window辅助类，实现背景灰色效果
 */
public class WindowHelper {

    private Activity mActivity;

    public WindowHelper(Activity activity) {
        this.mActivity = activity;
    }

    //设置外部区域背景透明度，0：完全不透明，1：完全透明
    public void setBackGroundAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }

}
