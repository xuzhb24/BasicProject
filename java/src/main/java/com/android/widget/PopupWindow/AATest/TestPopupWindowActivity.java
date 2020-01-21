package com.android.widget.PopupWindow.AATest;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.widget.PopupWindow.CommonPopupWindow;
import com.android.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:CommonPopupWindow使用示例
 */
public class TestPopupWindowActivity extends BaseActivity {

    private CommonPopupWindow mPopupWindow;

    @BindView(R.id.title_bar)
    TitleBar titleBar;

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(v -> {
            finish();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_popup_window;
    }

    @OnClick({R.id.down_btn1, R.id.down_btn2, R.id.right_btn, R.id.left_btn, R.id.full_btn, R.id.up_btn, R.id.query_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.down_btn1:  //向下弹出
                showDownWindow1(view);
                break;
            case R.id.down_btn2:  //向下弹出
                showDownWindow2(view);
                break;
            case R.id.right_btn:  //向右弹出
                break;
            case R.id.left_btn:  //向左弹出
                break;
            case R.id.full_btn:  //全屏弹出
                break;
            case R.id.up_btn:  //向上弹出
                break;
            case R.id.query_iv:  //问号
                break;
        }
    }

    private void showDownWindow1(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_down1)
                .setViewParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.AnimScaleDown)
                .setBackGroundAlpha(0.6f)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setOnClickListener(R.id.cancel_btn, v -> holder.setText(R.id.content_tv, "取消"));
                    holder.setOnClickListener(R.id.confirm_btn, v -> holder.setText(R.id.content_tv, "确定"));
                    holder.setOnClickListener(R.id.outside_view, v -> popupWindow.dismiss());
                }).build();
        mPopupWindow.showAsDropDown(view);
    }

    private void showDownWindow2(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_down2)
                .setViewParams(view.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.AnimScaleDown)
                .setBackGroundAlpha(0.6f)
                .setOnViewListener((holder, popupWindow) -> {
                    List<String> list = new ArrayList<>();
                    list.add("周一");
                    list.add("周二");
                    list.add("周三");
                    list.add("周四");
                    list.add("周五");
                    list.add("周六");
                    list.add("周日");
                    PopupWindowAdapter adapter = new PopupWindowAdapter(this, list);
                    RecyclerView popupRv = holder.getView(R.id.popup_rv);
                    popupRv.setAdapter(adapter);
                    adapter.setOnItemClickListener((obj, position) -> {
                        showToast((position + 1) + " " + obj);
//                        popupWindow.dismiss();
                    });
                }).build();
        mPopupWindow.showAsDropDown(view);

    }

}
