package com.android.widget.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.EditText
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.DrawableUtil
import com.android.util.SizeUtil
import com.android.util.ToastUtil
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2019/10/22
 * Desc:
 */
class TestDialogActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "通用的Dialog", "单按钮", "双按钮", "分享", "评论")
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
    }

    //单按钮Dialog
    private fun showSingleDialog() {
        ConfirmDialog.newInstance()
            .setTitle("警告")
            .setContent("对不起审核不通过！")
            .setConfirmText("我知道了")
            .setOnConfirmListener {
                it.dismiss()
                ToastUtil.toast("我知道了")
            }
            .setCancelVisible(false)
            .setDimAmount(0.6f)
            .setOutsideCancelable(false)
            .setViewParams(SizeUtil.dp2px(240f).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            .show(supportFragmentManager)
    }

    //双按钮Dialog
    private fun showMultiDialog() {
        ConfirmDialog.newInstance(content = "提交成功！")
            .setOnConfirmListener {
                it.dismiss()
                ToastUtil.toast("确定")
            }
            .setOnCancelListener {
                it.dismiss()
                ToastUtil.toast("取消")
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
                holder.setOnClickListener(R.id.weixin_tv, {
                    ToastUtil.toast("微信")
                    dialog.dismiss()
                })
                holder.setOnClickListener(R.id.qq_tv, {
                    ToastUtil.toast("QQ")
                    dialog.dismiss()
                })
                holder.setOnClickListener(R.id.weibo_tv, {
                    ToastUtil.toast("微博")
                    dialog.dismiss()
                })
            }
            .setDimAmount(0.3f)
            .setAnimationStyle(R.style.AnimUp)
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
                holder.setOnClickListener(R.id.send_tv, {
                    val text = commentEt.text.toString().trim()
                    if (TextUtils.isEmpty(text)) {
                        ToastUtil.toast("请输入文字")
                    } else {
                        ToastUtil.toast(text)
                        dialog.dismiss()
                    }
                })
            }
            .showAtBottom(supportFragmentManager)
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout
}