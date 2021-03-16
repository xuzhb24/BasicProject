package com.android.widget.FloatWindow.NeedPermission;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:选择以屏幕宽度或屏幕高度为基准
 */
public class ScreenType {
    public static final int width = 0;
    public static final int height = 1;

    @IntDef({width, height})
    @Retention(RetentionPolicy.SOURCE)
    @interface screenType {
    }
}
