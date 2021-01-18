package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.frame.mvc.BaseFragment;
import com.android.java.databinding.FragmentWidgetBinding;
import com.android.universal.TestSystemWidgetActivity;
import com.android.widget.Dialog.TestDialogActivity;
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
            PhotoViewActivity.start(getActivity());
        });
    }

    @Override
    public FragmentWidgetBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentWidgetBinding.inflate(inflater, container, false);
    }

}
