package com.android.widget.PopupWindow.AATest

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.android.frame.mvc.BaseActivity
import com.android.basicproject.R
import com.android.util.ToastUtil
import com.android.widget.PopupWindow.CommonPopupWindow
import kotlinx.android.synthetic.main.activity_test_popup_window.*
import org.jetbrains.anko.toast

/**
 * Created by xuzhb on 2019/9/1
 * Desc:CommonPopupWindow使用示例
 */
class TestPopupWindowActivity : BaseActivity(), CommonPopupWindow.OnChildViewListener {

    private var mPopupWindow: CommonPopupWindow? = null

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        //向下弹出
        down_btn1.setOnClickListener {
            showDownWindow1(it)
        }
        down_btn2.setOnClickListener {
            showDownWindow2(it)
        }
        //向上弹出
        up_btn.setOnClickListener {

        }
        //向右弹出
        right_btn.setOnClickListener {

        }
        //向左弹出
        left_btn.setOnClickListener {

        }
        //全屏弹出
        full_btn.setOnClickListener {

        }
        //问号
        query_iv.setOnClickListener {

        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_popup_window

    fun showDownWindow1(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_down1)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
//            .setAnimationStyle(R.style.AnimScaleDown)
//            .setBackGroundAlpha(0.6f)
            .setOnViewListener(this)
            .build()
        mPopupWindow!!.showAsDropDown(view)
    }

    fun showDownWindow2(view: View) {
        if (mPopupWindow != null && mPopupWindow!!.isShowing)
            return
        mPopupWindow = CommonPopupWindow.Builder(this)
            .setContentView(R.layout.layout_popup_window_down2)
            .setWidthAndHeight(
                view.width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true)
            .setAnimationStyle(R.style.AnimScaleDown)
            .setBackGroundAlpha(0.6f)
            .setOnViewListener(this)
            .build()
        mPopupWindow!!.showAsDropDown(view)
    }

    override fun onChildView(popupWindow: PopupWindow, view: View, layoutId: Int) {
        when (layoutId) {
            R.layout.layout_popup_window_down1 -> {
                val contentTv: TextView = view.findViewById(R.id.content_tv)
                val cancelBtn: Button = view.findViewById(R.id.cancel_btn)
                val confirmBtn: Button = view.findViewById(R.id.confirm_btn)
                val outsideView: View = view.findViewById(R.id.outside_view)
                cancelBtn.setOnClickListener {
                    contentTv.text = "取消"
                }
                confirmBtn.setOnClickListener {
                    contentTv.text = "确定"
                }
                outsideView.setOnClickListener {
                    popupWindow.dismiss()
                }
            }
            R.layout.layout_popup_window_down2 -> {
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
                val popupRv: RecyclerView = view.findViewById(R.id.popup_rv)
                popupRv.adapter = adapter
                adapter.setOnItemClickListener { obj, position ->
                    ToastUtil.toast("$position  $obj")
//                    popupWindow.dismiss()
                }
            }
        }
    }

}