package com.android.widget.PopupWindow.AATest

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.android.widget.PopupWindow.CommonPopupWindow
import kotlinx.android.synthetic.main.activity_test_popup_window.*

/**
 * Created by xuzhb on 2019/9/1
 * Desc:CommonPopupWindow使用示例
 */
class TestPopupWindowActivity : BaseActivity() {

    private var mPopupWindow: CommonPopupWindow? = null

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        //向下弹出
        to_bottom_btn1.setOnClickListener {
            showToBottomWindow1(it)
        }
        to_bottom_btn2.setOnClickListener {
            showToBottomWindow2(it)
        }
        //向右弹出
        to_right_btn.setOnClickListener {
            showToRightWindow(it)
        }
        //向左弹出
        to_left_btn.setOnClickListener {
            showToLeftWindow(it)
        }
        //全屏弹出
        full_btn.setOnClickListener {
            showFullWindow(it)
        }
        //向上弹出
        to_top_btn.setOnClickListener {
            showToTopWindow(it)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_popup_window

    //向下弹出
    private fun showToBottomWindow1(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_to_bottom1)
            .setViewParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
//            .setAnimationStyle(R.style.AnimScaleTop)
//            .setBackGroundAlpha(0.6f)
            .setOnViewListener { holder, popupWindow ->
                holder.setOnClickListener(R.id.cancel_btn) { holder.setText(R.id.content_tv, "取消") }
                holder.setOnClickListener(R.id.confirm_btn) { holder.setText(R.id.content_tv, "确定") }
                holder.setOnClickListener(R.id.outside_view) { popupWindow.dismiss() }
            }
            .build()
        mPopupWindow!!.showAsDropDown(view)
    }

    //向下弹出
    private fun showToBottomWindow2(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_to_bottom2)
            .setViewParams(
                view.width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(false)
            .setAnimationStyle(R.style.AnimScaleTop)
            .setBackGroundAlpha(0.6f)
            .setOnViewListener { holder, popupWindow ->
                val list = ArrayList<String>()
                with(list) {
                    add("周一")
                    add("周二")
                    add("周三")
                    add("周四")
                    add("周五")
                    add("周六")
                    add("周日")
                }
                val adapter = PopupWindowAdapter(this, list)
                val popupRv: RecyclerView = holder.getView(R.id.popup_rv)!!
                popupRv.adapter = adapter
                adapter.setOnItemClickListener { obj, position ->
                    showToast("${position + 1}  $obj")
                    popupWindow.dismiss()
                }
            }
            .build()
        mPopupWindow!!.showAsDropDown(view)
    }

    //向右弹出
    private fun showToRightWindow(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_to_left_or_right)
            .setViewParams(SizeUtil.dp2px(160f).toInt(), SizeUtil.dp2px(50f).toInt())
            .setBackgroundDrawable(resources.getDrawable(R.drawable.shape_corners_10_solid_000000))
            .setAnimationStyle(R.style.AnimScaleLeft)
            .setOnViewListener { holder, popupWindow ->
                holder.setOnClickListener(R.id.praise_tv) {
                    showToast("赞")
                    popupWindow.dismiss()
                }
                holder.setOnClickListener(R.id.comment_tv) {
                    showToast("评论")
                    popupWindow.dismiss()
                }
            }.build()
        mPopupWindow!!.showAsDropDown(view, view.width, -(view.height + (mPopupWindow!!.height - view.height) / 2))
    }

    //向左弹出
    private fun showToLeftWindow(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_to_left_or_right)
            .setViewParams(SizeUtil.dp2px(160f).toInt(), SizeUtil.dp2px(50f).toInt())
            .setBackgroundDrawable(resources.getDrawable(R.drawable.shape_corners_10_solid_000000))
            .setAnimationStyle(R.style.AnimScaleRight)
            .setOnViewListener { holder, popupWindow ->
                holder.setOnClickListener(R.id.praise_tv) {
                    showToast("赞")
                    popupWindow.dismiss()
                }
                holder.setOnClickListener(R.id.comment_tv) {
                    showToast("评论")
                    popupWindow.dismiss()
                }
            }.build()
        mPopupWindow!!.showAsDropDown(
            view,
            -mPopupWindow!!.width,
            -(view.height + (mPopupWindow!!.height - view.height) / 2)
        )
    }

    //全屏弹出
    private fun showFullWindow(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_full)
            .setViewParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .setBackGroundAlpha(0.5f)
            .setAnimationStyle(R.style.AnimTranslateBottom)
            .setOnViewListener { holder, popupWindow ->
                holder.setOnClickListener(R.id.camera_tv) {
                    showToast("拍照")
                }
                holder.setOnClickListener(R.id.gallery_tv) {
                    showToast("相册")
                }
                holder.setOnClickListener(R.id.cancel_tv) {
                    showToast("取消")
                    popupWindow.dismiss()
                }
            }.build()
        mPopupWindow!!.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0)
    }

    //向上弹出
    private fun showToTopWindow(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_to_top)
            .setAnimationStyle(R.style.AnimScaleBottom)
            .setOnViewListener { holder, popupWindow ->
                holder.setOnClickListener(R.id.reply_tv) {
                    showToast("回复")
                    popupWindow.dismiss()
                }
                holder.setOnClickListener(R.id.share_tv) {
                    showToast("分享")
                    popupWindow.dismiss()
                }
                holder.setOnClickListener(R.id.report_tv) {
                    showToast("举报")
                    popupWindow.dismiss()
                }
                holder.setOnClickListener(R.id.copy_tv) {
                    showToast("复制")
                    popupWindow.dismiss()
                }
            }.build()
        mPopupWindow!!.showAsDropDown(
            view,
            -(mPopupWindow!!.contentView.measuredWidth - view.width) / 2,
            -(mPopupWindow!!.contentView.measuredHeight + view.height)
        )
    }

}