package com.android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.java.BuildConfig;
import com.android.java.R;
import com.android.widget.PopupWindow.CommonPopupWindow;

import java.util.Random;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:工具类扩展，存放单个方法
 */
public class ExtraUtil {

    public static void main(String[] args) {
        System.out.println("随机5位字符串：" + createRandomKey(5));
    }

    //生成指定长度由大小写字母、数字组成的随机字符串
    public static String createRandomKey(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char s = 0;
            int j = random.nextInt(3) % 4;
            switch (j) {
                case 0:
                    //随机生成数字
                    s = (char) (random.nextInt(57) % (57 - 48 + 1) + 48);
                    break;
                case 1:
                    //随机生成大写字母
                    s = (char) (random.nextInt(90) % (90 - 65 + 1) + 65);
                    break;
                case 2:
                    //随机生成小写字母
                    s = (char) (random.nextInt(122) % (122 - 97 + 1) + 97);
                    break;
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static void alert(Context context, String msg) {
        AlertDialogUtil.showDialog(context, msg);
    }

    public static void alert(Context context, SpannableStringBuilder msg) {
        AlertDialogUtil.showDialog(context, msg);
    }

    public static void showImage(Activity activity, int resId) {
        new CommonPopupWindow.Builder(activity)
                .setContentView(R.layout.layout_image)
                .setViewParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundAlpha(0.5f)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setImageResource(R.id.iv, resId);
                })
                .build()
                .showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    public static void showImage(Activity activity, Bitmap bitmap) {
        new CommonPopupWindow.Builder(activity)
                .setContentView(R.layout.layout_image)
                .setViewParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundAlpha(0.5f)
                .setOnViewListener((holder, popupWindow) -> {
                    ((ImageView) holder.getView(R.id.iv)).setImageBitmap(bitmap);
                })
                .build()
                .showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    public static void showImage(Activity activity, Drawable drawable) {
        new CommonPopupWindow.Builder(activity)
                .setContentView(R.layout.layout_image)
                .setViewParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundAlpha(0.5f)
                .setOnViewListener((holder, popupWindow) -> {
                    ((ImageView) holder.getView(R.id.iv)).setBackground(drawable);
                })
                .build()
                .showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    //屏幕顶部中间区域双击获取当前Activity类名，只在debug环境下有效
    public static void getTopActivityName(Activity activity, MotionEvent ev) {
        if (BuildConfig.DEBUG && ev.getAction() == MotionEvent.ACTION_DOWN &&
                ev.getRawY() < SizeUtil.dp2px(80f) && ev.getRawX() > SizeUtil.dp2px(80f) &&
                ev.getRawX() < ScreenUtil.getScreenWidth(activity) - SizeUtil.dp2px(80f)) {
            CheckFastClickUtil.setOnMultiClickListener(600, clickCount -> {
                if (clickCount == 2) {
                    ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                    ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
                    String packageName = info.topActivity.getPackageName();
                    String className = info.topActivity.getClassName();
                    alert(activity, "包名：" + packageName + "\n类名：" + className);
                }
            });
        }
    }

}
