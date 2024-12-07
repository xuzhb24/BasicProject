package com.android.widget.justtext.AATest;

import android.graphics.Color;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.android.java.R;
import com.android.util.SizeUtil;
import com.android.util.ToastUtil;
import com.android.widget.InputLayout;
import com.android.widget.justtext.SpanConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * Created by xuzhb on 2024/12/7
 * Desc:
 */
public class SpanConfigAdapter extends BaseQuickAdapter<SpanConfig, BaseViewHolder> {

    public SpanConfigAdapter() {
        super(R.layout.item_span_config);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SpanConfig item) {
        InputLayout startPositionIl = holder.getView(R.id.start_position_il);
        InputLayout endPositionIl = holder.getView(R.id.end_position_il);
        RadioButton boldRb = holder.getView(R.id.bold_rb);
        RadioButton boldNotRb = holder.getView(R.id.bold_not_rb);
        InputLayout textColorIl = holder.getView(R.id.text_color_il);
        InputLayout textSizeIl = holder.getView(R.id.text_size_il);
        RadioButton alignCenterRb = holder.getView(R.id.align_center_rb);
        RadioButton alignCenterNotRb = holder.getView(R.id.align_center_not_rb);
        startPositionIl.setInputText(item.getStartPosition() + "");
        endPositionIl.setInputText(item.getEndPosition() + "");
        boldRb.setChecked(item.isTextBold());
        textColorIl.setInputText(item.getTextColor() == Color.BLACK ? "000000" : item.getTextColor() == Color.RED ? "FF0000" : "");
        textSizeIl.setInputText(SizeUtil.px2spInt(item.getTextSize()) + "");
        alignCenterRb.setChecked(item.isAlignLineCenter());
        String indexStr = "";
        switch (getItemPosition(item)) {
            case 0:
                indexStr = "样式一";
                break;
            case 1:
                indexStr = "样式二";
                break;
            case 2:
                indexStr = "样式三";
                break;
        }
        holder.setText(R.id.index_tv, indexStr);
        holder.getView(R.id.add_config_btn).setOnClickListener(v -> {
            int startPosition = parseInt(startPositionIl);
            int endPosition = parseInt(endPositionIl);
            item.setStartPosition(startPosition);
            item.setEndPosition(endPosition);
            item.setTextBold(boldRb.isChecked());
            item.setTextColor(parseColor(textColorIl));
            float textSize = parseFloat(textSizeIl);
            item.setTextSize(textSize > 0 ? SizeUtil.sp2px(textSize) : 0);
            item.setAlignLineCenter(alignCenterRb.isChecked());
            if (startPosition < 0 || endPosition < 0 || startPosition > endPosition) {
                ToastUtil.showToast("位置配置错误");
            }
            if (!item.isBoldConfig() && !item.isColorConfig() && !item.isSizeConfig()) {
                ToastUtil.showToast("样式配置错误");
            }
            if (mListener != null) {
                mListener.onChange();
            }
        });
        holder.getView(R.id.clear_config_btn).setOnClickListener(v -> {
            startPositionIl.setInputText("");
            endPositionIl.setInputText("");
            boldRb.setChecked(false);
            boldNotRb.setChecked(true);
            textColorIl.setInputText("");
            textSizeIl.setInputText("");
            alignCenterRb.setChecked(false);
            alignCenterNotRb.setChecked(true);
            item.setStartPosition(0);
            item.setEndPosition(0);
            item.setTextBold(false);
            item.setTextColor(0);
            item.setTextSize(0);
            item.setAlignLineCenter(false);
            if (mListener != null) {
                mListener.onChange();
            }
        });
    }

    private static int parseColor(InputLayout inputLayout) {
        try {
            return Color.parseColor("#" + inputLayout.getInputText());
        } catch (Exception e) {
            ToastUtil.showToast("字体颜色输入错误");
        }
        return 0;
    }

    private static int parseInt(InputLayout inputLayout) {
        try {
            return Integer.parseInt(inputLayout.getInputText());
        } catch (Exception e) {

        }
        return 0;
    }

    private static float parseFloat(InputLayout inputLayout) {
        try {
            return Float.parseFloat(inputLayout.getInputText());
        } catch (Exception e) {

        }
        return 0;
    }

    private OnChangeListener mListener;

    public void setOnChangeListener(OnChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnChangeListener {
        void onChange();
    }

}
