package com.android.util;

import android.widget.Toast;
import com.android.application.BaseApplication;

/**
 * Created by xuzhb on 2019/10/22
 * Desc:自定义Toast
 */
public class ToastUtil {

    public static void showToast(String text) {
        Toast.makeText(BaseApplication.instance, text, Toast.LENGTH_SHORT).show();
    }

}
