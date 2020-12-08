package com.android.util

import android.content.Context
import android.graphics.Color
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import java.util.*

/**
 * Created by xuzhb on 2020/12/4
 * Desc:底部选择器Android-PickerView工具类
 */
object PickerViewUtil {

    //选择日期
    fun selectDate(
        context: Context,
        listener: OnTimeSelectListener,
        currentTime: String = "",
        startTime: String = "",
        endTime: String = "",
        formatStr: String = "",
        titleText: String = "",
        cancelText: String = "取消",
        submitText: String = "确定",
        showDay: Boolean = true
    ) {
        val type: BooleanArray = booleanArrayOf(true, true, showDay, false, false, false)
        selectDateTime(context, listener, currentTime, startTime, endTime, formatStr, titleText, cancelText, submitText, type)
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
    fun selectDateTime(
        context: Context,
        listener: OnTimeSelectListener,
        currentTime: String = "",
        startTime: String = "",
        endTime: String = "",
        formatStr: String = "",
        titleText: String = "",
        cancelText: String = "取消",
        submitText: String = "确定",
        type: BooleanArray = booleanArrayOf(true, true, true, true, true, true)
    ) {
        val builder = TimePickerBuilder(context, listener)
        builder.setTitleText(titleText)  //设置标题文本
            .setTitleColor(Color.BLACK)
            .setTitleSize(18)
            .setCancelText(cancelText)  //设置标题栏的取消文本
            .setCancelColor(Color.parseColor("#6e6e6e"))
            .setSubmitText(submitText)  //设置标题栏的确定文本
            .setSubmitColor(Color.parseColor("#db4b3c"))
            .setSubCalSize(16)  //设置取消和确定文本的字体大小，以sp为单位
            .setTitleBgColor(Color.parseColor("#f7f8fd"))  //设置顶部标题栏的背景颜色
        val startCalendar = Calendar.getInstance()
        val startDate = DateUtil.string2Date(startTime, formatStr)
        startCalendar.time = startDate ?: DateUtil.string2Date("2000-01-01", DateUtil.Y_M_D)
        val endCalendar = Calendar.getInstance()
        val endDate = DateUtil.string2Date(endTime, formatStr)
        endCalendar.time = endDate ?: Date()
        val currentCalendar = Calendar.getInstance()
        val currentDate = DateUtil.string2Date(currentTime, formatStr)
        if (currentDate != null) {
            currentCalendar.time = currentDate
        }
        builder.setRangDate(startCalendar, endCalendar)  //设置选择的开始和结束时间
            .setLineSpacingMultiplier(2f)  //设置子项之间的间距
            .setDate(currentCalendar)  //设置当前选中的时间
            .setType(type)  //是否显示年月日时分秒
            .build()
            .show()
    }

}