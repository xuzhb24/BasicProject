package com.android.util;

import com.android.base.BaseApplication;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:尺寸信息单位转换
 */
public class SizeUtil {

    public static float px2dp(float pxValue) {
        float density = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return pxValue / density + 0.5f;
    }

    public static float dp2px(float dpValue) {
        float density = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }

    public static float px2sp(float pxValue) {
        float scaledDensity = BaseApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return pxValue / scaledDensity + 0.5f;
    }

    public static float sp2px(float spValue) {
        float scaledDensity = BaseApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scaledDensity + 0.5f;
    }

}
