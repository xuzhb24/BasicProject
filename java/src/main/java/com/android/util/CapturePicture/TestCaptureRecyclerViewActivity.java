package com.android.util.CapturePicture;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestCaptureRecyclerviewBinding;
import com.android.util.bitmap.BitmapUtil;
import com.android.util.permission.PermissionUtil;
import com.android.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xuzhb on 2021/5/12
 * Desc:
 */
public class TestCaptureRecyclerViewActivity extends BaseActivity<ActivityTestCaptureRecyclerviewBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//
//        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.HORIZONTAL, false));

        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));

        List<String> list = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
//            list.add("文本" + i);
            list.add(createExpandText(new Random().nextInt(50)));
        }
        binding.recyclerView.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_capture, null);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.setText(R.id.tv, list.get(position));
            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
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
            Bitmap bitmap = CapturePictureUtil.captureByRecyclerView(binding.recyclerView);
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "RecyclerView截图")) {
                showToast("保存成功，请在相册查看");
            } else {
                showToast("保存失败");
            }
        });
    }

    @Override
    public ActivityTestCaptureRecyclerviewBinding getViewBinding() {
        return ActivityTestCaptureRecyclerviewBinding.inflate(getLayoutInflater());
    }

    private String createExpandText(int count) {
        String[] array = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "a", "b", "c", "d", "e", "f", "t", "g", "q", "w", "e",
                "p", "l", "k", "i", "n", "m", "G", "H", "J", "I", "L", "C", "V", "B"
                , "你", "我", "他", "天", "地", "动", "进", "啊", "去", "改", "酒",
                "一", "会", "年", "收", "好", "嗯", "这", "有",
                /*"\r", "\n", "\r\n", "\t",*/ "，", "！", "%", "@"
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(array[new Random().nextInt(array.length)]);
        }
        return sb.toString();
    }

}
