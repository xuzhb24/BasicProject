package com.android.widget.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.EditText
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.DrawableUtil
import com.android.util.KeyboardUtil
import com.android.util.SizeUtil
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2019/10/22
 * Desc:
 */
class TestDialogActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "通用的Dialog", "单按钮", "双按钮", "分享", "评论", "领券")
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        btn1.setOnClickListener {
            showSingleDialog()
        }
        btn2.setOnClickListener {
            showMultiDialog()
        }
        btn3.setOnClickListener {
            showShareDialog()
        }
        btn4.setOnClickListener {
            showCommentDialog()
        }
        btn5.setOnClickListener {
            showCouponDialog()
        }
    }

    //单按钮Dialog
    private fun showSingleDialog() {
        ConfirmDialog.newInstance()
            .setTitle("警告")
            .setContent("对不起审核不通过！")
            .setConfirmText("我知道了")
            .setOnConfirmListener {
                it.dismiss()
                showToast("我知道了")
            }
            .setCancelVisible(false)
            .setDimAmount(0.6f)
            .setOutsideCancelable(false)
            .setViewParams(SizeUtil.dp2px(240f).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            .show(supportFragmentManager)
    }

    //双按钮Dialog
    private fun showMultiDialog() {
        ConfirmDialog.newInstance("提示", "提交成功！")
            .setOnConfirmListener {
                it.dismiss()
                showToast("确定")
            }
            .setOnCancelListener {
                it.dismiss()
                showToast("取消")
            }
            .setOutsideCancelable(true)
            .setHorizontalMargin(SizeUtil.dp2px(60f).toInt())
            .show(supportFragmentManager)
    }

    //分享Dialog
    private fun showShareDialog() {
        CommonDialog.newInstance()
            .setLayoutId(R.layout.layout_share_dialog)
            .setOnViewListener { holder, dialog ->
                holder.setOnClickListener(R.id.weixin_tv) {
                    showToast("微信")
                    dialog.dismiss()
                }
                holder.setOnClickListener(R.id.qq_tv) {
                    showToast("QQ")
                    dialog.dismiss()
                }
                holder.setOnClickListener(R.id.weibo_tv) {
                    showToast("微博")
                    dialog.dismiss()
                }
                holder.setOnClickListener(R.id.cancel_tv, { dialog.dismiss() })
            }
            .setDimAmount(0.3f)
            .setAnimationStyle(R.style.AnimTranslateBottom)
            .showAtBottom(supportFragmentManager)
    }

    //评论Dialog
    private fun showCommentDialog() {
        CommonDialog.newInstance()
            .setLayoutId(R.layout.layout_comment_dialog)
            .setOnViewListener { holder, dialog ->
                val commentEt: EditText = holder.getView(R.id.comment_et)!!
                val drawable = DrawableUtil.createSolidShape(SizeUtil.dp2px(10f), Color.parseColor("#e6e6e6"))
                commentEt.setBackground(drawable)
                holder.setOnClickListener(R.id.send_tv) {
                    val text = commentEt.text.toString().trim()
                    if (TextUtils.isEmpty(text)) {
                        showToast("请输入文字")
                    } else {
                        showToast(text)
                        dialog.dismiss()
                    }
                }
                KeyboardUtil.showSoftInputDelay(this, commentEt)
            }
            .showAtBottom(supportFragmentManager)
    }

    //领券Dialog
    private fun showCouponDialog() {
        CommonDialog.newInstance()
            .setLayoutId(R.layout.layout_coupon_dialog)
            .setOnViewListener { holder, dialog ->
                holder.setOnClickListener(R.id.return_tv) {
                    showToast("领取成功！")
                    dialog.dismiss()
                }
            }
            .show(supportFragmentManager)
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout
}