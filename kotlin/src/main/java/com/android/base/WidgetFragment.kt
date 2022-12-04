package com.android.base

import android.os.Bundle
import com.android.basicproject.databinding.FragmentWidgetBinding
import com.android.frame.mvc.AATest.TestWebviewActivity
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
                //长图
                "https://scpic.chinaz.net/files/default/imgs/2022-11-16/7509e2331beb98b0.jpg",
                "https://scpic.chinaz.net/files/default/imgs/2022-11-22/c86739e03928a677.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202207/apic41941.jpg",
                //超长图
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137b85ae35f79a801214a613dd49a.jpg%401280w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1671950097&t=c95a4b6e071acddab614067e078f2cc0",
                //宽图
                "https://scpic.chinaz.net/files/default/imgs/2022-11-16/fd255b6d859a0fd4.jpg",
                "https://scpic.chinaz.net/files/default/imgs/2022-11-16/a2ee6eb8dbdb10b8.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202102/apic30744.jpg",
                //超宽图
                "https://img2.baidu.com/it/u=725214044,4265534792&fm=253&fmt=auto&app=138&f=JPEG?w=2557&h=500",
                //无法加载
                "http:aaaaa"
            )
            activity?.let { PhotoViewActivity.start(it, array) }
        }
        //悬浮窗
        binding.floatTv.setOnClickListener {
            startActivity(TestFloatActivity::class.java)
        }
        //Webview
        binding.webviewTv.setOnClickListener {
            startActivity(TestWebviewActivity::class.java)
        }
    }

    override fun initListener() {
    }

}