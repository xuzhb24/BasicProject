package com.android.widget.PopupWindow.AATest;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestPopupWindowBinding;
import com.android.util.SizeUtil;
import com.android.widget.PopupWindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:CommonPopupWindow使用示例
 */
public class TestPopupWindowActivity extends BaseActivity<ActivityTestPopupWindowBinding> {

    private CommonPopupWindow mPopupWindow;

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        binding.toBottomBtn1.setOnClickListener(this::showToBottomWindow1);  //向下弹出
        binding.toBottomBtn2.setOnClickListener(this::showToBottomWindow2);  //向下弹出
        binding.toRightBtn.setOnClickListener(this::showToRightWindow);      //向右弹出
        binding.toLeftBtn.setOnClickListener(this::showToLeftWindow);        //向左弹出
        binding.fullBtn.setOnClickListener(this::showFullWindow);            //全屏弹出
        binding.toTopBtn.setOnClickListener(this::showToTopWIndow);          //向上弹出
    }

    //向下弹出
    private void showToBottomWindow1(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_to_bottom1)
                .setViewParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.AnimScaleTop)
                .setBackGroundAlpha(0.6f)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setOnClickListener(R.id.cancel_btn, v -> holder.setText(R.id.content_tv, "取消"));
                    holder.setOnClickListener(R.id.confirm_btn, v -> holder.setText(R.id.content_tv, "确定"));
                    holder.setOnClickListener(R.id.outside_view, v -> popupWindow.dismiss());
                }).build();
        mPopupWindow.showAsDropDown(view);
    }

    //向下弹出
    private void showToBottomWindow2(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_to_bottom2)
                .setViewParams(view.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(false)
                .setAnimationStyle(R.style.AnimScaleTop)
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
                        popupWindow.dismiss();
                    });
                }).build();
        mPopupWindow.showAsDropDown(view);

    }

    //向右弹出
    private void showToRightWindow(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_to_left_or_right)
                .setViewParams((int) SizeUtil.dp2px(160), (int) SizeUtil.dp2px(50))
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corners_10_solid_000000))
                .setAnimationStyle(R.style.AnimScaleLeft)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setOnClickListener(R.id.praise_tv, v -> {
                        showToast("赞");
                        popupWindow.dismiss();
                    });
                    holder.setOnClickListener(R.id.comment_tv, v -> {
                        showToast("评论");
                        popupWindow.dismiss();
                    });
                }).build();
        mPopupWindow.showAsDropDown(view, view.getWidth(), -(view.getHeight() + (mPopupWindow.getHeight() - view.getHeight()) / 2));
    }

    //向左弹出
    private void showToLeftWindow(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_to_left_or_right)
                .setViewParams((int) SizeUtil.dp2px(160), (int) SizeUtil.dp2px(50))
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corners_10_solid_000000))
                .setAnimationStyle(R.style.AnimScaleRight)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setOnClickListener(R.id.praise_tv, v -> {
                        showToast("赞");
                        popupWindow.dismiss();
                    });
                    holder.setOnClickListener(R.id.comment_tv, v -> {
                        showToast("评论");
                        popupWindow.dismiss();
                    });
                }).build();
        mPopupWindow.showAsDropDown(view, -mPopupWindow.getWidth(), -(view.getHeight() + (mPopupWindow.getHeight() - view.getHeight()) / 2));
    }

    //全屏弹出
    private void showFullWindow(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_full)
                .setViewParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundAlpha(0.5f)
                .setAnimationStyle(R.style.AnimTranslateBottom)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setOnClickListener(R.id.camera_tv, v -> {
                        showToast("拍照");
                    });
                    holder.setOnClickListener(R.id.gallery_tv, v -> {
                        showToast("相册");
                    });
                    holder.setOnClickListener(R.id.cancel_tv, v -> {
                        showToast("取消");
                        popupWindow.dismiss();
                    });
                }).build();
        mPopupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    //向上弹出
    private void showToTopWIndow(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new CommonPopupWindow.Builder(this)
                .setContentView(R.layout.layout_popup_window_to_top)
                .setAnimationStyle(R.style.AnimScaleBottom)
                .setOnViewListener((holder, popupWindow) -> {
                    holder.setOnClickListener(R.id.reply_tv, v -> {
                        showToast("回复");
                        popupWindow.dismiss();
                    });
                    holder.setOnClickListener(R.id.share_tv, v -> {
                        showToast("分享");
                        popupWindow.dismiss();
                    });
                    holder.setOnClickListener(R.id.report_tv, v -> {
                        showToast("举报");
                        popupWindow.dismiss();
                    });
                    holder.setOnClickListener(R.id.copy_tv, v -> {
                        showToast("复制");
                        popupWindow.dismiss();
                    });
                }).build();
        mPopupWindow.showAsDropDown(view,
                -(mPopupWindow.getContentView().getMeasuredWidth() - view.getWidth()) / 2,
                -(mPopupWindow.getContentView().getMeasuredHeight() + view.getHeight()));
    }

}
