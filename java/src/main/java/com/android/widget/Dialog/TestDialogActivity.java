package com.android.widget.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;
import com.android.util.DrawableUtil;
import com.android.util.SizeUtil;
import com.android.util.ToastUtil;
import com.android.widget.TitleBar;
import com.android.widget.ViewHolder;

/**
 * Created by xuzhb on 2019/10/21
 * Desc:
 */
public class TestDialogActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "通用的Dialog", "单按钮", "双按钮", "分享", "评论");
    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(new TitleBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                showSingleDialog();
                break;
            case R.id.btn2:
                showMultiDialog();
                break;
            case R.id.btn3:
                showShareDialog();
                break;
            case R.id.btn4:
                showCommentDialog();
                break;
        }
    }

    //单按钮Dialog
    private void showSingleDialog() {
        ConfirmDialog.newInstance()
                .setTitle("警告")
                .setContent("对不起审核不通过！")
                .setConfirmText("我知道了")
                .setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        dialog.dismiss();
                        ToastUtil.toast("确定");
                    }
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
                .setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        dialog.dismiss();
                        ToastUtil.toast("确定");
                    }
                })
                .setOnCancelListener(new ConfirmDialog.OnCancelListener() {
                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                        ToastUtil.toast("取消");
                    }
                })
                .setOutsideCancelable(true)
                .setHorizontalMargin((int) SizeUtil.dp2px(60))
                .show(getSupportFragmentManager());
    }

    //分享Dialog
    private void showShareDialog() {
        CommonDialog.newInstance()
                .setLayoutId(R.layout.layout_share_dialog)
                .setOnViewListener(new CommonDialog.OnViewListener() {
                    @Override
                    public void convertView(ViewHolder holder, BaseDialog dialog) {
                        holder.setOnClickListener(R.id.weixin_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.toast("微信");
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.qq_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.toast("QQ");
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.weibo_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.toast("微博");
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.cancel_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setAnimationStyle(R.style.AnimUp)
                .showAtBottom(getSupportFragmentManager());
    }

    //评论Dialog
    private void showCommentDialog() {
        CommonDialog.newInstance()
                .setLayoutId(R.layout.layout_comment_dialog)
                .setOnViewListener(new CommonDialog.OnViewListener() {
                    @Override
                    public void convertView(ViewHolder holder, BaseDialog dialog) {
                        EditText commentEt = holder.getView(R.id.comment_et);
                        commentEt.setBackground(DrawableUtil.createSolidShape(
                                SizeUtil.dp2px(10f), Color.parseColor("#e6e6e6")));
                        holder.setOnClickListener(R.id.send_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String text = commentEt.getText().toString().trim();
                                if (TextUtils.isEmpty(text)) {
                                    ToastUtil.toast("请输入文字！");
                                } else {
                                    ToastUtil.toast(text);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                })
                .showAtBottom(getSupportFragmentManager());
    }

}
