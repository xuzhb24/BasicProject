package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
            };
            PhotoViewActivity.start(getActivity(), array);
        });
        //悬浮窗(申请权限)
        binding.floatTv.setOnClickListener(v -> {
            startActivity(TestFloatActivity.class);
        });
    }

}
