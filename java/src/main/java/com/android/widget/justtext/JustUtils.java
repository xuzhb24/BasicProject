package com.android.widget.justtext;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2024/12/6
 * Desc:
 */
public class JustUtils {

    //首行是否缩进
    public static boolean isFirstIndent(String firstIndentText) {
        return !TextUtils.isEmpty(firstIndentText);
    }

    //末行是否缩进
    public static boolean isLastIndent(float lastIndentLength) {
        return lastIndentLength > 0;
    }

    //字体样式是否配置
    public static boolean isSpanConfig(ArrayList<SpanConfig> configs) {
        return configs != null && !configs.isEmpty();
    }

    //创建指定长度的首行缩进文本
    public static String createFirstIndent(float length, float textSize) {
        TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        String result = " ";
        while (length > paint.measureText(result)) {
            result += " ";
        }
        return result;
    }

    //获取配置的第一个字体颜色
    public static SpanConfig getSpanConfig(ArrayList<SpanConfig> configs, int position) {
        if (isSpanConfig(configs)) {
            for (SpanConfig config : configs) {
                if (config.isActive(position)) {
                    return config;
                }
            }
        }
        return null;
    }

    //获取有效的字体样式配置列表
    public static ArrayList<SpanConfig> getActiveSpanConfigList(ArrayList<SpanConfig> configs, boolean isTextBold) {
        ArrayList<SpanConfig> activeList = new ArrayList<>();
        if (isSpanConfig(configs)) {
            for (SpanConfig config : configs) {
                if ((!isTextBold && config.isBoldConfig()) || config.isColorConfig() || config.isSizeConfig()) {
                    activeList.add(config);
                }
            }
        }
        return activeList;
    }

    //复制传入TextPaint的所有属性
    public static TextPaint copyPaint(TextPaint tp) {
        TextPaint paint = new TextPaint();
        paint.set(tp);
        return paint;
    }

    //获取TextPaint在drawText时中线到baseline的距离，原理：https://blog.csdn.net/wangzhongshun/article/details/95341444
    public static float getDistanceFromCenterToBaseLine(TextPaint tp) {
        Paint.FontMetrics fm = tp.getFontMetrics();
        return (fm.descent - fm.ascent) / 2f - fm.descent;
    }

    //是否绘制完所有文本，nextPosition：下一个字绘制的位置
    public static boolean isDrawAllText(int nextPosition, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return nextPosition >= text.length();
    }

}
