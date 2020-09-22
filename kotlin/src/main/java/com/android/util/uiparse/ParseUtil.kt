package com.android.util.uiparse

import android.app.ActivityManager
import android.content.Context
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.basicproject.BuildConfig
import com.android.util.CheckFastClickUtil
import com.android.util.ScreenUtil
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/9/22
 * Desc:页面解析工具
 */
object ParseUtil {

    //双击屏幕顶部中间区域获取当前页面的Activity信息，只是调试用
    fun showTopActivityInfo(activity: AppCompatActivity, ev: MotionEvent?) {
        ev?.let {
            val screenWidth = ScreenUtil.getScreenWidth(activity)
            val width = SizeUtil.dp2px(40f)
            val height = SizeUtil.dp2px(80f)
            val left = (screenWidth - width) / 2f
            val right = left + width
            if (BuildConfig.DEBUG &&
                it.action == MotionEvent.ACTION_DOWN &&
                it.rawY < height && it.rawX > left && it.rawX < right
            ) {
                CheckFastClickUtil.setOnMultiClickListener(600) {
                    if (it == 2) {
                        ParseDialog.newInstance().show(activity.supportFragmentManager)
                    }
                }
            }
        }
    }

    fun getUIStructure(activity: FragmentActivity): UIStructure {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = manager.getRunningTasks(1)[0]
        val packageName = info.topActivity.packageName
        val className = info.topActivity.className
        val activityName = className.substring(className.lastIndexOf(".") + 1)
        val activityPath = className.substring(0, className.lastIndexOf("."))
        val fragmentList: MutableList<UIStructure.FragmentStructure> = mutableListOf()
        for (fragment in activity.supportFragmentManager.fragments) {
            if (fragment is ParseDialog) {
                continue
            }
//            if (fragment is SupportRequestManagerFragment) {
//                continue
//            }
            val parent = UIStructure.FragmentStructure(getFragmentName(fragment), fragment.userVisibleHint, null)
            fragmentList.add(parent)
            fragmentList.addAll(getChildFragments(fragment, parent))
        }
        return UIStructure(packageName, activityName, activityPath, fragmentList)
    }

    private fun getChildFragments(
        fragment: Fragment,
        parent: UIStructure.FragmentStructure
    ): MutableList<UIStructure.FragmentStructure> {
        val list: MutableList<UIStructure.FragmentStructure> = mutableListOf()
        for (f in fragment.childFragmentManager.fragments) {
            list.add(UIStructure.FragmentStructure(getFragmentName(f), f.userVisibleHint, parent))
        }
        return list
    }

    private fun getFragmentName(fragment: Fragment): String {
        val name = fragment.javaClass.name
        if (name.contains(".")) {
            return name.substring(name.lastIndexOf(".") + 1)
        }
        return name
    }

}