package com.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentWidgetBinding
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

/**
 * Created by xuzhb on 2019/9/7
 * Desc:控件篇
 */
class WidgetFragment : BaseFragment<FragmentWidgetBinding>() {

    companion object {
        fun newInstance() = WidgetFragment()
    }

    override fun handleView(savedInstanceState: Bundle?) {
        //通用PopupWindow
        binding.popupTv.setOnClickListener {
            startActivity(TestPopupWindowActivity::class.java)
        }
        //饼状图
        binding.piechartTv.setOnClickListener {
            startActivity(TestPieChartActivity::class.java)
        }
        //曲线图/折线图
        binding.linechartTv.setOnClickListener {
            startActivity(TestLineChartActivity::class.java)
        }
        //进度条
        binding.progressTv.setOnClickListener {
            startActivity(TestProgressBarActivity::class.java)
        }
        //对话框
        binding.dialogTv.setOnClickListener {
            startActivity(TestDialogActivity::class.java)
        }
        //拍照和相册弹窗
        binding.picDialogTv.setOnClickListener {
            startActivity(TestPicGetterDialogActivity::class.java)
        }
        //RecyclerView组件
        binding.recyclerviewTv.setOnClickListener {
            startActivity(TestRecyclerViewActivity::class.java)
        }
        //加载状态布局
        binding.loadingTv.setOnClickListener {
            startActivity(TestLoadingLayoutActivity::class.java)
        }
        //单一控件
        binding.singleTv.setOnClickListener {
            startActivity(TestSingleWidgetActivity::class.java)
        }
        //系统控件
        binding.systemTv.setOnClickListener {
            startActivity(TestSystemWidgetActivity::class.java)
        }
    }

    override fun initListener() {
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentWidgetBinding.inflate(inflater, container, false)

}