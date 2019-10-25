package com.android.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.application.BaseApplication;
import com.android.java.R;

/**
 * Created by xuzhb on 2019/10/22
 * Desc:自定义Toast
 */
public class ToastUtil {

    public static void showToast(String text) {
        showToast(text, BaseApplication.instance.getApplicationContext(), false, false);
    }

    public static void showToast(String text, Context context, boolean longToast, boolean isCenter) {
        int duration = Toast.LENGTH_SHORT;
        if (longToast) {
            duration = Toast.LENGTH_LONG;
        }
        Toast toast = Toast.makeText(context.getApplicationContext(), text, duration);
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_toast, null);
        TextView toastTv = layout.findViewById(R.id.toast_tv);
        toastTv.setText(text);
        layout.setBackground(DrawableUtil.createSolidShape(SizeUtil.dp2px(3), Color.parseColor("#CC1F2735")));
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setView(layout);
        toast.show();
    }

    public static void toast(String text) {
        Toast.makeText(BaseApplication.instance.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(String text) {
        Toast.makeText(BaseApplication.instance.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}
