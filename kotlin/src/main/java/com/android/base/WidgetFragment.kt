package com.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentWidgetBinding
import com.android.frame.mvc.BaseFragment
import com.android.universal.TestSystemWidgetActivity
import com.android.widget.FloatWindow.TestFloatActivity
import com.android.widget.LetterIndexBar.TestLetterIndexBarActivity
import com.android.widget.LineChart.TestLineChartActivity
import com.android.widget.LoadingLayout.TestLoadingLayoutActivity
import com.android.widget.PhotoViewer.PhotoViewActivity
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
        //系统控件
        binding.systemTv.setOnClickListener {
            startActivity(TestSystemWidgetActivity::class.java)
        }
        //单一控件
        binding.singleTv.setOnClickListener {
            startActivity(TestSingleWidgetActivity::class.java)
        }
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
        //字母通讯录
        binding.letterTv.setOnClickListener {
            startActivity(TestLetterIndexBarActivity::class.java)
        }
        //图片查看器
        binding.photoTv.setOnClickListener {
            val array = arrayOf(
                "aaaaa",
                "http://img.netbian.com/file/2021/0104/small69c4b125db64882f56f71843e0d633f11609692082.jpg",
                "http://img.netbian.com/file/2020/1223/small344fb01bb934cac4882d77f29d5ec5751608736763.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1730713693,2130926401&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2202780618,895893289&fm=26&gp=0.jpg",
                "http:sslancvan",
                "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1833567670,2009341108&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3225163326,3627210682&fm=26&gp=0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3200450391,4154446234&fm=26&gp=0.jpg"
            )
            activity?.let { PhotoViewActivity.start(it, array) }
        }
        //悬浮窗
        binding.floatTv.setOnClickListener {
            startActivity(TestFloatActivity::class.java)
        }
    }

    override fun initListener() {
    }

}