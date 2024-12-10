package com.android.widget.justtext.AATest;

import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.base.DontSwipeBack;
import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestJusttextBinding;
import com.android.util.JsonUtil;
import com.android.util.LayoutParamsUtil;
import com.android.util.SizeUtil;
import com.android.widget.InputLayout;
import com.android.widget.justtext.JustUtils;
import com.android.widget.justtext.SpanConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by xuzhb on 2024/12/7
 * Desc:
 */
public class TestJusttextActivity extends BaseActivity<ActivityTestJusttextBinding> implements DontSwipeBack {

    private static final String TAG = "TestJusttextActivity";

    private ArrayList<SpanConfig> mSpanConfigList = new ArrayList<>();
    private SpanConfigAdapter mAdapter = new SpanConfigAdapter();

    @Override
    public void handleView(Bundle savedInstanceState) {
        String content = "这是两端对齐文本，支持设置如下效果：\n1、首行缩进\n2、末行缩进\n3、部分加粗\n4、部分变色\n5、部分字体大小变化(居中对齐)";
        int boldStartPosition = content.indexOf("加粗");
        int colorStartPosition = content.indexOf("变色");
        int textSizeStartPosition = content.indexOf("字体大小");
        mSpanConfigList.add(new SpanConfig(boldStartPosition, boldStartPosition + 1, true));
        mSpanConfigList.add(new SpanConfig(colorStartPosition, colorStartPosition + 1, Color.RED));
        mSpanConfigList.add(new SpanConfig(textSizeStartPosition, textSizeStartPosition + 3, SizeUtil.sp2px(10f), true));
        binding.justTv.setSpanConfigList(mSpanConfigList);
        binding.configRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.configRv.setAdapter(mAdapter);
        mAdapter.setNewInstance(mSpanConfigList);
        binding.inputContentEt.setText(content);
        setContent();
    }

    @Override
    public void initListener() {
        binding.randomContentBtn.setOnClickListener(v -> {
            int count = parseInt(binding.contentCountIl);
            String content = createExpandText(count > 0 ? count : 100);
            binding.inputContentEt.setText(content);
            setContent();
        });
        binding.maxLinesBtn.setOnClickListener(v -> {
            int maxLines = parseInt(binding.maxLinesIl);
            maxLines = maxLines > 0 ? maxLines : Integer.MAX_VALUE;
            binding.justTv.setMaxLines(maxLines);
        });
        binding.minLinesBtn.setOnClickListener(v -> {
            int minLines = parseInt(binding.minLinesIl);
            minLines = Math.max(minLines, 0);
            binding.justTv.setMinLines(minLines);
        });
        binding.setContentBtn.setOnClickListener(v -> setContent());
        binding.firstIndentBtn.setOnClickListener(v -> setContent());
        binding.lastIndentBtn.setOnClickListener(v -> setContent());
        mAdapter.setOnChangeListener(this::setContent);
    }

    private void setContent() {
        String text = binding.inputContentEt.getText().toString();
        int firstIndentLength = parseInt(binding.firstIndentIl);
        if (firstIndentLength < 0) {
            firstIndentLength = 0;
        }
        int lastIndentLength = parseInt(binding.lastIndentIl);
        if (lastIndentLength < 0) {
            lastIndentLength = 0;
        }
        LayoutParamsUtil.setWidth(binding.firstIndentView, firstIndentLength);
        LayoutParamsUtil.setWidth(binding.lastIndentView, lastIndentLength);
        //设置缩进
        binding.justTv.setTextWithIndent(text, firstIndentLength, lastIndentLength);
        //设置样式
        binding.justTv.setSpanConfigList(mSpanConfigList);
        //设置最大行数
        int maxLines = parseInt(binding.maxLinesIl);
        maxLines = maxLines > 0 ? maxLines : Integer.MAX_VALUE;
        binding.justTv.setMaxLines(maxLines);
        //设置最小行数
        int minLines = parseInt(binding.minLinesIl);
        minLines = Math.max(minLines, 0);
        binding.justTv.setMinLines(minLines);

        Map<String, Object> map = new HashMap<>();
        map.put("最大行数", maxLines);
        map.put("最小行数", minLines);
        map.put("首行缩进", firstIndentLength);
        map.put("末行缩进", lastIndentLength);
        map.put("字体样式", mSpanConfigList);
        map.put("有效字体样式", JustUtils.getActiveSpanConfigList(mSpanConfigList, false));
        JsonUtil.printObject("TestJusttextActivity", map);
    }

    private String createExpandText(int count) {
        String[] array = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "a", "b", "c", "d", "e", "f", "t", "g", "q", "w", "e",
                "p", "l", "k", "i", "n", "m", "G", "H", "J", "I", "L", "C", "V", "B"
                , "你", "我", "他", "天", "地", "动", "进", "啊", "去", "改", "酒",
                "一", "会", "年", "收", "好", "嗯", "这", "有", "——",
                /*"\r", "\n", "\r\n", "\t",*/ "，", "！", "%", "@"
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(array[new Random().nextInt(array.length)]);
        }
        return sb.toString();
    }

    private static int parseInt(InputLayout inputLayout) {
        try {
            return Integer.parseInt(inputLayout.getInputText());
        } catch (Exception e) {

        }
        return 0;
    }

}
