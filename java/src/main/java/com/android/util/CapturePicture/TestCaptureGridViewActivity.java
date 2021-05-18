package com.android.util.CapturePicture;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestCaptureGridviewBinding;
import com.android.util.bitmap.BitmapUtil;
import com.android.util.permission.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2021/5/12
 * Desc:
 */
public class TestCaptureGridViewActivity extends BaseActivity<ActivityTestCaptureGridviewBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            list.add("文本" + i);
        }
        binding.gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public String getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_test_capture, null);
                ((TextView) view.findViewById(R.id.tv)).setText(list.get(position));
                return view;
            }
        });
    }

    @Override
    public void initListener() {
        mTitleBar.setOnRightTextClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showToast("请先允许权限");
                return;
            }
            Bitmap bitmap = CapturePictureUtil.captureByGridView(binding.gridView);
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "GridView截图")) {
                showToast("保存成功，请在相册查看");
            } else {
                showToast("保存失败");
            }
        });
    }

    @Override
    public ActivityTestCaptureGridviewBinding getViewBinding() {
        return ActivityTestCaptureGridviewBinding.inflate(getLayoutInflater());
    }
}
