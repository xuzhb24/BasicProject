package com.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.java.R;
import com.android.util.uiparse.ParseUtil;
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

    public static void parseActivity(AppCompatActivity activity, MotionEvent ev) {
        ParseUtil.showTopActivityInfo(activity, ev);
    }

}
