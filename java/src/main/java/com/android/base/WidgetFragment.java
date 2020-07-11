package com.android.base;

import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;

import com.android.universal.TestSystemWidgetActivity;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;
import com.android.widget.Dialog.TestDialogActivity;
import com.android.widget.PicGetterDialog.AATest.TestPicGetterDialogActivity;
import com.android.widget.PopupWindow.AATest.TestPopupWindowActivity;
import com.android.widget.ProgressBar.TestProgressBarActivity;
import com.android.widget.RecyclerView.AATest.TestRecyclerViewActivity;
import com.android.widget.TestSingleWidgetActivity;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:控件篇
 */
public class WidgetFragment extends BaseFragment {

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_widget;
    }

    @OnClick({R.id.popup_tv, R.id.piechart_tv, R.id.linechart_tv, R.id.progress_tv, R.id.dialog_tv, R.id.pic_dialog_tv,
            R.id.recyclerview_tv, R.id.single_tv, R.id.system_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.popup_tv:  //通用PopupWindow
                startActivity(TestPopupWindowActivity.class);
                break;
            case R.id.piechart_tv:  //饼状图
                break;
            case R.id.linechart_tv:  //曲线图/折线图
                break;
            case R.id.progress_tv:  //进度条
                startActivity(TestProgressBarActivity.class);
                break;
            case R.id.dialog_tv:  //对话框
                startActivity(TestDialogActivity.class);
                break;
            case R.id.pic_dialog_tv:
                startActivity(TestPicGetterDialogActivity.class);
                break;
            case R.id.recyclerview_tv:  //RecyclerView组件
                startActivity(TestRecyclerViewActivity.class);
                break;
            case R.id.single_tv:  //单一控件
                startActivity(TestSingleWidgetActivity.class);
                break;
            case R.id.system_tv:  //系统控件
                startActivity(TestSystemWidgetActivity.class);
                break;
        }
    }
}
