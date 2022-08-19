package com.android.widget.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.util.DrawableUtil;
import com.android.util.KeyboardUtil;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2019/10/21
 * Desc:
 */
public class TestDialogActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "通用的Dialog", "单按钮", "双按钮", "分享", "评论", "领券");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            showSingleDialog();
        });
        binding.btn2.setOnClickListener(v -> {
            showMultiDialog();
        });
        binding.btn3.setOnClickListener(v -> {
            showShareDialog();
        });
        binding.btn4.setOnClickListener(v -> {
            showCommentDialog();
        });
        binding.btn5.setOnClickListener(v -> {
            showCouponDialog();
        });
    }

    //单按钮Dialog
    private void showSingleDialog() {
        ConfirmDialog.newInstance()
                .setTitle("警告")
                .setContent("对不起审核不通过！")
                .setConfirmText("我知道了")
                .setOnConfirmListener(dialog -> {
                    dialog.dismissAllowingStateLoss();
                    showToast("确定");
                })
                .setCancelVisible(false)
                .setDimAmount(0.6f)
                .setOutsideCancelable(false)
                .setViewParams((int) SizeUtil.dp2px(240), WindowManager.LayoutParams.WRAP_CONTENT)
                .show(getSupportFragmentManager());
    }

    //双按钮Dialog
    private void showMultiDialog() {
        ConfirmDialog.newInstance("提示", "提交成功！", "确定", "取消", true)
                .setOnConfirmListener(dialog -> {
                    dialog.dismissAllowingStateLoss();
                    showToast("确定");
                })
                .setOnCancelListener(dialog -> {
                    dialog.dismissAllowingStateLoss();
                    showToast("取消");
                })
                .setOutsideCancelable(true)
                .setHorizontalMargin((int) SizeUtil.dp2px(60))
                .show(getSupportFragmentManager());
    }

    //分享Dialog
    private void showShareDialog() {
        CommonDialog.newInstance()
                .setLayoutId(R.layout.layout_share_dialog)
                .setOnViewListener((holder, dialog) -> {
                    holder.setOnClickListener(R.id.weixin_tv, v -> {
                        showToast("微信");
                        dialog.dismissAllowingStateLoss();
                    });
                    holder.setOnClickListener(R.id.qq_tv, v -> {
                        showToast("QQ");
                        dialog.dismissAllowingStateLoss();
                    });
                    holder.setOnClickListener(R.id.weibo_tv, v -> {
                        showToast("微博");
                        dialog.dismissAllowingStateLoss();
                    });
                    holder.setOnClickListener(R.id.cancel_tv, v -> {
                        dialog.dismissAllowingStateLoss();
                    });
                })
                .setDimAmount(0.3f)
                .setAnimationStyle(R.style.AnimTranslateBottom)
                .showAtBottom(getSupportFragmentManager());
    }

    //评论Dialog
    private void showCommentDialog() {
        CommonDialog.newInstance()
                .setLayoutId(R.layout.layout_comment_dialog)
                .setOnViewListener(((holder, dialog) -> {
                    EditText commentEt = holder.getView(R.id.comment_et);
                    commentEt.setBackground(DrawableUtil.createSolidShape(
                            SizeUtil.dp2px(10f), Color.parseColor("#e6e6e6")));
                    holder.setOnClickListener(R.id.send_tv, v -> {
                        String text = commentEt.getText().toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            showToast("请输入文字！");
                        } else {
                            showToast(text);
                            dialog.dismissAllowingStateLoss();
                        }
                    });
                    KeyboardUtil.showSoftInputDelay(this, commentEt);
                }))
                .showAtBottom(getSupportFragmentManager());
    }

    //领券Dialog
    private void showCouponDialog() {
        CommonDialog.newInstance()
                .setLayoutId(R.layout.layout_coupon_dialog)
                .setOnViewListener(((holder, dialog) -> {
                    holder.setOnClickListener(R.id.return_tv, v -> {
                        showToast("领取成功！");
                        dialog.dismissAllowingStateLoss();
                    });
                }))
                .setDimAmount(0.5f)
                .show(getSupportFragmentManager());
    }

}
