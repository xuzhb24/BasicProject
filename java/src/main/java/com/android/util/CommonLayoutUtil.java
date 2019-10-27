package com.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.java.R;
import com.android.widget.TitleBar;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:
 */
public class CommonLayoutUtil {

    public static void initCommonLayout(Activity activity, String title, String... text) {
        initCommonLayout(activity, title, false, false, text);
    }

    public static void initCommonLayout(Activity activity, String title, boolean showEditText, boolean showTextView, String... text) {
        TitleBar titleBar = (TitleBar) activity.findViewById(R.id.title_bar);
        EditText et = (EditText) activity.findViewById(R.id.et);
        TextView tv = (TextView) activity.findViewById(R.id.tv);
        Button btn1 = (Button) activity.findViewById(R.id.btn1);
        Button btn2 = (Button) activity.findViewById(R.id.btn2);
        Button btn3 = (Button) activity.findViewById(R.id.btn3);
        Button btn4 = (Button) activity.findViewById(R.id.btn4);
        Button btn5 = (Button) activity.findViewById(R.id.btn5);
        Button btn6 = (Button) activity.findViewById(R.id.btn6);
        titleBar.setTitleText(title);
        titleBar.setOnLeftClickListener(new TitleBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                activity.finish();
            }
        });
        et.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);
        if (showEditText) {
            et.setVisibility(View.VISIBLE);
        }
        if (showTextView) {
            tv.setVisibility(View.VISIBLE);
        }
        if (text.length >= 1) {
            btn1.setVisibility(View.VISIBLE);
            btn1.setText(text[0]);
        }
        if (text.length >= 2) {
            btn2.setVisibility(View.VISIBLE);
            btn2.setText(text[1]);
        }
        if (text.length >= 3) {
            btn3.setVisibility(View.VISIBLE);
            btn3.setText(text[2]);
        }
        if (text.length >= 4) {
            btn4.setVisibility(View.VISIBLE);
            btn4.setText(text[3]);
        }
        if (text.length >= 5) {
            btn5.setVisibility(View.VISIBLE);
            btn5.setText(text[4]);
        }
        if (text.length >= 6) {
            btn6.setVisibility(View.VISIBLE);
            btn6.setText(text[5]);
        }
    }

    public static final String MODULE_NAME = "MODULE_NAME";

    public static void jumpToTestUtilActivity(Context context, String moduleName) {
        Intent intent = new Intent();
        intent.setClass(context, TestUtilActivity.class);
        intent.putExtra(MODULE_NAME, moduleName);
        context.startActivity(intent);
    }

}
