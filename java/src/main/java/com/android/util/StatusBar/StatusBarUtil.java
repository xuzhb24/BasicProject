package com.android.util.StatusBar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:实现沉浸式状态栏，只支持4.4（API 19）以上，Android 4.4才支持状态栏，6.0才支持修改状态栏字体和图标颜色
 */
public class StatusBarUtil {

    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final float DEFAULT_ALPHA = 0f;  //默认透明度为0，0：完全透明 1：完全不透明
    private static final int MIN_API = 19;

    //判断是否是Flyme4（魅族4）以上
    private static boolean isFlyme4Later() {
        return Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }

    //判断是否是MIUI6（小米6）以上
    private static boolean isMIUI6Later() {
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", String.class);
            String versionName = (String) method.invoke(null, "ro.miui.ui.version.name");
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(versionName);
            if (m.find()) {
                return Integer.parseInt(m.group()) >= 6;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //设置状态栏样式和paddingTop
    public static void darkModeAndPadding(Activity activity, View view) {
        darkModeAndPadding(activity, view, DEFAULT_COLOR, DEFAULT_ALPHA, true);
    }

    /**
     * 设置状态栏样式
     *
     * @param activity：对应的Activity
     * @param view：要增加paddingTop的View
     * @param color：状态栏颜色
     * @param alpha：状态栏颜色的透明度
     * @param dark：是否设置状态栏字体及图标颜色为黑色，true为黑色，false为白色，目前只支持MIUI6以上、Flyme4以上以及Android M以上
     * @return
     */
    public static void darkModeAndPadding(
            Activity activity,
            View view,
            int color,
            @FloatRange(from = 0.0, to = 1.0) float alpha,
            boolean dark
    ) {
        darkMode(activity, color, alpha, dark);
        setPadding(activity.getApplicationContext(), view);
    }

    //设置状态栏样式
    public static void darkMode(Activity activity) {
        darkMode(activity, DEFAULT_COLOR, DEFAULT_ALPHA, true);
    }

    /**
     * 设置状态栏样式
     *
     * @param activity：对应的Activity
     * @param color：状态栏颜色
     * @param alpha：状态栏颜色的透明度
     * @param dark：是否设置状态栏字体及图标颜色为黑色，true为黑色，false为白色，目前只支持MIUI6以上、Flyme4以上以及Android M以上
     * @return
     */
    public static void darkMode(
            Activity activity,
            int color,
            @FloatRange(from = 0.0, to = 1.0) float alpha,
            boolean dark
    ) {
        darkMode(activity.getWindow(), color, alpha, dark);
    }

    /**
     * 设置状态栏样式
     *
     * @param window：对应的窗口window
     * @param color：状态栏颜色
     * @param alpha：状态栏颜色的透明度
     * @param dark：是否设置状态栏字体及图标颜色为黑色，true为黑色，false为白色，目前只支持MIUI6以上、Flyme4以上以及Android M以上
     * @return
     */
    public static void darkMode(
            Window window,
            int color,
            @FloatRange(from = 0.0, to = 1.0) float alpha,
            boolean dark
    ) {
        if (isFlyme4Later()) {
            darkModeForFlyme4(window, dark);
            immersive(window, color, alpha);
        } else if (isMIUI6Later()) {
            darkModeForMIUI6(window, dark);
            immersive(window, color, alpha);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            darkModeForM(window, dark);
            immersive(window, color, alpha);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        } else {
            immersive(window, color, alpha);
        }
    }

    //Flyme4以上设置状态栏字体和图标颜色，dark为true时为黑色，false为白色
    private static boolean darkModeForFlyme4(Window window, boolean dark) {
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //MIUI6以上设置状态栏字体和图标颜色，dark为true时为黑色，false为白色
    private static boolean darkModeForMIUI6(Window window, boolean dark) {
        try {
            Class clazz = window.getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                if (dark) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //android M（6.0）以上设置状态栏字体和图标颜色，dark为true时为黑色，false为白色
    @RequiresApi(Build.VERSION_CODES.M)
    private static void darkModeForM(Window window, boolean dark) {
        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        if (dark) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    //设置状态栏透明且状态栏高度为0
    private static void immersive(Window window, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mixtureColor(color, alpha));

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        } else {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }

    }

    //创建假的透明栏
    private static void setTranslucentView(ViewGroup container, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            int mixtureColor = mixtureColor(color, alpha);
            View translucentView = container.findViewById(android.R.id.custom);
            if (translucentView == null && mixtureColor != 0) {
                translucentView = new View(container.getContext());
                translucentView.setId(android.R.id.custom);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(container.getContext()));
                container.addView(translucentView, lp);
            }
            if (translucentView != null) {
                translucentView.setBackgroundColor(mixtureColor);
            }
        }
    }

    private static int mixtureColor(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        int a = (color & -16777216) == 0 ? 255 : color >>> 24;
        return color & 16777215 | (int) ((float) a * alpha) << 24;
    }

    //增加View的paddingTop,增加的值为状态栏高度
    public static void setPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    //设置View的paddingTop,增加的值为状态栏高度
    public static void setPaddingTop(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            view.setPadding(0, getStatusBarHeight(context), 0, 0);
        }
    }

    //增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
    public static void setPaddingSmart(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context);  //增高
            }
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    //增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
    public static void setHeightAndPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);  //增高
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    //增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
    public static void setMargin(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.topMargin += getStatusBarHeight(context);  //增高
            view.setLayoutParams(lp);
        }
    }

    //通过反射获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    (float) result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }

    //隐藏导航栏，并且全屏
    //SystemUI Flag介绍：https://www.jianshu.com/p/e6656707f56c
    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  //隐藏导航栏
                    | View.SYSTEM_UI_FLAG_FULLSCREEN  //隐藏状态栏且全屏展示
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            activity.getWindow().getDecorView().setSystemUiVisibility(options);
        }
    }

    //设置导航栏和状态栏透明
    public static void setNavigationBarStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            int options = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            activity.getWindow().getDecorView().setSystemUiVisibility(options);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
