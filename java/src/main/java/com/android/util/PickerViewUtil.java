package com.android.util;

import android.content.Context;
import android.graphics.Color;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuzhb on 2020/2/20
 * Desc:底部选择器Android-PickerView工具类
 */
public class PickerViewUtil {

    //选择日期
    public static void selectDate(Context context, OnTimeSelectListener listener) {
        boolean[] type = new boolean[]{true, true, true, false, false, false};
        selectDateTime(context, listener, "", "", "", "",
                "", "取消", "确定", type);
    }

    //选择日期，curentTime的格式为"yyyy-MM-dd"
    public static void selectDate(Context context, OnTimeSelectListener listener, String curentTime) {
        boolean[] type = new boolean[]{true, true, true, false, false, false};
        selectDateTime(context, listener, curentTime, "", "", DateUtil.Y_M_D,
                "", "取消", "确定", type);
    }

    //选择日期
    public static void selectDate(
            Context context, OnTimeSelectListener listener,
            String currentTime, String startTime, String endTime, String formatStr
    ) {
        boolean[] type = new boolean[]{true, true, true, false, false, false};
        selectDateTime(context, listener, currentTime, startTime, endTime, formatStr,
                "", "取消", "确定", type);
    }

    //选择日期，showDay:是否显示日期，false只显示年月，true显示年月日
    public static void selectDate(
            Context context, OnTimeSelectListener listener,
            String currentTime, String startTime, String endTime, String formatStr,
            String titleText, String cancelText, String submitText, boolean showDay
    ) {
        boolean[] type = new boolean[]{true, true, showDay, false, false, false};
        selectDateTime(context, listener, currentTime, startTime, endTime, formatStr,
                titleText, cancelText, submitText, type);
    }

    //选择日期时间，同时显示年月日时分秒
    public static void selectDateTime(
            Context context, OnTimeSelectListener listener,
            String currentTime, String startTime, String endTime, String formatStr,
            String titleText, String cancelText, String submitText
    ) {
        boolean[] type = new boolean[]{true, true, true, true, true, true};
        selectDateTime(context, listener, currentTime, startTime, endTime, formatStr,
                titleText, cancelText, submitText, type);
    }

    /**
     * 选择日期时间
     *
     * @param listener    时间选择事件监听
     * @param currentTime 当前选中的时间，没有设置的话默认当前时间
     * @param startTime   选择的开始时间，没有设置的话默认"2000-01-01"
     * @param endTime     选择的结束时间，没有设置的话默认当前时间
     * @param formatStr   currentTime、startTime和endTime的时间格式
     * @param titleText   标题文本
     * @param cancelText  取消文本
     * @param submitText  确定文本
     * @param type        控制"年月日时分秒"的显示或隐藏，是一个大小为6的boolean数组
     */
    public static void selectDateTime(
            Context context, OnTimeSelectListener listener,
            String currentTime, String startTime, String endTime, String formatStr,
            String titleText, String cancelText, String submitText, boolean[] type
    ) {
        TimePickerBuilder builder = new TimePickerBuilder(context, listener);
        builder.setTitleText(titleText)  //设置标题文本
                .setTitleColor(Color.BLACK)
                .setTitleSize(18)
                .setCancelText(cancelText)  //设置标题栏的取消文本
                .setCancelColor(Color.parseColor("#6e6e6e"))
                .setSubmitText(submitText)  //设置标题栏的确定文本
                .setSubmitColor(Color.parseColor("#db4b3c"))
                .setSubCalSize(16)  //设置取消和确定文本的字体大小，以sp为单位
                .setTitleBgColor(Color.parseColor("#f7f8fd"));  //设置顶部标题栏的背景颜色
        Calendar startCalendar = Calendar.getInstance();
        Date startDate = DateUtil.string2Date(startTime, formatStr);
        startCalendar.setTime(startDate != null ? startDate : DateUtil.string2Date("2000-01-01", DateUtil.Y_M_D));
        Calendar endCalendar = Calendar.getInstance();
        Date endDate = DateUtil.string2Date(endTime, formatStr);
        endCalendar.setTime(endDate != null ? endDate : new Date());
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = DateUtil.string2Date(currentTime, formatStr);
        if (currentDate != null) {
            currentCalendar.setTime(currentDate);
        }
        builder.setRangDate(startCalendar, endCalendar)  //设置选择的开始和结束时间
                .setLineSpacingMultiplier(2)  //设置子项之间的间距
                .setDate(currentCalendar)  //设置当前选中的时间
                .setType(type)  //是否显示年月日时分秒
                .build()
                .show();
    }

}

