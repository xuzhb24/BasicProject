package com.android.widget.justtext;

import androidx.annotation.ColorInt;

/**
 * Created by xuzhb on 2024/12/6
 * Desc:
 */
public class SpanConfig {

    private int startPosition;
    private int endPosition;
    private boolean textBold;  //加粗
    @ColorInt
    private int textColor;   //字体颜色
    private float textSize;  //字体大小，只适合小范围的字体大小变化，变化过大可能字会相互挤压
    private boolean alignLineCenter;  //同一行文本是否居中对齐（如果字体大小不一样），默认false，即基线对齐

    public SpanConfig() {
    }

    public SpanConfig(int startPosition, int endPosition, boolean textBold) {
        this(startPosition, endPosition, textBold, 0, 0, false);
    }

    public SpanConfig(int startPosition, int endPosition, @ColorInt int textColor) {
        this(startPosition, endPosition, false, textColor, 0, false);
    }

    public SpanConfig(int startPosition, int endPosition, float textSize, boolean alignLineCenter) {
        this(startPosition, endPosition, false, 0, textSize, alignLineCenter);
    }

    public SpanConfig(int startPosition, int endPosition, boolean textBold, @ColorInt int textColor) {
        this(startPosition, endPosition, textBold, textColor, 0, false);
    }

    public SpanConfig(int startPosition, int endPosition, @ColorInt int textColor, float textSize, boolean alignLineCenter) {
        this(startPosition, endPosition, false, textColor, textSize, alignLineCenter);
    }

    public SpanConfig(int startPosition, int endPosition, boolean textBold, float textSize, boolean alignLineCenter) {
        this(startPosition, endPosition, textBold, 0, textSize, alignLineCenter);
    }

    public SpanConfig(int startPosition, int endPosition, boolean textBold, int textColor, float textSize, boolean alignLineCenter) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.textBold = textBold;
        this.textColor = textColor;
        this.textSize = textSize;
        this.alignLineCenter = alignLineCenter;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public boolean isTextBold() {
        return textBold;
    }

    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public boolean isAlignLineCenter() {
        return alignLineCenter;
    }

    public void setAlignLineCenter(boolean alignLineCenter) {
        this.alignLineCenter = alignLineCenter;
    }

    //是否配置字体样式：加粗、字体颜色或大小
    public boolean isActive(int position) {
        return position >= startPosition && position <= endPosition && (isBoldConfig() || isColorConfig() || isSizeConfig());
    }

    //是否配置加粗
    public boolean isBoldConfig() {
        return textBold;
    }

    //是否配置字体颜色
    public boolean isColorConfig() {
        return textColor != 0;
    }

    //是否配置字体大小
    public boolean isSizeConfig() {
        return textSize > 0;
    }

}
