package com.android.util;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:测试工具类方法
 */
public class TestUtilActivity extends BaseActivity {

    public static final String TEST_DRAWABLE = "TEST_DRAWABLE";
    public static final String TEST_KEYBOARD = "TEST_KEYBOARD";
    public static final String TEST_NOTIFICATION = "TEST_NOTIFICATION";

    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;

    @Override
    public void handleView(Bundle savedInstanceState) {
        switch (getIntent().getStringExtra(CommonLayoutUtil.MODULE_NAME)) {
            case TEST_KEYBOARD:
                testKeyBoardUtil();
                break;
            case TEST_DRAWABLE:
                testDrawableUtil();
                break;
            case TEST_NOTIFICATION:
                testNotification();
                break;
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    private void testKeyBoardUtil() {
        CommonLayoutUtil.initCommonLayout(this, "测试键盘工具", "弹出软键盘", "收起软键盘", "复制到剪切板");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.showSoftInput(getApplicationContext(), v);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.hideSoftInput(getApplicationContext(), v);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.copyToClipboard(getApplicationContext(), "https://www.baidu.com");
            }
        });
    }

    private void testDrawableUtil() {
        CommonLayoutUtil.initCommonLayout(this, "代码创建Drawable", "按钮", "solid", "stroke", "虚线stroke", "solid和stroke");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = DrawableUtil.createSolidShape(
                        SizeUtil.dp2px(10f),
                        getResources().getColor(R.color.orange)
                );
                btn1.setBackground(drawable);
                btn1.setTextColor(getResources().getColor(R.color.white));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = DrawableUtil.createStrokeShape(
                        SizeUtil.dp2px(15),
                        (int) SizeUtil.dp2px(1),
                        getResources().getColor(R.color.orange)
                );
                btn1.setBackground(drawable);
                btn1.setTextColor(getResources().getColor(R.color.orange));
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = DrawableUtil.createStrokeShape(
                        SizeUtil.dp2px(15),
                        (int) SizeUtil.dp2px(1),
                        getResources().getColor(R.color.orange),
                        SizeUtil.dp2px(2f),
                        SizeUtil.dp2px(2f)
                );
                btn1.setBackground(drawable);
                btn1.setTextColor(getResources().getColor(R.color.orange));
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = DrawableUtil.createSolidStrokeShape(
                        SizeUtil.dp2px(25),
                        getResources().getColor(R.color.white),
                        (int) SizeUtil.dp2px(2),
                        getResources().getColor(R.color.orange)
                );
                btn1.setBackground(drawable);
                btn1.setTextColor(getResources().getColor(R.color.orange));
            }
        });
    }

    //通知管理
    private void testNotification() {
        String extraCpntent = getIntent().getStringExtra("content");
        tv.setText(extraCpntent);  //传递过来的参数
        EditText titleEt = new EditText(this);
        titleEt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        titleEt.setHint("请输入标题");
        ll.addView(titleEt, 0);
        et.setHint("请输入内容");
        CommonLayoutUtil.initCommonLayout(this, "通知管理", true, true,
                "自定义通知", "自定义通知（带跳转）", "通知是否打开", "跳转通知设置界面");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    title = "这是标题";
                }
                String content = et.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    content = "这是内容";
                }
                NotificationUtil.showNotification(getApplicationContext(), title, content);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    title = "这是标题";
                }
                String content = et.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    content = "跳转到通知管理页面";
                }
                Intent intent = new Intent(TestUtilActivity.this, TestUtilActivity.class);
                intent.putExtra(CommonLayoutUtil.MODULE_NAME, TEST_NOTIFICATION);
                intent.putExtra("content", content);
                NotificationUtil.showNotificationWithIntent(TestUtilActivity.this, intent, title, content);
                finish();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOpen = NotificationUtil.isNotificationEnabled(getApplicationContext());
                tv.setText(isOpen ? "通知权限已打开" : "通知权限被关闭");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtil.gotoNotificationSetting(getApplicationContext());
            }
        });
    }

}
