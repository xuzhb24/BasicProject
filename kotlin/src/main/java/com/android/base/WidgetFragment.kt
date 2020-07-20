package com.android.base

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseFragment
import com.android.universal.TestSystemWidgetActivity
import com.android.widget.LineChart.TestLineChartActivity
import com.android.widget.LoadingLayout.TestLoadingLayoutActivity
import com.android.widget.PicGetterDialog.AATest.TestPicGetterDialogActivity
import com.android.widget.PieChart.TestPieChartActivity
import com.android.widget.PopupWindow.AATest.TestPopupWindowActivity
import com.android.widget.ProgressBar.TestProgressBarActivity
import com.android.widget.RecyclerView.AATest.TestRecyclerViewActivity
import com.android.widget.TestSingleWidgetActivity
import com.android.widget.dialog.TestDialogActivity
import kotlinx.android.synthetic.main.fragment_widget.*

/**
 * Created by xuzhb on 2019/9/7
 * Desc:控件篇
 */
class WidgetFragment : BaseFragment() {

    companion object {
        fun newInstance() = WidgetFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {
        //通用PopupWindow
        popup_tv.setOnClickListener {
            startActivity(TestPopupWindowActivity::class.java)
        }
        //饼状图
        piechart_tv.setOnClickListener {
            startActivity(TestPieChartActivity::class.java)
        }
        //曲线图/折线图
        linechart_tv.setOnClickListener {
            startActivity(TestLineChartActivity::class.java)
        }
        //进度条
        progress_tv.setOnClickListener {
            startActivity(TestProgressBarActivity::class.java)
        }
        //对话框
        dialog_tv.setOnClickListener {
            startActivity(TestDialogActivity::class.java)
        }
        //拍照和相册弹窗
        pic_dialog_tv.setOnClickListener {
            startActivity(TestPicGetterDialogActivity::class.java)
        }
        //RecyclerView组件
        recyclerview_tv.setOnClickListener {
            startActivity(TestRecyclerViewActivity::class.java)
        }
        //加载状态布局
        loading_tv.setOnClickListener {
            startActivity(TestLoadingLayoutActivity::class.java)
        }
        //单一控件
        single_tv.setOnClickListener {
            startActivity(TestSingleWidgetActivity::class.java)
        }
        //系统控件
        system_tv.setOnClickListener {
            startActivity(TestSystemWidgetActivity::class.java)
        }
    }

    override fun initListener() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_widget


}