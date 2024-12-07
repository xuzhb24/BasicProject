package com.android.base;

import android.os.Bundle;

import com.android.frame.mvc.AATest.TestWebviewActivity;
import com.android.frame.mvc.BaseFragment;
import com.android.java.databinding.FragmentWidgetBinding;
import com.android.universal.TestSystemWidgetActivity;
import com.android.widget.Dialog.TestDialogActivity;
import com.android.widget.FloatWindow.TestFloatActivity;
import com.android.widget.LoadingLayout.TestLoadingLayoutActivity;
import com.android.widget.PhotoViewer.PhotoViewActivity;
import com.android.widget.PicGetterDialog.AATest.TestPicGetterDialogActivity;
import com.android.widget.PopupWindow.AATest.TestPopupWindowActivity;
import com.android.widget.ProgressBar.TestProgressBarActivity;
import com.android.widget.RecyclerView.AATest.TestRecyclerViewActivity;
import com.android.widget.TestSingleWidgetActivity;
import com.android.widget.justtext.AATest.TestJusttextActivity;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:控件篇
 */
public class WidgetFragment extends BaseFragment<FragmentWidgetBinding> {

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        //系统控件
        binding.systemTv.setOnClickListener(v -> {
            startActivity(TestSystemWidgetActivity.class);
        });
        //单一控件
        binding.singleTv.setOnClickListener(v -> {
            startActivity(TestSingleWidgetActivity.class);
        });
        //通用PopupWindow
        binding.popupTv.setOnClickListener(v -> {
            startActivity(TestPopupWindowActivity.class);
        });
        //两端对齐文本
        binding.justTv.setOnClickListener(v -> {
            startActivity(TestJusttextActivity.class);
        });
        //饼状图
        binding.piechartTv.setOnClickListener(v -> {

        });
        //曲线图/折线图
        binding.linechartTv.setOnClickListener(v -> {

        });
        //进度条
        binding.progressTv.setOnClickListener(v -> {
            startActivity(TestProgressBarActivity.class);
        });
        //对话框
        binding.dialogTv.setOnClickListener(v -> {
            startActivity(TestDialogActivity.class);
        });
        //拍照和相册弹窗
        binding.picDialogTv.setOnClickListener(v -> {
            startActivity(TestPicGetterDialogActivity.class);
        });
        //RecyclerView组件
        binding.recyclerviewTv.setOnClickListener(v -> {
            startActivity(TestRecyclerViewActivity.class);
        });
        //加载状态布局
        binding.loadingTv.setOnClickListener(v -> {
            startActivity(TestLoadingLayoutActivity.class);
        });
        //图片查看器
        binding.photoTv.setOnClickListener(v -> {
            String[] array = {
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
                    "http:aaaaa",
            };
            PhotoViewActivity.start(getActivity(), array, 0);
        });
        //悬浮窗(申请权限)
        binding.floatTv.setOnClickListener(v -> {
            startActivity(TestFloatActivity.class);
        });
        //Webview
        binding.webviewTv.setOnClickListener(v -> {
            startActivity(TestWebviewActivity.class);
        });
    }

}
