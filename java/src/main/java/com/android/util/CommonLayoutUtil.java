package com.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.java.R;
import com.android.widget.InputLayout;
import com.android.widget.TitleBar;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:
 */
public class CommonLayoutUtil {

    public static void initCommonLayout(Activity activity, String title, String... text) {
        initCommonLayout(activity, title, false, false, text);
    }

    public static void initCommonLayout(Activity activity, String title, boolean showInputLayout, boolean showTextView, String... text) {
        TitleBar titleBar = activity.findViewById(R.id.title_bar);
        InputLayout il = activity.findViewById(R.id.il);
        TextView tv = activity.findViewById(R.id.tv);
        Button btn1 = activity.findViewById(R.id.btn1);
        Button btn2 = activity.findViewById(R.id.btn2);
        Button btn3 = activity.findViewById(R.id.btn3);
        Button btn4 = activity.findViewById(R.id.btn4);
        Button btn5 = activity.findViewById(R.id.btn5);
        Button btn6 = activity.findViewById(R.id.btn6);
        Button btn7 = activity.findViewById(R.id.btn7);
        Button btn8 = activity.findViewById(R.id.btn8);
        Button btn9 = activity.findViewById(R.id.btn9);
        titleBar.setTitleText(title);
        titleBar.setOnLeftClickListener(v -> {
            activity.finish();
        });
        il.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
        btn6.setVisibility(View.GONE);
        btn7.setVisibility(View.GONE);
        btn8.setVisibility(View.GONE);
        btn9.setVisibility(View.GONE);
        if (showInputLayout) {
            il.setVisibility(View.VISIBLE);
        }
        if (showTextView) {
            tv.setVisibility(View.VISIBLE);
        } else {
            if (text.length > 0) {
                float topMargin = (ScreenUtil.getScreenHeight(activity) - titleBar.getHeight()
                        - text.length * SizeUtil.dp2px(70) - SizeUtil.dp2px(60)) / 2f;
                LayoutParamsUtil.setMarginTop(btn1, topMargin > 0 ? (int) topMargin : (int) SizeUtil.dp2px(10));
            }
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
        if (text.length >= 7) {
            btn7.setVisibility(View.VISIBLE);
            btn7.setText(text[6]);
        }
        if (text.length >= 8) {
            btn8.setVisibility(View.VISIBLE);
            btn8.setText(text[7]);
        }
        if (text.length >= 9) {
            btn9.setVisibility(View.VISIBLE);
            btn9.setText(text[8]);
        }
    }

    public static final String MODULE_NAME = "MODULE_NAME";

    public static void jumpToTestUtilActivity(Context context, String moduleName) {
        Intent intent = new Intent();
        intent.setClass(context, TestUtilActivity.class);
        intent.putExtra(MODULE_NAME, moduleName);
        context.startActivity(intent);
    }

    public static InputLayout createInputLayout(Activity activity, String hint) {
        InputLayout il = new InputLayout(activity);
        il.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
        il.setInputTextHint(hint);
        il.setDividerHeight(SizeUtil.dp2px(2));
        il.setDividerColor(activity.getResources().getColor(R.color.colorPrimary));
        LayoutParamsUtil.setMargin(il, (int) SizeUtil.dp2px(20), 0, (int) SizeUtil.dp2px(20), (int) SizeUtil.dp2px(15));
        return il;
    }

}
